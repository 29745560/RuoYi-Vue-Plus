package com.ruoyi.app.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 酒店旅客对象 biz_guest
 *
 * @author ruoyi
 * @date 2023-08-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_guest")
public class AppGuest extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 身份证号
     */
    @TableId(value = "card_id", type = IdType.INPUT)
    private String cardId;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 用户状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
}
