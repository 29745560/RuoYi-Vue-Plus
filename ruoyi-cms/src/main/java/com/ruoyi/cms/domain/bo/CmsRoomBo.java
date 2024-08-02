package com.ruoyi.cms.domain.bo;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 房间业务对象 biz_room
 *
 * @author ruoyi
 * @date 2023-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CmsRoomBo extends BaseEntity {

    /**
     * 房间编号
     */
    @NotNull(message = "房间编号不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 房间名称
     */
    @NotBlank(message = "房间名称不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(min = 0, max = 50, message = "房间名称不能超过50个字符")
    private String name;

    /**
     * 入住编号
     */
    private Long roomRecordId;

    /**
     * 类型编号
     */
    @NotNull(message = "房间类型编号不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long categoryId;

    /**
     * 房间状态（0空闲中 1整理中 2入住中 9已关闭）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
}
