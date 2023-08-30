package com.ruoyi.cms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.PageQuery;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.cms.domain.bo.CmsUserBo;
import com.ruoyi.cms.domain.vo.CmsUserVo;
import com.ruoyi.cms.domain.CmsUser;
import com.ruoyi.cms.mapper.CmsUserMapper;
import com.ruoyi.cms.service.ICmsUserService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.Collection;
import java.util.List;

/**
 * 微信用户Service业务层处理
 *
 * @author ruoyi
 * @date 2023-08-16
 */
@RequiredArgsConstructor
@Service
public class CmsUserServiceImpl implements ICmsUserService {

    private final CmsUserMapper userMapper;

    /**
     * 查询微信用户
     */
    @Override
    public CmsUserVo selectUserById(Long id) {
        return userMapper.selectVoById(id);
    }

    /**
     * 查询微信用户列表
     */
    @Override
    public TableDataInfo<CmsUserVo> selectUserPageList(CmsUserBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<CmsUser> wrapper = buildQueryWrapper(bo);
        Page<CmsUserVo> result = userMapper.selectVoPage(pageQuery.build(), wrapper);
        return TableDataInfo.build(result);
    }

    /**
     * 查询微信用户列表
     */
    @Override
    public List<CmsUserVo> selectUserList(CmsUserBo bo) {
        LambdaQueryWrapper<CmsUser> wrapper = buildQueryWrapper(bo);
        return userMapper.selectVoList(wrapper);
    }

    private LambdaQueryWrapper<CmsUser> buildQueryWrapper(CmsUserBo bo) {
        LambdaQueryWrapper<CmsUser> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtils.isNotBlank(bo.getCardId()), CmsUser::getCardId, bo.getCardId());
        wrapper.eq(StringUtils.isNotBlank(bo.getPhone()), CmsUser::getPhone, bo.getPhone());
        wrapper.eq(StringUtils.isNotBlank(bo.getSource()), CmsUser::getSource, bo.getSource());
        wrapper.eq(StringUtils.isNotBlank(bo.getStatus()), CmsUser::getStatus, bo.getStatus());
        return wrapper;
    }

    /**
     * 修改微信用户
     */
    @Override
    public Boolean updateUser(CmsUserBo bo) {
        CmsUser update = BeanUtil.toBean(bo, CmsUser.class);
        return userMapper.updateById(update) > 0;
    }

    /**
     * 批量删除微信用户
     */
    @Override
    public Boolean deleteUserByIds(Collection<Long> ids) {
        return userMapper.deleteBatchIds(ids) > 0;
    }
}
