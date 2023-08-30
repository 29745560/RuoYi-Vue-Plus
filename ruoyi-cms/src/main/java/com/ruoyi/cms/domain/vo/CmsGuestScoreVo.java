package com.ruoyi.cms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.annotation.ExcelDictFormat;
import com.ruoyi.common.convert.ExcelDictConvert;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 旅客积分视图对象 biz_guest_score
 *
 * @author ruoyi
 * @date 2023-08-14
 */
@Data
@ExcelIgnoreUnannotated
public class CmsGuestScoreVo {
    private static final long serialVersionUID = 1L;

    /**
     * 旅客编号
     */
    @ExcelProperty(value = "旅客编号")
    private String cardId;

    /**
     * 总计积分
     */
    @ExcelProperty(value = "总计积分")
    private BigDecimal scoreTotal;

    /**
     * 可用积分
     */
    @ExcelProperty(value = "可用积分")
    private BigDecimal scoreUsable;

    /**
     * 账户状态（0正常 1停用）
     */
    @ExcelProperty(value = "账户状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
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
