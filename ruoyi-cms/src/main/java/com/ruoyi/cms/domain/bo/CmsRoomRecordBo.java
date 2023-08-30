package com.ruoyi.cms.domain.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 房间入住记录业务对象 biz_room_record
 *
 * @author ruoyi
 * @date 2023-08-02
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CmsRoomRecordBo extends BaseEntity {

    /**
     * 入住编号
     */
    @NotNull(message = "入住编号不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 房间编号
     */
    @NotNull(message = "房间编号不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long roomId;

    /**
     * 身份证号
     */
    @NotBlank(message = "身份证号不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(min = 0, max = 18, message = "身份证号错误")
    private String cardId;

    /**
     * 真实姓名
     */
    @Size(min = 0, max = 50, message = "真实姓名长度不能超过50个字符")
    private String realname;

    /**
     * 手机号码
     */
    @Size(min = 0, max = 50, message = "手机号码长度不能超过50个字符")
    private String phone;

    /**
     * 入住日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "入住日期不能为空", groups = {AddGroup.class, EditGroup.class})
    private Date checkInDate;

    /**
     * 离店日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "离店日期不能为空", groups = {AddGroup.class, EditGroup.class})
    private Date checkOutDate;

    /**
     * 备注
     */
    private String remark;
}
