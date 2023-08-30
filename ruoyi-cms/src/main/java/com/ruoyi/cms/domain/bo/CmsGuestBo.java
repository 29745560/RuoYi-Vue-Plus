package com.ruoyi.cms.domain.bo;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 酒店旅客业务对象 biz_guest
 *
 * @author ruoyi
 * @date 2023-08-03
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CmsGuestBo extends BaseEntity {

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
     * 用户状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
}
