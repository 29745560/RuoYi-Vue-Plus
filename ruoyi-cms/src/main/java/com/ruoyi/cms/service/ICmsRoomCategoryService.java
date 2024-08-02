package com.ruoyi.cms.service;

import com.ruoyi.cms.domain.bo.CmsRoomCategoryBo;
import com.ruoyi.cms.domain.vo.CmsRoomCategoryVo;
import java.util.Collection;
import java.util.List;

/**
 * 客房类型Service接口
 *
 * @author ruoyi
 * @date 2024-01-29
 */
public interface ICmsRoomCategoryService {

    /**
     * 查询客房类型
     */
    public CmsRoomCategoryVo selectRoomCategoryById(Long id);

    /**
     * 查询客房类型列表
     */
    public List<CmsRoomCategoryVo> selectRoomCategoryList(CmsRoomCategoryBo bo);

    /**
     * 新增客房类型
     */
    public Boolean insertRoomCategory(CmsRoomCategoryBo bo);

    /**
     * 修改客房类型
     */
    public Boolean updateRoomCategory(CmsRoomCategoryBo bo);

    /**
     * 批量删除客房类型
     */
    public Boolean deleteRoomCategoryByIds(Collection<Long> ids);
}
