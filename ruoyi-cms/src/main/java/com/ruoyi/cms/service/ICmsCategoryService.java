package com.ruoyi.cms.service;

import com.ruoyi.cms.domain.vo.CmsCategoryVo;
import com.ruoyi.cms.domain.bo.CmsCategoryBo;

import java.util.Collection;
import java.util.List;

/**
 * 物品分类Service接口
 *
 * @author ruoyi
 * @date 2023-08-01
 */
public interface ICmsCategoryService {

    /**
     * 查询物品分类
     */
    CmsCategoryVo queryById(Long id);


    /**
     * 查询物品分类列表
     */
    List<CmsCategoryVo> queryList(CmsCategoryBo bo);

    /**
     * 新增物品分类
     */
    Boolean insertByBo(CmsCategoryBo bo);

    /**
     * 修改物品分类
     */
    Boolean updateByBo(CmsCategoryBo bo);

    /**
     * 校验并批量删除物品分类信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids);
}
