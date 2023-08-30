package com.ruoyi.cms.domain.vo;

import lombok.Data;
import java.util.Date;

/**
 * 物品分类视图对象 biz_category
 *
 * @author ruoyi
 * @date 2023-08-01
 */
@Data
public class CmsCategoryVo {
    private static final long serialVersionUID = 1L;

    /**
     * 分类编号
     */
    private Long id;

    /**
     * 分类父编号
     */
    private Long parentId;

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
