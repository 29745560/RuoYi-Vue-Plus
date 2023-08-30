package com.ruoyi.cms.service;

import com.ruoyi.cms.domain.vo.CmsGoodsVo;
import com.ruoyi.cms.domain.bo.CmsGoodsBo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;
import java.util.Collection;
import java.util.List;

/**
 * 物品Service接口
 *
 * @author ruoyi
 * @date 2023-08-04
 */
public interface ICmsGoodsService {

    /**
     * 查询物品
     */
    CmsGoodsVo selectGoodsById(String id);

    /**
     * 查询物品列表
     */
    TableDataInfo<CmsGoodsVo> selectPageGoodsList(CmsGoodsBo bo, PageQuery pageQuery);

    /**
     * 查询物品列表
     */
    List<CmsGoodsVo> selectGoodsList(CmsGoodsBo bo);

    /**
     * 新增物品
     */
    Boolean insertGoods(CmsGoodsBo bo);

    /**
     * 修改物品
     */
    Boolean updateGoods(CmsGoodsBo bo);

    /**
     * 物品与房间的关联
     */
    Boolean updateGoodsRoomByIds(Boolean binding, Long roomId, Collection<String> ids);

    /**
     * 校验并批量删除物品信息
     */
    Boolean deleteGoodsByIds(Collection<String> ids);
}
