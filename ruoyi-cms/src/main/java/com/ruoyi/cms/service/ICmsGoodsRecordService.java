package com.ruoyi.cms.service;

import com.ruoyi.cms.domain.vo.CmsGoodsRecordVo;
import com.ruoyi.cms.domain.bo.CmsGoodsRecordBo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;
import java.util.List;

/**
 * 房间换洗记录Service接口
 *
 * @author ruoyi
 * @date 2023-08-09
 */
public interface ICmsGoodsRecordService {

    /**
     * 查询房间换洗记录
     */
    CmsGoodsRecordVo queryById(Long id);

    /**
     * 查询房间换洗记录列表
     */
    TableDataInfo<CmsGoodsRecordVo> queryPageList(CmsGoodsRecordBo bo, PageQuery pageQuery);

    /**
     * 查询房间换洗记录列表
     */
    List<CmsGoodsRecordVo> queryList(CmsGoodsRecordBo bo);
}
