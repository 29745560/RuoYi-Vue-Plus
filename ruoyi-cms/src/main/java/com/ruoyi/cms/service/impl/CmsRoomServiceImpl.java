package com.ruoyi.cms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;
import com.ruoyi.common.exception.ServiceException;
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
        if (flag) {
            bo.setId(addRoom.getId());
        }
        return flag;
    }

    /**
     * 修改房间
     */
    @Override
    public Boolean updateByBo(CmsRoomBo bo) {
        CmsRoom updateRoom = BeanUtil.toBean(bo, CmsRoom.class);
        return roomMapper.updateById(updateRoom) > 0;
    }

    /**
     * 旅客退房
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean checkout(Long id) {
        CmsRoom room = roomMapper.selectById(id);
        //更新房间
        int updateRoomRows = roomMapper.update(null,
            Wrappers.lambdaUpdate(CmsRoom.class)
                .set(CmsRoom::getRoomRecordId, null)
                .set(CmsRoom::getStatus, "1")
                .eq(CmsRoom::getId, id)
        );
        if (updateRoomRows < 1) throw new ServiceException("更新房间信息失败, 请重试");

        //更新入住记录
        int updateRoomRecordRows = roomRecordMapper.update(null,
            Wrappers.lambdaUpdate(CmsRoomRecord.class)
                .set(CmsRoomRecord::getCheckOutDate, DateUtil.beginOfDay(DateUtil.date()))
                .eq(CmsRoomRecord::getId, room.getRoomRecordId())
                .eq(CmsRoomRecord::getRoomId, id)
        );
        if (updateRoomRecordRows < 1) throw new ServiceException("更新入住记录失败, 请重试");

        //更新物品
        int updateGoodsRows = goodsMapper.update(null,
            Wrappers.lambdaUpdate(CmsGoods.class)
                .set(CmsGoods::getRoomId, null)
                .set(CmsGoods::getStatus, "0")
                .eq(CmsGoods::getRoomId, id)
        );
        if (updateGoodsRows < 1) throw new ServiceException("更新物品信息失败, 请重试");

        //更新物品换洗记录
        int updateGoodsRecordRows = goodsRecordMapper.update(null,
            Wrappers.lambdaUpdate(CmsGoodsRecord.class)
                .set(CmsGoodsRecord::getEndDate, DateUtil.beginOfDay(DateUtil.date()))
                .eq(CmsGoodsRecord::getRoomId, id)
                .eq(CmsGoodsRecord::getRoomRecordId, room.getRoomRecordId())
                .isNull(CmsGoodsRecord::getEndDate)
        );
        if (updateGoodsRecordRows < 1) throw new ServiceException("更新换洗记录失败, 请重试");

        //获取物品换洗记录,并折算分数
        List<CmsGoodsRecord> goodsRecordList = goodsRecordMapper.selectList(
            Wrappers.lambdaQuery(CmsGoodsRecord.class)
                .eq(CmsGoodsRecord::getRoomId, id)
                .eq(CmsGoodsRecord::getRoomRecordId, room.getRoomRecordId())
        );
        if (CollUtil.isNotEmpty(goodsRecordList)) {
            List<CmsGoods> goodsList = goodsMapper.selectList();
            BigDecimal onceScore = BigDecimal.ZERO;
            for (CmsGoodsRecord record : goodsRecordList) {
                // 查找对应的物品分值
                CmsGoods goods = goodsList.stream().filter(m -> ObjectUtil.equal(m.getId(), record.getGoodsId())).findFirst().orElse(null);
                if (ObjectUtil.isNotNull(goods)) {
                    // 计算相隔天数
                    long days = DateUtil.between(record.getStartDate(), record.getEndDate(), DateUnit.DAY);
                    // 期初物品
                    if (StringUtils.equals("0", record.getStatus())) {
                        if (days > 0) {
                            onceScore = onceScore.add(BigDecimal.valueOf(days).multiply(goods.getScore()));
                        }
                    }
                    // 后续换洗物品,需冻结一天
                    else if (StringUtils.equals("1", record.getStatus())) {
                        if (days > 1) {
                            BigDecimal realDays = BigDecimal.valueOf(days).subtract(BigDecimal.ONE);
                            onceScore = onceScore.add(realDays.multiply(goods.getScore()));
                        }
                    }
                }
            }
            //更新旅客总分数
            CmsRoomRecord roomRecord = roomRecordMapper.selectRoomRecordById(room.getRoomRecordId());
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
