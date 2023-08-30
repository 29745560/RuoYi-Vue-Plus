package com.ruoyi.cms.service;

import com.ruoyi.cms.domain.CmsRoomRecord;
import com.ruoyi.cms.domain.bo.CmsRoomRecordBo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;
import java.util.Collection;
import java.util.List;

/**
 * 房间入住记录Service接口
 *
 * @author ruoyi
 * @date 2023-08-02
 */
public interface ICmsRoomRecordService {

    /**
     * 查询房间入住记录
     */
    CmsRoomRecord selectRoomRecordById(Long id);

    /**
     * 查询房间入住记录列表
     */
    TableDataInfo<CmsRoomRecord> selectPageRoomRecordList(CmsRoomRecordBo bo, PageQuery pageQuery);

    /**
     * 查询房间入住记录列表
     */
    List<CmsRoomRecord> selectRoomRecordList(CmsRoomRecordBo bo);

    /**
     * 新增房间入住记录
     */
    Boolean insertRoomRecord(CmsRoomRecordBo bo);

    /**
     * 修改房间入住记录
     */
    Boolean updateRoomRecord(CmsRoomRecordBo bo);

    /**
     * 校验并批量删除房间入住记录信息
     */
    Boolean deleteRoomRecordByIds(Collection<Long> ids);
}
