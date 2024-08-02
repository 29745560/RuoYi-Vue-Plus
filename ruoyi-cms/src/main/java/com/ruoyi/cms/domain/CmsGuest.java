package com.ruoyi.cms.domain;

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
public class CmsGuest extends BaseEntity {
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
     * 联系方式
     */
    private String contact;

    /**
     * 宾客性别（0男 1女 2未知）
     */
    private String sex;

    /**
     * 宾客状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
}
