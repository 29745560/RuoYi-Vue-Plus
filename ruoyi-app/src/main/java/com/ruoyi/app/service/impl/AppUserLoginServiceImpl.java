package com.ruoyi.app.service.impl;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.app.domain.AppUser;
import com.ruoyi.app.domain.dto.WechatCode2SessionDTO;
import com.ruoyi.app.mapper.AppUserMapper;
import com.ruoyi.app.service.IAppUserLoginService;
import com.ruoyi.app.service.IAppWechatTokenService;
import com.ruoyi.common.config.WechatConfig;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.domain.model.SmsLoginBody;
import com.ruoyi.common.core.domain.model.XcxLoginBody;
import com.ruoyi.common.core.domain.model.XcxLoginUser;
import com.ruoyi.common.enums.DeviceType;
import com.ruoyi.common.enums.UserType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.helper.LoginHelper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.JsonUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户登录Service业务层处理
 *
 * @author ruoyi
 * @date 2023-08-01
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class AppUserLoginServiceImpl implements IAppUserLoginService {

    private final WechatConfig wechatConfig;
    private final AppUserMapper appUserMapper;
    private final IAppWechatTokenService appWechatTokenService;

    /**
     * 微信用户登录
     *
     * @param loginBody 登录凭证
     * @return 令牌
     */
    @Override
    public String loginByWechat(XcxLoginBody loginBody) {
        String phoneCode = loginBody.getPhoneCode();
        String loginCode = loginBody.getLoginCode();
        String clientIp = ServletUtils.getClientIP(); // IP地址

        // 解析出手机号
        String phonenumber = getUserPhone(phoneCode);

        // 微信登录
        XcxLoginUser loginData = getWechatSession(loginCode);
        String openIdStr = loginData.getOpenId(); // 微信用户唯一标识
        String unionIdStr = loginData.getUnionId(); // 微信开放平台唯一标识
        String sessionKeyStr = loginData.getSessionKey(); // 会话密钥

        // 查询用户是否存在
        AppUser appUser = appUserMapper.selectOne(Wrappers.lambdaQuery(AppUser.class).eq(AppUser::getPhone, phonenumber));
        if (ObjectUtil.isNull(appUser)) {
            // 用户注册
            appUser = new AppUser();
            appUser.setPhone(phonenumber);
            appUser.setNickname(phonenumber);
            appUser.setSource(UserConstants.SOURCE_WECHAT);
            appUser.setOpenId(openIdStr);
            appUser.setUnionId(unionIdStr);
            appUser.setSessionKey(sessionKeyStr);
            appUser.setLoginIp(clientIp);
            appUser.setLoginDate(DateUtils.getNowDate());
            appUser.setCreateTime(DateUtils.getNowDate());
            appUser.setCreateBy("app_login_wechat_register");
            int userRegisterRes = appUserMapper.insert(appUser);
            log.info("微信登录, ***用户注册***, SQL={}", userRegisterRes);
            if (userRegisterRes < 1) throw new ServiceException("微信登录失败:用户信息注册失败,请重试");
        } else if (StringUtils.equals("1", appUser.getStatus())) {
            // 用户被封禁
            throw new ServiceException("微信登录失败:用户已停用,请联系客服", 10001);
        } else {
            // 更新用户登录信息
            int dbRes = appUserMapper.update(null,
                Wrappers.lambdaUpdate(AppUser.class)
                    .set(AppUser::getOpenId, openIdStr)
                    .set(AppUser::getUnionId, unionIdStr)
                    .set(AppUser::getSessionKey, sessionKeyStr)
                    .set(AppUser::getLoginIp, clientIp)
                    .set(AppUser::getLoginDate, DateUtils.getNowDate())
                    .set(AppUser::getUpdateTime, DateUtils.getNowDate())
                    .set(AppUser::getUpdateBy, "app_login_wechat_refresh")
                    .eq(AppUser::getId, appUser.getId())
            );
            log.info("微信登录, >>>用户刷新<<<, SQL={}", dbRes);
            if (dbRes < 1) throw new ServiceException("微信登录失败:用户刷新信息失败,请重试", 10001);
        }
        // 生成登录信息
        // 框架登录不限制从什么表查询 只要最终构建出 LoginUser 即可
        // 此处可根据登录用户的数据不同 自行创建 loginUser 属性不够用继承扩展就行了
        XcxLoginUser loginUser = new XcxLoginUser();
        loginUser.setUserId(appUser.getId());
        loginUser.setUserType(UserType.APP_USER.getUserType());
        loginUser.setUsername(appUser.getNickname());
        loginUser.setPhonenumber(phonenumber);
        loginUser.setOpenId(openIdStr);
        loginUser.setUnionId(unionIdStr);
        loginUser.setSessionKey(sessionKeyStr);

        // 生成token
        LoginHelper.loginByDevice(loginUser, DeviceType.XCX);
        return StpUtil.getTokenValue();
    }

    /**
     * 手机用户登录
     *
     * @param loginBody 登录信息
     * @return 令牌
     */
    @Override
    public String loginBySMS(SmsLoginBody loginBody) {
        String phonenumber = loginBody.getPhonenumber();
        String code = loginBody.getSmsCode();
        if (!StringUtils.endsWithIgnoreCase("8888", code)) {
            throw new ServiceException("验证码不正确");
        }
        String clientIp = ServletUtils.getClientIP(); // IP地址
        log.info("手机用户登录, phone={}, code={}, ip={}", phonenumber, code, clientIp);
        // 查询用户是否存在
        AppUser appUser = appUserMapper.selectOne(Wrappers.lambdaQuery(AppUser.class).eq(AppUser::getPhone, phonenumber));
        if (ObjectUtil.isNull(appUser)) {
            // 用户注册
            appUser = new AppUser();
            appUser.setPhone(phonenumber);
            appUser.setNickname(phonenumber);
            appUser.setSource(UserConstants.SOURCE_H5);
            appUser.setLoginIp(clientIp);
            appUser.setLoginDate(DateUtils.getNowDate());
            appUser.setCreateTime(DateUtils.getNowDate());
            appUser.setCreateBy("app_login_phone_register");
            int userRegisterRows = appUserMapper.insert(appUser);
            log.info("手机用户登录, ***注册***, SQL={}", userRegisterRows);
            if (userRegisterRows < 1) throw new ServiceException("用户信息注册失败,请重试");
        } else if (StringUtils.equals("1", appUser.getStatus())) {
            // 用户被封禁
            throw new ServiceException("手机用户登录失败:用户已停用,请联系客服", 10001);
        } else {
            // 更新用户登录信息
            int userUpdateRows = appUserMapper.update(null,
                Wrappers.lambdaUpdate(AppUser.class)
                    .set(AppUser::getLoginIp, clientIp)
                    .set(AppUser::getLoginDate, DateUtils.getNowDate())
                    .set(AppUser::getUpdateTime, DateUtils.getNowDate())
                    .set(AppUser::getUpdateBy, "app_login_phone_refresh")
                    .eq(AppUser::getId, appUser.getId())
            );
            log.info("手机用户登录, >>>刷新<<<, SQL={}", userUpdateRows);
            if (userUpdateRows < 1) throw new ServiceException("用户刷新信息失败,请重试", 10001);
        }
        // 此处可根据登录用户的数据不同 自行创建 loginUser 属性不够用继承扩展就行了
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(appUser.getId());
        loginUser.setUserType(UserType.APP_USER.getUserType());
        loginUser.setUsername(appUser.getNickname());

        // 生成token
        LoginHelper.loginByDevice(loginUser, DeviceType.APP);
        return StpUtil.getTokenValue();
    }

    /**
     * 登录凭证校验
     *
     * @param loginCode 微信登录凭证
     * @return 手机号
     */
    private XcxLoginUser getWechatSession(String loginCode) {
        // 微信登录凭证校验
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("appid", wechatConfig.getAppId());
        params.put("secret", wechatConfig.getSecret());
        params.put("js_code", loginCode);
        params.put("grant_type", "authorization_code");
        String url = wechatConfig.getJsCode2sessionUrl();
        String response = HttpUtil.get(url, params);
        log.info("微信小程序登录, 登录凭证校验, 参数===>{}, 返回值={}", params, response);
        if (StringUtils.isBlank(response)) {
            throw new ServiceException("微信登录凭证校验失败:无返回数据");
        }
        WechatCode2SessionDTO resDto = JsonUtils.parseObject(response, WechatCode2SessionDTO.class);
        if (ObjectUtil.isNull(resDto)) {
            throw new ServiceException("微信登录凭证校验失败:数据解析失败");
        }
        Integer errCode = resDto.getErrcode();
        if (ObjectUtil.isNull(errCode) || errCode.equals(0)) {
            String openIdStr = resDto.getOpenid(); // 微信用户唯一标识
            String unionIdStr = resDto.getUnionid(); // 微信开放平台唯一标识
            String sessionKeyStr = resDto.getSession_key(); // 会话密钥
            log.info("微信小程序登录, 获取凭证成功, OpenId={}, UnionId={}, SessionKey={}", openIdStr, unionIdStr, sessionKeyStr);
            XcxLoginUser loginUser = new XcxLoginUser();
            loginUser.setOpenId(StringUtils.defaultString(openIdStr));
            loginUser.setUnionId(StringUtils.defaultString(unionIdStr));
            loginUser.setSessionKey(StringUtils.defaultString(sessionKeyStr));
            return loginUser; // 返回登录用户信息,给token使用
        } else {
            if (errCode.equals(-1)) {
                throw new ServiceException("系统繁忙");
            } else if (errCode.equals(40029)) {
                throw new ServiceException("Code无效");
            } else if (errCode.equals(40226)) {
                throw new ServiceException("API调用太频繁");
            } else if (errCode.equals(45011)) {
                throw new ServiceException("高风险等级用户");
            } else {
                String err_msg = resDto.getErrmsg();
                String def_err_msg = "微信登录凭证校验失败" + String.valueOf(errCode);
                throw new ServiceException(StringUtils.blankToDefault(err_msg, def_err_msg));
            }
        }
    }

    /**
     * 获取微信用户手机
     *
     * @param phoneCode 手机号获取凭证
     * @return 手机号
     */
    private String getUserPhone(String phoneCode) {
        // 先获取接口调用凭证
        String accessToken = appWechatTokenService.getAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            throw new ServiceException("微信获取接口调用凭据失败,请重试或联系客服");
        }
        // 获取手机号
        String url = wechatConfig.getUserPhoneNumberUrl() + "?access_token=" + accessToken;
        String params = "{\"code\":\"" + phoneCode + "\"}";
        String response = HttpUtil.post(url, params);
        log.info("微信获取用户手机号, url={} 参数===>{}, 返回值={}", url, params, response);
        if (StringUtils.isBlank(response)) {
            throw new ServiceException("获取手机号失败:无返回数据");
        }
        JSONObject resObj = JsonUtils.parseObject(response, JSONObject.class);
        if (ObjectUtil.isNull(resObj)) {
            throw new ServiceException("获取手机号失败:数据解析失败");
        }
        Integer errCode = resObj.getInt("errcode");
        if (ObjectUtil.isNull(errCode) || errCode.equals(0)) {
            JSONObject infoObj = resObj.getJSONObject("phone_info"); // 用户手机号信息
            // 核查水印
            if (infoObj.containsKey("watermark")) {
                JSONObject watermarkObj = infoObj.getJSONObject("watermark");
                checkWatermark(watermarkObj.getStr("appid"), watermarkObj.getInt("timestamp"));
            }
            // 更新用户手机号
            return infoObj.getStr("phoneNumber");
        } else {
            if (errCode.equals(-1)) {
                throw new ServiceException("系统繁忙");
            } else if (errCode.equals(40029)) {
                throw new ServiceException("js_code无效");
            } else {
                String err_msg = resObj.getStr("errmsg");
                String def_err_msg = "获取手机号失败:" + String.valueOf(errCode);
                throw new ServiceException(StringUtils.blankToDefault(err_msg, def_err_msg));
            }
        }
    }

    /**
     * 检查水印的正确性
     */
    private void checkWatermark(String appId, Integer timestamp) {
        if (StringUtils.isNotBlank(appId) && ObjectUtil.isNotNull(timestamp)) {
            if (!StringUtils.equalsIgnoreCase(wechatConfig.getAppId(), appId)) {
                throw new ServiceException("小程序校验失败");
            }
            int watermarkTimestamp = Integer.parseInt(String.valueOf(timestamp.intValue()));
            int currentTimestamp = (int) (System.currentTimeMillis() / 1000);
            if (Math.abs(currentTimestamp - watermarkTimestamp) > 120) {
                throw new ServiceException("水印时间不正确");
            }
        }
    }

    /**
     * 退出登录
     */
    @Override
    public void logout() {
        try {
            StpUtil.logout();
        } catch (NotLoginException ignored) {
        }
    }
}
