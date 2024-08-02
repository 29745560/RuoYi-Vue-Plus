package com.ruoyi.cms.service;

import com.ruoyi.cms.domain.vo.CmsGoodsCategoryVo;
import com.ruoyi.cms.domain.bo.CmsGoodsCategoryBo;

import java.util.Collection;
import java.util.List;

/**
 * 物品分类Service接口
 *
 * @author ruoyi
 * @date 2023-08-01
 */
public interface ICmsGoodsCategoryService {

    /**
     * 查询物品分类
     */
    CmsGoodsCategoryVo queryById(Long id);


    /**
     * 查询物品分类列表
     */
    List<CmsGoodsCategoryVo> queryList(CmsGoodsCategoryBo bo);

    /**
     * 新增物品分类
     */
    Boolean insertByBo(CmsGoodsCategoryBo bo);

    /**
     * 修改物品分类
     */
    Boolean updateByBo(CmsGoodsCategoryBo bo);

    /**
     * 校验并批量删除物品分类信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids);
}
