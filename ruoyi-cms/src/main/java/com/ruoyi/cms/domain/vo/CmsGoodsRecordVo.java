package com.ruoyi.cms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.ruoyi.common.annotation.ExcelDictFormat;
import com.ruoyi.common.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;

/**
 * 房间换洗记录视图对象 biz_goods_record
 *
 * @author ruoyi
 * @date 2023-08-09
 */
@Data
@ExcelIgnoreUnannotated
public class CmsGoodsRecordVo {
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    private Long id;

    /**
     * 房间编号
     */
    private Long roomId;

    /**
     * 房间名称
     */
    @ExcelProperty(value = "房间名称")
    private String roomName;

    /**
     * 入住编号
     */
    @ExcelProperty(value = "入住编号")
    private Long roomRecordId;

    /**
     * 物品编号
     */
    private String goodsId;

    /**
     * 物品名称
     */
    @ExcelProperty(value = "物品名称")
    private String goodsName;

    /**
     * 开始日期
     */
    @ExcelProperty(value = "开始日期")
    @DateTimeFormat(value = "yyyy-MM-dd")
    private Date startDate;

    /**
     * 结束日期
     */
    @ExcelProperty(value = "结束日期")
    @DateTimeFormat(value = "yyyy-MM-dd")
    private Date endDate;

    /**
     * 换洗状态（0初始 1更换）
     */
    @ExcelProperty(value = "换洗状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=初始,1=更换")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;
}
