package com.ruoyi.cms.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.TreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 物品分类对象 biz_goods_category
 *
 * @author ruoyi
 * @date 2023-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_goods_category")
public class CmsGoodsCategory extends TreeEntity<CmsGoodsCategory> {

    private static final long serialVersionUID = 1L;

    /**
     * 分类编号
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 分类排序
     */
    private Long sort;

    /**
     * 分类名称
     */
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
