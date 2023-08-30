package com.ruoyi.app.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import com.ruoyi.app.domain.dto.WechatAccessTokenDTO;
import com.ruoyi.app.service.IAppWechatTokenService;
import com.ruoyi.common.config.WechatConfig;
import com.ruoyi.common.constant.WechatConstants;
import com.ruoyi.common.utils.JsonUtils;
import com.ruoyi.common.utils.redis.RedisUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class AppWechatTokenServiceImpl implements IAppWechatTokenService {

    /**
     * 微信相关配置
     */
    private final WechatConfig wechatConfig;

    /**
     * 获取接口调用凭据
     *
     * @return 凭证信息
     */
    @Override
    public String getAccessToken() {
        String token = null;
        Long exp = null;
        Boolean exist = RedisUtils.hasKey(WechatConstants.ACCESS_TOKEN_KEY);
        if (Boolean.TRUE.equals(exist)) {
            token = ObjectUtil.toString(RedisUtils.getCacheObject(WechatConstants.ACCESS_TOKEN_KEY));
        } else {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("appid", wechatConfig.getAppId());
            params.put("secret", wechatConfig.getSecret());
            params.put("grant_type", "client_credential");
            String response = HttpUtil.get(wechatConfig.getAccessTokenUrl(), params);
            log.info("微信小程序登录, 获取接口调用凭据, 参数===>{}, 返回值={}", params, response);
            if (StringUtils.isNotBlank(response)) {
                WechatAccessTokenDTO resDto = JsonUtils.parseObject(response, WechatAccessTokenDTO.class);
                if (ObjectUtil.isNotNull(resDto)) {
                    token = resDto.getAccess_token(); //获取到的凭证
                    exp = resDto.getExpires_in(); //凭证有效时间
                    if (exp >= WechatConstants.ACCESS_TOKEN_EXPIRES_MIN) {
                        exp = exp - WechatConstants.ACCESS_TOKEN_EXPIRES_DEFAULT; //使提前过期,提高系统稳定性
                    }
                    RedisUtils.setCacheObject(WechatConstants.ACCESS_TOKEN_KEY, token, Duration.ofSeconds(exp));
                }
            }
        }
        return token;
    }
}
