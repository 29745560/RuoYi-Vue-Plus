package com.ruoyi.app.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.date.DateUtil;
import com.ruoyi.app.service.IAppUserLoginService;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.model.SmsLoginBody;
import com.ruoyi.common.core.domain.model.XcxLoginBody;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录验证
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/login")
public class AppLoginController extends BaseController {

    private final IAppUserLoginService loginService;

    /**
     * 小程序登录
     */
    @SaIgnore
    @PostMapping("/wechat")
    public R<Map<String, Object>> loginByWechat(@Validated @RequestBody XcxLoginBody loginBody) {
        Map<String, Object> ajax = new HashMap<>();
        // 生成令牌
        String token = loginService.loginByWechat(loginBody);
        ajax.put(Constants.TOKEN, token);
        ajax.put(Constants.EXPIRE, DateUtil.current() + 259200000);
        return R.ok(ajax);
    }

    /**
     * 短信登录
     */
    @SaIgnore
    @PostMapping("/sms")
    public R<Map<String, Object>> loginBySMS(@Validated @RequestBody SmsLoginBody loginBody) {
        Map<String, Object> ajax = new HashMap<>();
        // 生成令牌
        String token = loginService.loginBySMS(loginBody);
        ajax.put(Constants.TOKEN, token);
        ajax.put(Constants.EXPIRE, DateUtil.current() + 259200000);
        return R.ok(ajax);
    }

    /**
     * 退出登录
     */
    @SaIgnore
    @PostMapping("/logout")
    public R<Void> logout() {
        loginService.logout();
        return R.ok("退出成功");
    }
}
