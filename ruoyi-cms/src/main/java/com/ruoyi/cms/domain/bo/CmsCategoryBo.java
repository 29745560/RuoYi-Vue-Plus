package com.ruoyi.cms.domain.bo;

import com.ruoyi.common.core.domain.TreeEntity;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 物品分类业务对象 biz_category
 *
 * @author ruoyi
 * @date 2023-08-01
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CmsCategoryBo extends TreeEntity<CmsCategoryBo> {

    /**
     * 分类编号
     */
    @NotNull(message = "分类编号不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 分类排序
     */
    private Long sort;

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(min = 0, max = 50, message = "分类名称不能超过50个字符")
    private String name;

    /**
     * 分类状态（0正常 1关闭）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
}
