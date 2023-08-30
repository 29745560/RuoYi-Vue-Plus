package com.ruoyi.app.domain.vo;

import com.ruoyi.common.annotation.Translation;
import com.ruoyi.common.constant.TransConstant;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 物品视图对象 biz_goods
 *
 * @author ruoyi
 * @date 2023-08-04
 */
@Data
public class AppGoodsVo {
    private static final long serialVersionUID = 1L;

    /**
     * 物品编号
     */
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
     * 物品图片_ossId
     */
    private String cover;

    /**
     * 物品图片_url
     */
    @Translation(type = TransConstant.OSS_ID_TO_URL, mapper = "cover")
    private String coverUrl;

    /**
     * 物品分类
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 物品分值
     */
    private BigDecimal score;

    /**
     * 物品状态（0换洗中 1使用中）
     */
    private String status;
}
