package com.ruoyi.cms.domain.bo;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 房间类型业务对象 biz_room_category
 *
 * @author ruoyi
 * @date 2024-01-29
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CmsRoomCategoryBo extends BaseEntity {

    /**
     * 客房类型编号
     */
    @NotNull(message = "客房类型编号不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 客房类型名称
     */
    @NotBlank(message = "客房类型名称不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(min = 0, max = 20, message = "客房类型名称长度不能超过20个字符")
    private String name;

    /**
     * 价格
     */
    @DecimalMin(value = "1.00", message = "客房价格必须大于等于1元", groups = {AddGroup.class, EditGroup.class})
    private BigDecimal price;

    /**
     * 床位数
     */
    @Max(value = 4, message = "床位数最多4个", groups = {AddGroup.class, EditGroup.class})
    @Min(value = 1, message = "床位数最少1个", groups = {AddGroup.class, EditGroup.class})
    private Integer beds;

    /**
     * 备注
     */
    private String remark;
}
