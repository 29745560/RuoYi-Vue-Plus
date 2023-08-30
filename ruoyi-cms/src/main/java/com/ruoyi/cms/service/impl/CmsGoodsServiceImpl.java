package com.ruoyi.cms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.PageQuery;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.cms.domain.bo.CmsGoodsBo;
import com.ruoyi.cms.domain.vo.CmsGoodsVo;
import com.ruoyi.cms.domain.CmsCategory;
import com.ruoyi.cms.domain.CmsGoods;
import com.ruoyi.cms.domain.CmsGoodsRecord;
import com.ruoyi.cms.domain.CmsRoom;
import com.ruoyi.cms.mapper.CmsCategoryMapper;
import com.ruoyi.cms.mapper.CmsGoodsMapper;
import com.ruoyi.cms.mapper.CmsGoodsRecordMapper;
import com.ruoyi.cms.mapper.CmsRoomMapper;
import com.ruoyi.cms.service.ICmsGoodsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 物品Service业务层处理
 *
 * @author ruoyi
 * @date 2023-08-04
 */
@RequiredArgsConstructor
@Service
public class CmsGoodsServiceImpl implements ICmsGoodsService {

    private final CmsGoodsMapper goodsMapper;
    private final CmsGoodsRecordMapper goodsRecordMapper;
    private final CmsCategoryMapper categoryMapper;
    private final CmsRoomMapper roomMapper;

    /**
     * 分页查询物品列表
     */
    @Override
    public TableDataInfo<CmsGoodsVo> selectPageGoodsList(CmsGoodsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<CmsGoods> wrapper = buildQueryWrapper(bo);
        Page<CmsGoodsVo> page = goodsMapper.selectVoPage(pageQuery.build(), wrapper);
        page.setRecords(transferIdsToNames(page.getRecords()));
        return TableDataInfo.build(page);
    }

    /**
     * 查询物品列表
     */
    @Override
    public List<CmsGoodsVo> selectGoodsList(CmsGoodsBo bo) {
        LambdaQueryWrapper<CmsGoods> wrapper = buildQueryWrapper(bo);
        return transferIdsToNames(goodsMapper.selectVoList(wrapper));
    }

    private List<CmsGoodsVo> transferIdsToNames(List<CmsGoodsVo> list) {
        if (CollUtil.isNotEmpty(list)) {
            List<CmsRoom> roomList = roomMapper.selectList();
            List<CmsCategory> categoryList = categoryMapper.selectList();
            for (CmsGoodsVo goodsVo : list) {
                Long _roomId = goodsVo.getRoomId();
                Long _categoryId = goodsVo.getCategoryId();
                if (ObjectUtil.isNotNull(_roomId)) {
                    CmsRoom room = roomList.stream().filter(m -> ObjectUtil.equal(m.getId(), _roomId)).findFirst().orElse(null);
                    if (ObjectUtil.isNotNull(room)) goodsVo.setRoomName(room.getName());
                }
                if (ObjectUtil.isNotNull(_categoryId)) {
                    CmsCategory category = categoryList.stream().filter(m -> ObjectUtil.equal(m.getId(), _categoryId)).findFirst().orElse(null);
                    if (ObjectUtil.isNotNull(category)) goodsVo.setCategoryName(category.getName());
                }
            }
        }
        return list;
    }

    private LambdaQueryWrapper<CmsGoods> buildQueryWrapper(CmsGoodsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<CmsGoods> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotBlank(bo.getName()), CmsGoods::getName, bo.getName());
        wrapper.eq(ObjectUtil.isNotNull(bo.getCategoryId()), CmsGoods::getCategoryId, bo.getCategoryId());
        wrapper.eq(CmsGoods::getDelFlag, "0");
        if (StringUtils.equals("true", ObjectUtil.toString(params.get("select_usable")))) {
            wrapper.isNull(CmsGoods::getRoomId);
            wrapper.eq(CmsGoods::getStatus, "0");
        } else {
            wrapper.eq(ObjectUtil.isNotNull(bo.getRoomId()), CmsGoods::getRoomId, bo.getRoomId());
            wrapper.eq(StringUtils.isNotBlank(bo.getStatus()), CmsGoods::getStatus, bo.getStatus());
        }
        return wrapper;
    }

    /**
     * 查询物品详情
     */
    @Override
    public CmsGoodsVo selectGoodsById(String id) {
        return goodsMapper.selectVoById(id);
    }

    /**
     * 新增物品
     */
    @Override
    public Boolean insertGoods(CmsGoodsBo bo) {
        CmsGoods addGoods = BeanUtil.toBean(bo, CmsGoods.class);
        return goodsMapper.insert(addGoods) > 0;
    }

    /**
     * 修改物品
     */
    @Override
    public Boolean updateGoods(CmsGoodsBo bo) {
        CmsGoods updateGoods = BeanUtil.toBean(bo, CmsGoods.class);
        return goodsMapper.updateById(updateGoods) > 0;
    }

    /**
     * 物品与房间的关联
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateGoodsRoomByIds(Boolean binding, Long roomId, Collection<String> ids) {
        // 查询当前房间信息
        CmsRoom room = roomMapper.selectById(roomId);
        if (ObjectUtil.equals(Boolean.TRUE, binding)) {
            // 添加物品与房间的关联
            int rows = goodsMapper.update(null,
                Wrappers.lambdaUpdate(CmsGoods.class)
                    .set(CmsGoods::getRoomId, roomId)
                    .set(StringUtils.equals("2", room.getStatus()), CmsGoods::getStatus, "1")//若当前房间已入住,那么将物品设为使用状态
                    .eq(CmsGoods::getStatus, "0")
                    .in(CmsGoods::getId, ids)
            );
            // 已入住的情况下,才添加物品换洗记录
            if (rows > 0 && StringUtils.equals("2", room.getStatus())) {
                List<CmsGoodsRecord> records = new ArrayList<CmsGoodsRecord>();
                for (String id : ids) {
                    CmsGoodsRecord gr = new CmsGoodsRecord();
                    gr.setRoomId(roomId);
                    gr.setRoomRecordId(room.getRoomRecordId());
                    gr.setGoodsId(id);
                    gr.setStartDate(DateUtil.beginOfDay(DateUtil.date()));
                    gr.setStatus("1");
                    records.add(gr);
                }
                boolean addRecords = goodsRecordMapper.insertBatch(records);
                if (addRecords) return true;
                else throw new ServiceException("批量新增物品换洗记录失败, 请重试");
            } else {
                return rows > 0;
            }
        } else {
            // 移除物品与房间的关联
            int rows = goodsMapper.update(null,
                Wrappers.lambdaUpdate(CmsGoods.class)
                    .set(CmsGoods::getRoomId, null)
                    .set(CmsGoods::getStatus, "0")
                    .in(CmsGoods::getId, ids)
            );
            if (rows > 0) {
                // 查找并更新当前房间入住记录下的物品
                int updateRecords = goodsRecordMapper.update(null,
                    Wrappers.lambdaUpdate(CmsGoodsRecord.class)
                        .set(CmsGoodsRecord::getEndDate, DateUtil.beginOfDay(DateUtil.date()))
                        .eq(CmsGoodsRecord::getRoomId, roomId)
                        .eq(CmsGoodsRecord::getRoomRecordId, room.getRoomRecordId())
                        .isNull(CmsGoodsRecord::getEndDate)
                        .in(CmsGoodsRecord::getGoodsId, ids)
                );
                // 已入住的情况下,若更新物品换洗记录失败
                if (StringUtils.equals("2", room.getStatus()) && updateRecords < 1) {
                    throw new ServiceException("批量更新物品换洗记录失败, 请重试");
                }
                return true;
            }
            return false;
        }
    }

    /**
     * 批量删除物品
     */
    @Override
    public Boolean deleteGoodsByIds(Collection<String> ids) {
        return goodsMapper.deleteBatchIds(ids) > 0;
    }
}
