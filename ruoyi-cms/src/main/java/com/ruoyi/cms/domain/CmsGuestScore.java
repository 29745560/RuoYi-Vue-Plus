package com.ruoyi.cms.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/**
 * 旅客积分对象 biz_guest_score
 *
 * @author ruoyi
 * @date 2023-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_guest_score")
public class CmsGuestScore extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 旅客编号
     */
    @TableId(value = "card_id", type = IdType.INPUT)
    private String cardId;

    /**
     * 总计积分
     */
    private BigDecimal scoreTotal;

    /**
     * 可用积分
     */
    private BigDecimal scoreUsable;

    /**
     * 账户状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
}
