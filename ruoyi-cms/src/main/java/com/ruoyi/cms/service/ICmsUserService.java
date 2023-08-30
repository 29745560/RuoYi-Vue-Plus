package com.ruoyi.cms.service;

import com.ruoyi.cms.domain.vo.CmsUserVo;
import com.ruoyi.cms.domain.bo.CmsUserBo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;
import java.util.Collection;
import java.util.List;

/**
 * 微信用户Service接口
 *
 * @author ruoyi
 * @date 2023-08-16
 */
public interface ICmsUserService {

    /**
     * 查询微信用户
     */
    CmsUserVo selectUserById(Long id);

    /**
     * 查询微信用户列表
     */
    TableDataInfo<CmsUserVo> selectUserPageList(CmsUserBo bo, PageQuery pageQuery);

    /**
     * 查询微信用户列表
     */
    List<CmsUserVo> selectUserList(CmsUserBo bo);

    /**
     * 修改微信用户
     */
    Boolean updateUser(CmsUserBo bo);

    /**
     * 批量删除微信用户信息
     */
    Boolean deleteUserByIds(Collection<Long> ids);
}
