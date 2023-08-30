package com.ruoyi.app.service;

import com.ruoyi.common.core.domain.model.SmsLoginBody;
import com.ruoyi.common.core.domain.model.XcxLoginBody;

public interface IAppUserLoginService {

    /**
     * 微信用户登录
     *
     * @param loginBody 登录凭证
     * @return 令牌
     */
    public String loginByWechat(XcxLoginBody loginBody);

    /**
     * 手机用户登录
     *
     * @param loginBody 登录信息
     * @return 令牌
     */
    public String loginBySMS(SmsLoginBody loginBody);

    /**
     * 退出登录
     */
    public void logout();
}
