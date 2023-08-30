package com.ruoyi.cms.service;

import com.ruoyi.cms.domain.vo.CmsGuestVo;
import com.ruoyi.cms.domain.bo.CmsGuestBo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;
import java.util.List;

/**
 * 酒店旅客Service接口
 *
 * @author ruoyi
 * @date 2023-08-03
 */
public interface ICmsGuestService {

    /**
     * 查询酒店旅客
     */
    CmsGuestVo queryById(String cardId);

    /**
     * 查询酒店旅客列表
     */
    TableDataInfo<CmsGuestVo> queryPageList(CmsGuestBo bo, PageQuery pageQuery);

    /**
     * 查询酒店旅客列表
     */
    List<CmsGuestVo> queryList(CmsGuestBo bo);

    /**
     * 新增酒店旅客
     */
    Boolean insertByBo(CmsGuestBo bo);

    /**
     * 修改酒店旅客
     */
    Boolean updateByBo(CmsGuestBo bo);
}
