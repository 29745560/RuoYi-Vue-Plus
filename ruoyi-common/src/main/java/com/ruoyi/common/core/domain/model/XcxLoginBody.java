package com.ruoyi.common.core.domain.model;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 小程序登录对象
 *
 * @author Lion Li
 */

@Data
public class XcxLoginBody {

    /**
     * 微信登录凭证
     */
    @NotBlank(message = "微信登录凭证不能为空")
    private String loginCode;

    /**
     * 手机号获取凭证
     */
    @NotBlank(message = "手机号获取凭证不能为空")
    private String phoneCode;
}
