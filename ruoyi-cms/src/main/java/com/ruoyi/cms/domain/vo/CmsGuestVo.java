package com.ruoyi.cms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.annotation.ExcelDictFormat;
import com.ruoyi.common.convert.ExcelDictConvert;
import lombok.Data;

/**
 * 酒店旅客视图对象 biz_guest
 *
 * @author ruoyi
 * @date 2023-08-03
 */
@Data
@ExcelIgnoreUnannotated
public class CmsGuestVo {
    private static final long serialVersionUID = 1L;

    /**
     * 身份证号
     */
    @ExcelProperty(value = "身份证号")
    private String cardId;

    /**
     * 真实姓名
     */
    @ExcelProperty(value = "真实姓名")
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
     * 宾客状态（0正常 1停用）
     */
    @ExcelProperty(value = "宾客状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;
}
