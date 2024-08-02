package com.ruoyi.cms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.PageQuery;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.helper.LoginHelper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.cms.domain.bo.CmsRoomBo;
import com.ruoyi.cms.domain.vo.CmsRoomVo;
import com.ruoyi.cms.domain.CmsGoods;
import com.ruoyi.cms.domain.CmsGoodsRecord;
import com.ruoyi.cms.domain.CmsGuestScore;
import com.ruoyi.cms.domain.CmsRoom;
import com.ruoyi.cms.domain.CmsRoomRecord;
import com.ruoyi.cms.mapper.CmsGoodsMapper;
import com.ruoyi.cms.mapper.CmsGoodsRecordMapper;
import com.ruoyi.cms.mapper.CmsGuestScoreMapper;
import com.ruoyi.cms.mapper.CmsRoomMapper;
import com.ruoyi.cms.mapper.CmsRoomRecordMapper;
import com.ruoyi.cms.service.ICmsRoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * 房间Service业务层处理
 *
 * @author ruoyi
 * @date 2023-08-01
 */
@RequiredArgsConstructor
@Service
public class CmsRoomServiceImpl implements ICmsRoomService {

    private final CmsGoodsMapper goodsMapper;
    private final CmsGoodsRecordMapper goodsRecordMapper;
    private final CmsGuestScoreMapper guestScoreMapper;
    private final CmsRoomMapper roomMapper;
    private final CmsRoomRecordMapper roomRecordMapper;

    /**
     * 查询房间
     */
    @Override
    public CmsRoomVo queryById(Long id) {
        return roomMapper.selectVoById(id);
    }

    /**
     * 查询房间列表
     */
    @Override
    public TableDataInfo<CmsRoomVo> queryPageList(CmsRoomBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<CmsRoom> lqw = buildQueryWrapper(bo);
        Page<CmsRoomVo> result = roomMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询房间列表
     */
    @Override
    public List<CmsRoomVo> queryList(CmsRoomBo bo) {
        LambdaQueryWrapper<CmsRoom> lqw = buildQueryWrapper(bo);
        return roomMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<CmsRoom> buildQueryWrapper(CmsRoomBo bo) {
        LambdaQueryWrapper<CmsRoom> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtil.isNotNull(bo.getRoomRecordId()), CmsRoom::getRoomRecordId, bo.getRoomRecordId());
        wrapper.eq(StringUtils.isNotBlank(bo.getName()), CmsRoom::getName, bo.getName());
        wrapper.eq(StringUtils.isNotBlank(bo.getStatus()), CmsRoom::getStatus, bo.getStatus());
        wrapper.eq(CmsRoom::getDelFlag, "0");
        return wrapper;
    }

    /**
     * 新增房间
     */
    @Override
    public Boolean insertByBo(CmsRoomBo bo) {
        CmsRoom addRoom = BeanUtil.toBean(bo, CmsRoom.class);
        boolean flag = roomMapper.insert(addRoom) > 0;
        if (flag) bo.setId(addRoom.getId());
        return flag;
    }

    /**
     * 修改房间
     */
    @Override
    public Boolean updateByBo(CmsRoomBo bo) {
        CmsRoom room = roomMapper.selectById(bo.getId());
        if (ObjectUtil.isNull(room)) {
            throw new ServiceException("房间信息不存在, 请重试");
        } else if (ObjectUtil.isNotNull(bo.getCategoryId())) {
            if (!StringUtils.equals("0", room.getStatus())) {
                throw new ServiceException("无法更新房间类型, 请确保房间是空闲状态");
            }
        }
        CmsRoom updateRoom = BeanUtil.toBean(bo, CmsRoom.class);
        return roomMapper.updateById(updateRoom) > 0;
    }

    /**
     * 宾客退房
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean checkout(Long id, String payStatus) {
        CmsRoom room = roomMapper.selectById(id);
        if (ObjectUtil.isNull(room)) {
            throw new ServiceException("房间信息不存在, 请重试");
        } else if (ObjectUtil.isNull(room.getRoomRecordId())) {
            throw new ServiceException("入住订单不存在, 请重试");
        }
        CmsRoomRecord roomRecord = roomRecordMapper.selectRoomRecordById(room.getRoomRecordId());
        if (ObjectUtil.isNull(roomRecord)) throw new ServiceException("入住信息不存在, 请重试");
        long days = DateUtil.betweenDay(roomRecord.getCheckInDate(), DateUtil.date(), true);
        if (days < 1) throw new ServiceException("入住不满1晚, 不可退房");
        // 计算待支付金额
        BigDecimal unitPrice = ObjectUtil.isNull(roomRecord.getUnitPrice()) ? BigDecimal.ZERO : roomRecord.getUnitPrice();
        BigDecimal totalAmount = unitPrice.multiply(BigDecimal.valueOf(days));

        //更新房间
        int updateRoomRows = roomMapper.update(null,
            new LambdaUpdateWrapper<CmsRoom>()
                .set(CmsRoom::getRoomRecordId, null)
                .set(CmsRoom::getStatus, "1")//更新为整理中状态
                .set(CmsRoom::getUpdateBy, LoginHelper.getUsername())
                .set(CmsRoom::getUpdateTime, DateUtils.getNowDate())
                .eq(CmsRoom::getId, id)
        );
        if (updateRoomRows < 1) throw new ServiceException("更新房间信息失败, 请重试");

        //更新入住记录
        int updateRoomRecordRows = roomRecordMapper.update(null,
            new LambdaUpdateWrapper<CmsRoomRecord>()
                .set(CmsRoomRecord::getCheckOutDate, DateUtil.beginOfDay(DateUtil.date()))
                .set(CmsRoomRecord::getTotalAmount, totalAmount)
                .set(CmsRoomRecord::getPay, payStatus)
                .set(CmsRoomRecord::getUpdateBy, LoginHelper.getUsername())
                .set(CmsRoomRecord::getUpdateTime, DateUtils.getNowDate())
                .eq(CmsRoomRecord::getId, room.getRoomRecordId())
                .eq(CmsRoomRecord::getRoomId, id)
        );
        if (updateRoomRecordRows < 1) throw new ServiceException("更新入住记录失败, 请重试");

        //更新物品
        int updateGoodsRows = goodsMapper.update(null,
            new LambdaUpdateWrapper<CmsGoods>()
                .set(CmsGoods::getRoomId, null)
                .set(CmsGoods::getStatus, "0")
                .set(CmsGoods::getUpdateBy, LoginHelper.getUsername())
                .set(CmsGoods::getUpdateTime, DateUtils.getNowDate())
                .eq(CmsGoods::getRoomId, id)
        );
        if (updateGoodsRows < 1) throw new ServiceException("更新物品信息失败, 请重试");

        //更新物品换洗记录
        int updateGoodsRecordRows = goodsRecordMapper.update(null,
            new LambdaUpdateWrapper<CmsGoodsRecord>()
                .set(CmsGoodsRecord::getEndDate, DateUtil.beginOfDay(DateUtil.date()))
                .set(CmsGoodsRecord::getUpdateBy, LoginHelper.getUsername())
                .set(CmsGoodsRecord::getUpdateTime, DateUtils.getNowDate())
                .eq(CmsGoodsRecord::getRoomId, id)
                .eq(CmsGoodsRecord::getRoomRecordId, room.getRoomRecordId())
                .isNull(CmsGoodsRecord::getEndDate)
        );
        if (updateGoodsRecordRows < 1) throw new ServiceException("更新换洗记录失败, 请重试");

        //获取物品换洗记录,并折算分数
        List<CmsGoodsRecord> goodsRecordList = goodsRecordMapper.selectList(
            new LambdaQueryWrapper<CmsGoodsRecord>()
                .eq(CmsGoodsRecord::getRoomId, id)
                .eq(CmsGoodsRecord::getRoomRecordId, room.getRoomRecordId())
        );
        if (CollUtil.isNotEmpty(goodsRecordList)) {
            List<CmsGoods> goodsList = goodsMapper.selectList();
            BigDecimal onceScore = BigDecimal.ZERO;
            for (CmsGoodsRecord record : goodsRecordList) {
                // 查找对应的物品分值,并且这个物品是环保物品
                CmsGoods goods = goodsList.stream().filter(m -> ObjectUtil.equal(m.getId(), record.getGoodsId())).findFirst().orElse(null);
                if (ObjectUtil.isNotNull(goods) && StringUtils.equals("1", goods.getType())) {
                    // 计算相隔天数
                    long dayBetween = DateUtil.betweenDay(record.getStartDate(), record.getEndDate(), true);
                    // 期初物品
                    if (StringUtils.equals("0", record.getStatus())) {
                        if (dayBetween > 0) {
                            onceScore = onceScore.add(BigDecimal.valueOf(days).multiply(goods.getScore()));
                        }
                    }
                    // 后续换洗物品,需冻结一天
                    else if (StringUtils.equals("1", record.getStatus())) {
                        if (dayBetween > 1) {
                            BigDecimal realDays = BigDecimal.valueOf(days).subtract(BigDecimal.ONE);//减去1天
                            onceScore = onceScore.add(realDays.multiply(goods.getScore()));
                        }
                    }
                }
            }
            //更新旅客总分数
            CmsGuestScore guestScore = guestScoreMapper.selectById(roomRecord.getCardId());
            if (ObjectUtil.isNull(guestScore)) {
                guestScore = new CmsGuestScore();
                guestScore.setCardId(roomRecord.getCardId());
                guestScore.setScoreTotal(onceScore);
                guestScore.setScoreUsable(onceScore);
            } else {
                BigDecimal scoreTotal = guestScore.getScoreTotal();
                BigDecimal scoreUsable = guestScore.getScoreUsable();
                if (ObjectUtil.isNull(scoreTotal)) scoreTotal = BigDecimal.ZERO;
                if (ObjectUtil.isNull(scoreUsable)) scoreUsable = BigDecimal.ZERO;
                scoreTotal = onceScore.add(scoreTotal);
                scoreUsable = onceScore.add(scoreUsable);
                guestScore.setScoreTotal(scoreTotal);
                guestScore.setScoreUsable(scoreUsable);
            }
            guestScore.setUpdateBy(LoginHelper.getUsername());
            guestScore.setUpdateTime(DateUtils.getNowDate());
            boolean updateScore = guestScoreMapper.insertOrUpdate(guestScore);
            if (!updateScore) throw new ServiceException("更新旅客积分失败, 请重试");
        }
        return true;
    }

    /**
     * 批量删除房间
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids) {
        return roomMapper.deleteBatchIds(ids) > 0;
    }
}
