package com.ruoyi.cms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.cms.domain.bo.CmsCategoryBo;
import com.ruoyi.cms.domain.vo.CmsCategoryVo;
import com.ruoyi.cms.domain.CmsCategory;
import com.ruoyi.cms.mapper.CmsCategoryMapper;
import com.ruoyi.cms.service.ICmsCategoryService;
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
public class CmsCategoryServiceImpl implements ICmsCategoryService {

    private final CmsCategoryMapper categoryMapper;

    /**
     * 查询物品分类
     */
    @Override
    public CmsCategoryVo queryById(Long id) {
        return categoryMapper.selectVoById(id);
    }

    /**
     * 查询物品分类列表
     */
    @Override
    public List<CmsCategoryVo> queryList(CmsCategoryBo bo) {
        LambdaQueryWrapper<CmsCategory> lqw = buildQueryWrapper(bo);
        return categoryMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<CmsCategory> buildQueryWrapper(CmsCategoryBo bo) {
        LambdaQueryWrapper<CmsCategory> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotBlank(bo.getName()), CmsCategory::getName, bo.getName());
        wrapper.eq(StringUtils.isNotBlank(bo.getStatus()), CmsCategory::getStatus, bo.getStatus());
        return wrapper;
    }

    /**
     * 新增物品分类
     */
    @Override
    public Boolean insertByBo(CmsCategoryBo bo) {
        CmsCategory addCategory = BeanUtil.toBean(bo, CmsCategory.class);
        boolean flag = categoryMapper.insert(addCategory) > 0;
        if (flag) {
            bo.setId(addCategory.getId());
        }
        return flag;
    }

    /**
     * 修改物品分类
     */
    @Override
    public Boolean updateByBo(CmsCategoryBo bo) {
        CmsCategory updateCategory = BeanUtil.toBean(bo, CmsCategory.class);
        return categoryMapper.updateById(updateCategory) > 0;
    }

    /**
     * 批量删除物品分类
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids) {
        return categoryMapper.deleteBatchIds(ids) > 0;
    }
}
