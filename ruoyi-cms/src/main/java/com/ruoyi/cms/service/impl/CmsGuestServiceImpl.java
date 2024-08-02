package com.ruoyi.cms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.cms.domain.bo.CmsGuestBo;
import com.ruoyi.cms.domain.vo.CmsGuestVo;
import com.ruoyi.cms.domain.CmsGuest;
import com.ruoyi.cms.mapper.CmsGuestMapper;
import com.ruoyi.cms.service.ICmsGuestService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

/**
 * 酒店旅客Service业务层处理
 *
 * @author ruoyi
 * @date 2023-08-03
 */
@RequiredArgsConstructor
@Service
public class CmsGuestServiceImpl implements ICmsGuestService {

    private final CmsGuestMapper guestMapper;

    /**
     * 分页查询酒店旅客列表
     */
    @Override
    public TableDataInfo<CmsGuestVo> queryPageList(CmsGuestBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<CmsGuest> lqw = buildQueryWrapper(bo);
        Page<CmsGuestVo> result = guestMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询酒店旅客列表
     */
    @Override
    public List<CmsGuestVo> queryList(CmsGuestBo bo) {
        LambdaQueryWrapper<CmsGuest> lqw = buildQueryWrapper(bo);
        return guestMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<CmsGuest> buildQueryWrapper(CmsGuestBo bo) {
        LambdaQueryWrapper<CmsGuest> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotBlank(bo.getRealname()), CmsGuest::getRealname, bo.getRealname());
        wrapper.eq(StringUtils.isNotBlank(bo.getCardId()), CmsGuest::getCardId, bo.getCardId());
        wrapper.eq(StringUtils.isNotBlank(bo.getContact()), CmsGuest::getContact, bo.getContact());
        wrapper.eq(StringUtils.isNotBlank(bo.getSex()), CmsGuest::getSex, bo.getSex());
        wrapper.eq(StringUtils.isNotBlank(bo.getStatus()), CmsGuest::getStatus, bo.getStatus());
        return wrapper;
    }

    /**
     * 查询酒店旅客
     */
    @Override
    public CmsGuestVo queryById(String cardId) {
        return guestMapper.selectVoById(cardId);
    }

    /**
     * 新增酒店旅客
     */
    @Override
    public Boolean insertByBo(CmsGuestBo bo) {
        CmsGuest addGuest = BeanUtil.toBean(bo, CmsGuest.class);
        return guestMapper.insert(addGuest) > 0;
    }

    /**
     * 修改酒店旅客
     */
    @Override
    public Boolean updateByBo(CmsGuestBo bo) {
        CmsGuest updateGuest = BeanUtil.toBean(bo, CmsGuest.class);
        return guestMapper.updateById(updateGuest) > 0;
    }
}
