package com.ruoyi.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取微信相关配置
 *
 * @author Lion Li
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatConfig {

    /**
     * 小程序ID
     */
    private String appId;
    /**
     * 小程序密钥
     */
    private String secret;
    /**
     * 获取接口调用凭据URL
     */
    private String accessTokenUrl;
    /**
     * 获取手机号URL
     */
    private String userPhoneNumberUrl;
    /**
     * 小程序登录URL
     */
    private String jsCode2sessionUrl;
    /**
     * 获取小程序LinkUrl
     */
    private String genLinkUrl;
}
