package com.ruoyi.cms.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 房间入住记录视图对象 biz_room_category
 *
 * @author ruoyi
 * @date 2024-01-29
 */
@Data
@NoArgsConstructor
public class CmsRoomCategoryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 客房类型编号
     */
    private Long id;

    /**
     * 客房类型名称
     */
    private String name;

    /**
     * 客房价格
     */
    private BigDecimal price;

    /**
     * 床位数
     */
    private Integer beds;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
