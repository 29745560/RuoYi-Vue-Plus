package com.ruoyi.cms.mapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.cms.domain.CmsRoomRecord;
import com.ruoyi.common.core.mapper.BaseMapperPlus;
import java.util.List;

/**
 * 房间入住记录Mapper接口
 *
 * @author ruoyi
 * @date 2023-08-02
 */
public interface CmsRoomRecordMapper extends BaseMapperPlus<CmsRoomRecordMapper, CmsRoomRecord, CmsRoomRecord> {

    Page<CmsRoomRecord> selectPageRoomRecordList(@Param("page") Page<CmsRoomRecord> page, @Param(Constants.WRAPPER) Wrapper<CmsRoomRecord> queryWrapper);

    List<CmsRoomRecord> selectRoomRecordList(@Param(Constants.WRAPPER) Wrapper<CmsRoomRecord> queryWrapper);

    CmsRoomRecord selectRoomRecordById(Long id);
}
