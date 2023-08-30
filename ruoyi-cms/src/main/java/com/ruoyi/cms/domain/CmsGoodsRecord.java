package com.ruoyi.cms.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

/**
 * 房间换洗记录对象 biz_goods_record
 *
 * @author ruoyi
 * @date 2023-08-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_goods_record")
public class CmsGoodsRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 房间编号
     */
    private Long roomId;

    /**
     * 入住编号
     */
    private Long roomRecordId;

    /**
     * 物品编号
     */
    private String goodsId;

    /**
     * 开始日期
     */
    private Date startDate;

    /**
     * 结束日期
     */
    private Date endDate;

    /**
     * 换洗状态（0初始 1更换）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
}
