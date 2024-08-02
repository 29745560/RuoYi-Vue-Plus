package com.ruoyi.cms.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 房间类型对象 biz_room_category
 *
 * @author ruoyi
 * @date 2024-01-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_room_category")
public class CmsRoomCategory extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 客房类型编号
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 客房类型名称
     */
    private String name;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 床位数
     */
    private Integer beds;

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
