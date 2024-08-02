package com.ruoyi.cms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.cms.domain.bo.CmsGoodsCategoryBo;
import com.ruoyi.cms.domain.vo.CmsGoodsCategoryVo;
import com.ruoyi.cms.domain.CmsGoodsCategory;
import com.ruoyi.cms.mapper.CmsGoodsCategoryMapper;
import com.ruoyi.cms.service.ICmsGoodsCategoryService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.Collection;
import java.util.List;

/**
 * 物品分类Service业务层处理
 *
 * @author ruoyi
 * @date 2023-08-01
 */
@RequiredArgsConstructor
@Service
public class CmsGoodsCategoryServiceImpl implements ICmsGoodsCategoryService {

    private final CmsGoodsCategoryMapper goodsCategoryMapper;

    /**
     * 查询物品分类
     */
    @Override
    public CmsGoodsCategoryVo queryById(Long id) {
        return goodsCategoryMapper.selectVoById(id);
    }

    /**
     * 查询物品分类列表
     */
    @Override
    public List<CmsGoodsCategoryVo> queryList(CmsGoodsCategoryBo bo) {
        LambdaQueryWrapper<CmsGoodsCategory> wrapper = buildQueryWrapper(bo);
        return goodsCategoryMapper.selectVoList(wrapper);
    }

    private LambdaQueryWrapper<CmsGoodsCategory> buildQueryWrapper(CmsGoodsCategoryBo bo) {
        LambdaQueryWrapper<CmsGoodsCategory> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotBlank(bo.getName()), CmsGoodsCategory::getName, bo.getName());
        wrapper.eq(StringUtils.isNotBlank(bo.getStatus()), CmsGoodsCategory::getStatus, bo.getStatus());
        return wrapper;
    }

    /**
     * 新增物品分类
     */
    @Override
    public Boolean insertByBo(CmsGoodsCategoryBo bo) {
        CmsGoodsCategory addCategory = BeanUtil.toBean(bo, CmsGoodsCategory.class);
        boolean flag = goodsCategoryMapper.insert(addCategory) > 0;
        if (flag) bo.setId(addCategory.getId());
        return flag;
    }

    /**
     * 修改物品分类
     */
    @Override
    public Boolean updateByBo(CmsGoodsCategoryBo bo) {
        CmsGoodsCategory updateCategory = BeanUtil.toBean(bo, CmsGoodsCategory.class);
        return goodsCategoryMapper.updateById(updateCategory) > 0;
    }

    /**
     * 批量删除物品分类
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids) {
        return goodsCategoryMapper.deleteBatchIds(ids) > 0;
    }
}
