package com.ruoyi.cms.service;

import com.ruoyi.cms.domain.bo.CmsGuestScoreBo;
import com.ruoyi.cms.domain.vo.CmsGuestScoreVo;
import com.ruoyi.common.core.domain.PageQuery;
import com.ruoyi.common.core.page.TableDataInfo;
import java.util.List;

/**
 * 旅客积分Service接口
 *
 * @author ruoyi
 * @date 2023-08-14
 */
public interface ICmsGuestScoreService {

    /**
     * 查询旅客积分
     */
    CmsGuestScoreVo queryGuestScoreById(String cardId);

    /**
     * 查询旅客积分列表
     */
    TableDataInfo<CmsGuestScoreVo> queryPageGuestScoreList(CmsGuestScoreBo bo, PageQuery pageQuery);

    /**
     * 查询旅客积分列表
     */
    List<CmsGuestScoreVo> queryGuestScoreList(CmsGuestScoreBo bo);
}
