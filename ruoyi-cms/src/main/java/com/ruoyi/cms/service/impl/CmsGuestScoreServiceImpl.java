package com.ruoyi.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.PageQuery;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.cms.domain.bo.CmsGuestScoreBo;
import com.ruoyi.cms.domain.vo.CmsGuestScoreVo;
import com.ruoyi.cms.domain.CmsGuestScore;
import com.ruoyi.cms.mapper.CmsGuestScoreMapper;
import com.ruoyi.cms.service.ICmsGuestScoreService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

/**
 * 旅客积分Service业务层处理
 *
 * @author ruoyi
 * @date 2023-08-14
 */
@RequiredArgsConstructor
@Service
public class CmsGuestScoreServiceImpl implements ICmsGuestScoreService {

    private final CmsGuestScoreMapper guestScoreMapper;

    /**
     * 查询旅客积分
     */
    @Override
    public CmsGuestScoreVo queryGuestScoreById(String cardId) {
        return guestScoreMapper.selectVoById(cardId);
    }

    /**
     * 查询旅客积分列表
     */
    @Override
    public TableDataInfo<CmsGuestScoreVo> queryPageGuestScoreList(CmsGuestScoreBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<CmsGuestScore> wrapper = buildQueryWrapper(bo);
        Page<CmsGuestScoreVo> result = guestScoreMapper.selectVoPage(pageQuery.build(), wrapper);
        return TableDataInfo.build(result);
    }

    /**
     * 查询旅客积分列表
     */
    @Override
    public List<CmsGuestScoreVo> queryGuestScoreList(CmsGuestScoreBo bo) {
        LambdaQueryWrapper<CmsGuestScore> wrapper = buildQueryWrapper(bo);
        return guestScoreMapper.selectVoList(wrapper);
    }

    private LambdaQueryWrapper<CmsGuestScore> buildQueryWrapper(CmsGuestScoreBo bo) {
        LambdaQueryWrapper<CmsGuestScore> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtils.isNotBlank(bo.getCardId()), CmsGuestScore::getCardId, bo.getCardId());
        wrapper.eq(StringUtils.isNotBlank(bo.getStatus()), CmsGuestScore::getStatus, bo.getStatus());
        return wrapper;
    }
}
