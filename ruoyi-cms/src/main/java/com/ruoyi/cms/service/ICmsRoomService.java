package com.ruoyi.cms.service;

import com.ruoyi.cms.domain.vo.CmsRoomVo;
import com.ruoyi.cms.domain.bo.CmsRoomBo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 房间Service接口
 *
 * @author ruoyi
 * @date 2023-08-01
 */
public interface ICmsRoomService {

    /**
     * 查询房间
     */
    CmsRoomVo queryById(Long id);

    /**
     * 查询房间列表
     */
    TableDataInfo<CmsRoomVo> queryPageList(CmsRoomBo bo, PageQuery pageQuery);

    /**
     * 查询房间列表
     */
    List<CmsRoomVo> queryList(CmsRoomBo bo);

    /**
     * 新增房间
     */
    Boolean insertByBo(CmsRoomBo bo);

    /**
     * 修改房间
     */
    Boolean updateByBo(CmsRoomBo bo);

    /**
     * 宾客退房
     */
    Boolean checkout(Long id, String payStatus);

    /**
     * 校验并批量删除房间信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids);
}
