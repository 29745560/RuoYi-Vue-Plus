package com.ruoyi.cms.domain.bo;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 物品业务对象 biz_goods
 *
 * @author ruoyi
 * @date 2023-08-04
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CmsGoodsBo extends BaseEntity {

    /**
     * 物品编号
     */
    @NotBlank(message = "物品编号不能为空", groups = {AddGroup.class})
    private String id;

    /**
     * 物品名称
     */
    @NotBlank(message = "物品名称不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(min = 0, max = 50, message = "物品名称不能超过50个字符")
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
     * NFC秘文
     */
    private String secret;

    /**
     * 物品分值
     */
    private BigDecimal score;

    /**
     * 物品类型（0普通 1环保）
     */
    private String type;

    /**
     * 物品状态（0换洗中 1使用中）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
}
