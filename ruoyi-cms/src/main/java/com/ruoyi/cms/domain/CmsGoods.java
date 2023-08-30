package com.ruoyi.cms.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.BaseEntity;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 物品对象 biz_goods
 *
 * @author ruoyi
 * @date 2023-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_goods")
public class CmsGoods extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 物品编号
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * NFC秘文
     */
    private String secret;

    /**
     * 物品名称
     */
    private String name;

    /**
     * 物品图片
     */
    private String cover;

    /**
     * 物品分类
     */
    private Long categoryId;

    /**
     * 房间编号
     */
    private Long roomId;

    /**
     * 物品分值
     */
    private BigDecimal score;

    /**
     * 物品状态（0换洗中 1使用中）
     */
    private String status;

    /**
     * 删除标志（0正常 2删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 备注
     */
    private String remark;
}
