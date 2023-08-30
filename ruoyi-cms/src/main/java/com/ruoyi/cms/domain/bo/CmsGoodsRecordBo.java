package com.ruoyi.cms.domain.bo;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 房间换洗记录业务对象 biz_goods_record
 *
 * @author ruoyi
 * @date 2023-08-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CmsGoodsRecordBo extends BaseEntity {

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
     * 换洗状态（0初始 1更换）
     */
    private String status;
}
