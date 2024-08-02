package com.ruoyi.cms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.annotation.ExcelDictFormat;
import com.ruoyi.common.annotation.Translation;
import com.ruoyi.common.constant.TransConstant;
import com.ruoyi.common.convert.ExcelDictConvert;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 物品视图对象 biz_goods
 *
 * @author ruoyi
 * @date 2023-08-04
 */
@Data
@ExcelIgnoreUnannotated
public class CmsGoodsVo {
    private static final long serialVersionUID = 1L;

    /**
     * 物品编号
     */
    @ExcelProperty(value = "物品编号")
    private String id;

    /**
     * NFC秘文
     */
    @ExcelProperty(value = "NFC秘文")
    private String secret;

    /**
     * 物品名称
     */
    @ExcelProperty(value = "物品名称")
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
    @ExcelProperty(value = "分类名称")
    private String categoryName;

    /**
     * 房间编号
     */
    @ExcelProperty(value = "房间编号")
    private Long roomId;

    /**
     * 房间名称
     */
    @ExcelProperty(value = "房间名称")
    private String roomName;

    /**
     * 物品分值
     */
    @ExcelProperty(value = "物品分值")
    private BigDecimal score;

    /**
     * 物品类型（0普通 1环保）
     */
    @ExcelProperty(value = "物品类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=普通,1=环保")
    private String type;

    /**
     * 物品状态（0换洗中 1使用中）
     */
    @ExcelProperty(value = "物品状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=换洗中,1=使用中")
    private String status;

    /**
     * 创建者
     */
    @ExcelProperty(value = "创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新者
     */
    @ExcelProperty(value = "更新者")
    private String updateBy;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;
}
