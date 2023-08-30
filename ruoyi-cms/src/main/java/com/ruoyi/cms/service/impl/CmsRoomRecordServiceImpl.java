package com.ruoyi.cms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.PageQuery;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.cms.domain.bo.CmsRoomRecordBo;
import com.ruoyi.cms.domain.vo.CmsGoodsVo;
import com.ruoyi.cms.domain.CmsGoods;
import com.ruoyi.cms.domain.CmsGoodsRecord;
import com.ruoyi.cms.domain.CmsGuest;
import com.ruoyi.cms.domain.CmsRoom;
import com.ruoyi.cms.domain.CmsRoomRecord;
import com.ruoyi.cms.mapper.CmsGoodsMapper;
import com.ruoyi.cms.mapper.CmsGoodsRecordMapper;
import com.ruoyi.cms.mapper.CmsGuestMapper;
import com.ruoyi.cms.mapper.CmsRoomMapper;
import com.ruoyi.cms.mapper.CmsRoomRecordMapper;
import com.ruoyi.cms.service.ICmsRoomRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 房间入住记录Service业务层处理
 *
 * @author ruoyi
 * @date 2023-08-02
 */
@RequiredArgsConstructor
@Service
public class CmsRoomRecordServiceImpl implements ICmsRoomRecordService {

    private final CmsRoomMapper roomMapper;
    private final CmsGoodsMapper goodsMapper;
    private final CmsGuestMapper guestMapper;
    private final CmsRoomRecordMapper roomRecordMapper;
    private final CmsGoodsRecordMapper goodsRecordMapper;

    /**
     * 分页查询房间入住记录列表
     */
    @Override
    public TableDataInfo<CmsRoomRecord> selectPageRoomRecordList(CmsRoomRecordBo bo, PageQuery pageQuery) {
        Page<CmsRoomRecord> page = roomRecordMapper.selectPageRoomRecordList(pageQuery.build(), this.buildQueryWrapper(bo));
        return TableDataInfo.build(page);
    }

    /**
     * 查询房间入住记录列表
     */
    @Override
    public List<CmsRoomRecord> selectRoomRecordList(CmsRoomRecordBo bo) {
        return roomRecordMapper.selectRoomRecordList(this.buildQueryWrapper(bo));
    }

    private Wrapper<CmsRoomRecord> buildQueryWrapper(CmsRoomRecordBo bo) {
        Map<String, Object> params = bo.getParams();
        QueryWrapper<CmsRoomRecord> wrapper = Wrappers.query();
        wrapper.eq(StringUtils.isNotBlank(bo.getCardId()), "r.card_id", bo.getCardId());
        wrapper.eq(ObjectUtil.isNotNull(bo.getRoomId()), "r.room_id", bo.getRoomId());
        if (ObjectUtil.isNotNull(params.get("beginDate")) && ObjectUtil.isNotNull(params.get("endDate"))) {
            wrapper.ge("r.check_in_date", params.get("beginDate"));
            wrapper.le("r.check_out_date", params.get("endDate"));
        }
        return wrapper;
    }

    /**
     * 查询房间入住记录详情
     */
    @Override
    public CmsRoomRecord selectRoomRecordById(Long id) {
        return roomRecordMapper.selectRoomRecordById(id);
    }

    /**
     * 新增房间入住记录
     */
    // TODO: [bug] 只能选择从今天开始, 否则要重构整个项目
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean insertRoomRecord(CmsRoomRecordBo bo) {
        if (DateUtil.compare(DateUtil.beginOfDay(DateUtil.date()), DateUtil.beginOfDay(bo.getCheckInDate())) != 0) {
            throw new ServiceException("入住日期必须是今日开始");
        }
        if (DateUtil.compare(DateUtil.beginOfDay(bo.getCheckInDate()), DateUtil.beginOfDay(bo.getCheckOutDate())) >= 0 ||
            DateUtil.between(DateUtil.beginOfDay(bo.getCheckInDate()), DateUtil.beginOfDay(bo.getCheckOutDate()), DateUnit.DAY) < 1) {
            throw new ServiceException("离店日期必须在入住日期之后");
        }
        CmsRoomRecord addRecord = BeanUtil.toBean(bo, CmsRoomRecord.class);
        boolean flag = roomRecordMapper.insert(addRecord) > 0;
        if (flag) {
            Long roomRecordId = addRecord.getId();
            bo.setId(roomRecordId);

            // 修改房间信息
            CmsRoom updateRoom = new CmsRoom();
            updateRoom.setId(bo.getRoomId());
            updateRoom.setRoomRecordId(roomRecordId);
            updateRoom.setStatus("2");
            int rows = roomMapper.updateById(updateRoom);
            if (rows < 1) {
                throw new ServiceException("房间信息修改失败, 请重试");
            }

            // 插入或更新旅客记录
            CmsGuest addGuest = new CmsGuest();
            addGuest.setCardId(bo.getCardId());
            addGuest.setRealname(bo.getRealname());
            addGuest.setPhone(bo.getPhone());
            addGuest.setStatus("0");
            addGuest.setRemark("入住操作");
            boolean gr = guestMapper.insertOrUpdate(addGuest);
            if (!gr) {
                throw new ServiceException("旅客信息插入操作失败, 请重试");
            }

            // 获取该房间内所有物品
            List<CmsGoodsVo> goodsList = goodsMapper.selectVoList(
                Wrappers.lambdaQuery(CmsGoods.class)
                    .eq(CmsGoods::getRoomId, bo.getRoomId())
                    .eq(CmsGoods::getStatus, "0")
            );
            if (CollUtil.isEmpty(goodsList)) {
                throw new ServiceException("该房间内暂无物品, 请添加后再重试");
            }
            List<CmsGoodsRecord> goodsRecords = new ArrayList<CmsGoodsRecord>();
            List<String> goodsIds = new ArrayList<String>();
            for (CmsGoodsVo goods : goodsList) {
                CmsGoodsRecord record = new CmsGoodsRecord();
                record.setRoomId(bo.getRoomId());
                record.setRoomRecordId(roomRecordId);
                record.setGoodsId(goods.getId());
                record.setStartDate(DateUtil.beginOfDay(bo.getCheckInDate()));
                record.setStatus("0");
                goodsRecords.add(record);
                goodsIds.add(goods.getId());
            }
            // 插入物品换洗记录
            boolean addGoodsRecord = goodsRecordMapper.insertBatch(goodsRecords);
            if (!addGoodsRecord) {
                throw new ServiceException("物品换洗记录插入操作失败, 请重试");
            }
            // 将房间内的物品都置为(使用中)状态
            int updateGoodsRows = goodsMapper.update(null,
                Wrappers.lambdaUpdate(CmsGoods.class)
                    .set(CmsGoods::getStatus, "1")
                    .in(CmsGoods::getId, goodsIds)
            );
            if (updateGoodsRows < 1) {
                throw new ServiceException("物品状态变更操作失败, 请重试");
            }
        }
        return flag;
    }

    /**
     * 修改房间入住记录
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateRoomRecord(CmsRoomRecordBo bo) {
        CmsRoomRecord updateRecord = BeanUtil.toBean(bo, CmsRoomRecord.class);
        int recordRows = roomRecordMapper.update(null,
            Wrappers.lambdaUpdate(CmsRoomRecord.class)
                .set(StringUtils.isNotBlank(updateRecord.getRemark()), CmsRoomRecord::getRemark, updateRecord.getRemark())
                .eq(CmsRoomRecord::getId, updateRecord.getId())
        );
        if (recordRows > 0) {
            // 若修改旅客信息
            CmsGuest updateGuest = new CmsGuest();
            updateGuest.setCardId(bo.getCardId());
            updateGuest.setRealname(bo.getRealname());
            updateGuest.setPhone(bo.getPhone());
            int guestRows = guestMapper.updateById(updateGuest);
            if (guestRows < 1) {
                throw new ServiceException("旅客信息更新操作失败,请重试");
            }
            return true;
        }
        return false;
    }

    /**
     * 批量删除房间入住记录
     */
    @Override
    public Boolean deleteRoomRecordByIds(Collection<Long> ids) {
        return roomRecordMapper.deleteBatchIds(ids) > 0;
    }
}
