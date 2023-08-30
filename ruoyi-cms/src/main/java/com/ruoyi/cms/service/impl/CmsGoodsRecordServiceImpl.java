package com.ruoyi.cms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.core.domain.PageQuery;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.cms.domain.bo.CmsGoodsRecordBo;
import com.ruoyi.cms.domain.vo.CmsGoodsRecordVo;
import com.ruoyi.cms.domain.CmsGoods;
import com.ruoyi.cms.domain.CmsGoodsRecord;
import com.ruoyi.cms.domain.CmsRoom;
import com.ruoyi.cms.mapper.CmsGoodsMapper;
import com.ruoyi.cms.mapper.CmsGoodsRecordMapper;
import com.ruoyi.cms.mapper.CmsRoomMapper;
import com.ruoyi.cms.service.ICmsGoodsRecordService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;

/**
 * 房间换洗记录Service业务层处理
 *
 * @author ruoyi
 * @date 2023-08-09
 */
@RequiredArgsConstructor
@Service
public class CmsGoodsRecordServiceImpl implements ICmsGoodsRecordService {

    private final CmsRoomMapper roomMapper;
    private final CmsGoodsMapper goodsMapper;
    private final CmsGoodsRecordMapper goodsRecordMapper;

    /**
     * 查询房间换洗记录
     */
    @Override
    public CmsGoodsRecordVo queryById(Long id) {
        return goodsRecordMapper.selectVoById(id);
    }

    /**
     * 查询房间换洗记录列表
     */
    @Override
    public TableDataInfo<CmsGoodsRecordVo> queryPageList(CmsGoodsRecordBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<CmsGoodsRecord> wrapper = buildQueryWrapper(bo);
        Page<CmsGoodsRecordVo> page = goodsRecordMapper.selectVoPage(pageQuery.build(), wrapper);
        page.setRecords(transferIdsToNames(page.getRecords()));
        return TableDataInfo.build(page);
    }

    /**
     * 查询房间换洗记录列表
     */
    @Override
    public List<CmsGoodsRecordVo> queryList(CmsGoodsRecordBo bo) {
        LambdaQueryWrapper<CmsGoodsRecord> wrapper = buildQueryWrapper(bo);
        return transferIdsToNames(goodsRecordMapper.selectVoList(wrapper));
    }

    private List<CmsGoodsRecordVo> transferIdsToNames(List<CmsGoodsRecordVo> list) {
        if (CollUtil.isNotEmpty(list)) {
            List<CmsRoom> roomList = roomMapper.selectList();
            List<CmsGoods> goodsList = goodsMapper.selectList();
            for (CmsGoodsRecordVo goodsVo : list) {
                Long _roomId = goodsVo.getRoomId();
                String _goodsId = goodsVo.getGoodsId();
                if (ObjectUtil.isNotNull(_roomId)) {
                    CmsRoom room = roomList.stream().filter(m -> ObjectUtil.equal(m.getId(), _roomId)).findFirst().orElse(null);
                    if (ObjectUtil.isNotNull(room)) goodsVo.setRoomName(room.getName());
                }
                if (StringUtils.isNotBlank(_goodsId)) {
                    CmsGoods goods = goodsList.stream().filter(m -> ObjectUtil.equal(m.getId(), _goodsId)).findFirst().orElse(null);
                    if (ObjectUtil.isNotNull(goods)) goodsVo.setGoodsName(goods.getName());
                }
            }
        }
        return list;
    }

    private LambdaQueryWrapper<CmsGoodsRecord> buildQueryWrapper(CmsGoodsRecordBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<CmsGoodsRecord> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtil.isNotNull(bo.getRoomId()), CmsGoodsRecord::getRoomId, bo.getRoomId());
        wrapper.eq(ObjectUtil.isNotNull(bo.getRoomRecordId()), CmsGoodsRecord::getRoomRecordId, bo.getRoomRecordId());
        wrapper.eq(StringUtils.isNotBlank(bo.getGoodsId()), CmsGoodsRecord::getGoodsId, bo.getGoodsId());
        wrapper.eq(StringUtils.isNotBlank(bo.getStatus()), CmsGoodsRecord::getStatus, bo.getStatus());
        if (ObjectUtil.isNotNull(params.get("beginDate")) && ObjectUtil.isNotNull(params.get("endDate"))) {
            wrapper.ge(CmsGoodsRecord::getStartDate, params.get("beginDate"));
            wrapper.le(CmsGoodsRecord::getEndDate, params.get("endDate"));
        }
        return wrapper;
    }
}
