package com.ruoyi.cms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.cms.domain.bo.CmsRoomCategoryBo;
import com.ruoyi.cms.domain.vo.CmsRoomCategoryVo;
import com.ruoyi.cms.domain.CmsRoomCategory;
import com.ruoyi.cms.mapper.CmsRoomCategoryMapper;
import com.ruoyi.cms.service.ICmsRoomCategoryService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.Collection;
import java.util.List;

/**
 * 房间类型Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-29
 */
@RequiredArgsConstructor
@Service
public class CmsRoomCategoryServiceImpl implements ICmsRoomCategoryService {

    private final CmsRoomCategoryMapper roomCategoryMapper;

    /**
     * 查询客房类型
     */
    @Override
    public CmsRoomCategoryVo selectRoomCategoryById(Long id) {
        return roomCategoryMapper.selectVoById(id);
    }

    /**
     * 查询客房类型列表
     */
    @Override
    public List<CmsRoomCategoryVo> selectRoomCategoryList(CmsRoomCategoryBo bo) {
        LambdaQueryWrapper<CmsRoomCategory> wrapper = buildQueryWrapper(bo);
        return roomCategoryMapper.selectVoList(wrapper);
    }

    private LambdaQueryWrapper<CmsRoomCategory> buildQueryWrapper(CmsRoomCategoryBo bo) {
        LambdaQueryWrapper<CmsRoomCategory> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotBlank(bo.getName()), CmsRoomCategory::getName, bo.getName());
        wrapper.eq(ObjectUtil.isNotNull(bo.getBeds()), CmsRoomCategory::getBeds, bo.getBeds());
        return wrapper;
    }

    /**
     * 新增客房分类
     */
    @Override
    public Boolean insertRoomCategory(CmsRoomCategoryBo bo) {
        CmsRoomCategory category = BeanUtil.toBean(bo, CmsRoomCategory.class);
        boolean flag = roomCategoryMapper.insert(category) > 0;
        if (flag) bo.setId(category.getId());
        return flag;
    }

    /**
     * 修改客房分类
     */
    @Override
    public Boolean updateRoomCategory(CmsRoomCategoryBo bo) {
        CmsRoomCategory category = BeanUtil.toBean(bo, CmsRoomCategory.class);
        return roomCategoryMapper.updateById(category) > 0;
    }

    /**
     * 批量删除客房分类
     */
    @Override
    public Boolean deleteRoomCategoryByIds(Collection<Long> ids) {
        return roomCategoryMapper.deleteBatchIds(ids) > 0;
    }
}
