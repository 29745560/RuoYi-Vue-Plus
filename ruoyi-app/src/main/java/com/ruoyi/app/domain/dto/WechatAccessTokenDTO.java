package com.ruoyi.app.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 微信获取接口调用凭据
 *
 * @author Lion Li
 */
@Data
@NoArgsConstructor
public class WechatAccessTokenDTO implements Serializable {

    /**
     * 获取到的凭证
     */
    private String access_token;

    /**
     * 凭证有效时间
     */
    private Long expires_in;
}
