package com.ruoyi.cms.domain.bo;

import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.core.domain.BaseEntity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信用户业务对象 biz_user
 *
 * @author ruoyi
 * @date 2023-08-16
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CmsUserBo extends BaseEntity {

    /**
     * 用户编号
     */
    @NotNull(message = "用户编号不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 旅客编号
     */
    private String cardId;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 邮箱地址
     */
    @Email(message = "邮箱格式不正确", groups = {EditGroup.class})
    private String email;

    /**
     * 注册来源（0未知 1手机 2小程序）
     */
    private String source;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
}
