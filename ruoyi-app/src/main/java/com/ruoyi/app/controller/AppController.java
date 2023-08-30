package com.ruoyi.app.controller;

import com.ruoyi.app.domain.vo.AppGuestVo;
import com.ruoyi.app.domain.vo.AppRoomVo;
import com.ruoyi.app.domain.vo.AppUserVo;
import com.ruoyi.app.service.IAppService;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.helper.LoginHelper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import javax.validation.constraints.NotNull;

/**
 * 登录验证
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app")
public class AppController extends BaseController {

    private final IAppService appService;

    /**
     * 获取用户信息
     */
    @GetMapping("/user")
    public R<AppUserVo> getUserInfo() {
        LoginUser loginUser = LoginHelper.getLoginUser();
        return R.ok(appService.getUserInfo(loginUser.getUserId()));
    }

    /**
     * 获取入住信息
     */
    @GetMapping("/guest/{roomId}")
    public R<AppGuestVo> getGuestInfo(@NotNull(message = "房间号不能为空") @PathVariable Long roomId) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        return R.ok(appService.getGuestInfo(roomId, loginUser.getUserId()));
    }

    /**
     * 确认绑定身份证号
     */
    @RepeatSubmit()
    @PostMapping("/user/binding/{roomId}")
    public R<Void> bindingUserByCard(@NotNull(message = "房间号不能为空") @PathVariable Long roomId) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        return toAjax(appService.bindingUserByCard(roomId, loginUser.getUserId()));
    }

    /**
     * 获取房间信息
     */
    @GetMapping("/room/{roomId}")
    public R<AppRoomVo> getRoomInfo(@NotNull(message = "房间号不能为空") @PathVariable Long roomId) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        return R.ok(appService.getRoomInfo(roomId, loginUser.getUserId()));
    }
}
