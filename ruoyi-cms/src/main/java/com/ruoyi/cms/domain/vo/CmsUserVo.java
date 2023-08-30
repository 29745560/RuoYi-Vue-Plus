package com.ruoyi.cms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.annotation.ExcelDictFormat;
import com.ruoyi.common.annotation.Sensitive;
import com.ruoyi.common.convert.ExcelDictConvert;
import com.ruoyi.common.enums.SensitiveStrategy;
import lombok.Data;
import java.util.Date;

/**
 * 微信用户视图对象 biz_user
 *
 * @author ruoyi
 * @date 2023-08-16
 */
@Data
@ExcelIgnoreUnannotated
public class CmsUserVo {
    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    @ExcelProperty(value = "用户编号")
    private Long id;

    /**
     * 身份证号
     */
    @Sensitive(strategy = SensitiveStrategy.ID_CARD)
    @ExcelProperty(value = "身份证号")
    private String cardId;

    /**
     * 手机号码
     */
    @Sensitive(strategy = SensitiveStrategy.PHONE)
    @ExcelProperty(value = "手机号码")
    private String phone;

    /**
     * 邮箱地址
     */
    @Sensitive(strategy = SensitiveStrategy.EMAIL)
    @ExcelProperty(value = "邮箱地址")
    private String email;

    /**
     * 用户昵称
     */
    @ExcelProperty(value = "用户昵称")
    private String nickname;

    /**
     * 头像地址
     */
    @ExcelProperty(value = "头像地址")
    private String avatar;

    /**
     * 注册来源（0未知 1手机 2小程序）
     */
    @ExcelProperty(value = "注册来源", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=未知,1=手机,2=小程序")
    private String source;

    /**
     * 帐号状态（0正常 1停用）
     */
    @ExcelProperty(value = "帐号状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
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
    @ExcelProperty(value = "最后登录IP")
    private String loginIp;

    /**
     * 最后登录时间
     */
    @ExcelProperty(value = "最后登录时间")
    private Date loginDate;

    /**
     * 创建者
     */
    @ExcelProperty(value = "创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新者
     */
    @ExcelProperty(value = "更新者")
    private String updateBy;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;
}
