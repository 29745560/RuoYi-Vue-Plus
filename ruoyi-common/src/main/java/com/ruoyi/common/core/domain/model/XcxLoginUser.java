package com.ruoyi.common.core.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 小程序登录用户身份权限
 *
 * @author Lion Li
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class XcxLoginUser extends LoginUser {
    private static final long serialVersionUID = 1L;

    /**
     * 微信用户唯一标识
     */
    private String openId;

    /**
     * 微信开放平台唯一标识
     */
    private String unionId;

    /**
     * 会话密钥
     */
    private String sessionKey;

    /**
     * 联系方式
     */
    private String phonenumber;
}
