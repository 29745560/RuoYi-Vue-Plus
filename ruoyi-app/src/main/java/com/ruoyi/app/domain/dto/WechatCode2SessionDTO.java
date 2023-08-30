package com.ruoyi.app.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 微信登录凭证校验
 *
 * @author Lion Li
 */
@Data
@NoArgsConstructor
public class WechatCode2SessionDTO implements Serializable {

    /**
     * 用户唯一标识
     */
    private String openid;

    /**
     * 会话密钥
     */
    private String session_key;

    /**
     * 用户在开放平台的唯一标识符
     */
    private String unionid;

    /**
     * 错误码
     */
    private Integer errcode;

    /**
     * 错误信息
     */
    private String errmsg;
}
