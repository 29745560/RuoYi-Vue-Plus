package com.ruoyi.app.service;

public interface IAppWechatTokenService {

    /**
     * 获取接口调用凭据
     *
     * @return 凭证信息
     */
    public String getAccessToken();
}
