package com.ruoyi.app.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

/**
 * 微信用户对象 biz_user
 *
 * @author ruoyi
 * @date 2023-08-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_user")
public class AppUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 旅客编号
     */
    private String cardId;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 邮箱地址
     */
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
     * 微信用户唯一标识
     */
    private String openId;
    /**
     * 微信开放平台唯一标识
     */
    private String unionId;
    /**
     * 会话密钥
     */
    private String sessionKey;
    /**
     * 最后登录IP
     */
    private String loginIp;
    /**
     * 最后登录时间
     */
    private Date loginDate;
    /**
     * 备注
     */
    private String remark;
}
