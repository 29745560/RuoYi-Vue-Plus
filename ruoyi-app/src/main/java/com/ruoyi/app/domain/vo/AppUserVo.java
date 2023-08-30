package com.ruoyi.app.domain.vo;

import com.ruoyi.common.annotation.Sensitive;
import com.ruoyi.common.enums.SensitiveStrategy;
import lombok.Data;

/**
 * 微信用户视图对象 biz_user
 *
 * @author ruoyi
 * @date 2023-08-16
 */
@Data
public class AppUserVo {
    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    private Long id;
    /**
     * 旅客编号
     */
    @Sensitive(strategy = SensitiveStrategy.ID_CARD)
    private String cardId;
    /**
     * 手机号码
     */
    @Sensitive(strategy = SensitiveStrategy.PHONE)
    private String phone;
    /**
     * 邮箱地址
     */
    @Sensitive(strategy = SensitiveStrategy.EMAIL)
    private String email;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 头像地址
     */
    private String avatar;
    /**
     * 注册来源（0未知 1手机 2小程序）
     */
    private String source;
    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;

    /**
     * 用户积分
     */
    private AppUserScoreVo userScore;
}
