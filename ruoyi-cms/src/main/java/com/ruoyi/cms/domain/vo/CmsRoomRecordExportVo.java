package com.ruoyi.cms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.ruoyi.common.annotation.ExcelDictFormat;
import com.ruoyi.common.convert.ExcelDictConvert;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 房间入住记录视图对象 biz_room_record
 *
 * @author ruoyi
 * @date 2023-08-02
 */
@Data
@NoArgsConstructor
@ExcelIgnoreUnannotated
public class CmsRoomRecordExportVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 入住编号
     */
    @ExcelProperty(value = "入住编号")
    private Long id;

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
     * 身份证号
     */
    @ExcelProperty(value = "身份证号")
    private String cardId;

    /**
     * 宾客姓名
     */
    @ExcelProperty(value = "宾客姓名")
    private String realname;

    /**
     * 联系方式
     */
    @ExcelProperty(value = "联系方式")
    private String contact;

    /**
     * 宾客性别（0男 1女 2未知）
     */
    @ExcelProperty(value = "宾客性别", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /**
     * 入住日期
     */
    @ExcelProperty(value = "入住日期")
    @DateTimeFormat(value = "yyyy-MM-dd")
    private Date checkInDate;

    /**
     * 离店日期
     */
    @ExcelProperty(value = "离店日期")
    @DateTimeFormat(value = "yyyy-MM-dd")
    private Date checkOutDate;

    /**
     * 房间单价
     */
    @ExcelProperty(value = "房间单价")
    private BigDecimal unitPrice;

    /**
     * 消费金额
     */
    @ExcelProperty(value = "消费金额")
    private BigDecimal totalAmount;

    /**
     * 支付状态（0未支付 1已支付）
     */
    @ExcelProperty(value = "支付状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=未支付,1=已支付")
    private String pay;

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
