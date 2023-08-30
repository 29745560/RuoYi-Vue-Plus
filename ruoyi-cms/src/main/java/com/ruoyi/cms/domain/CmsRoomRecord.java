package com.ruoyi.cms.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 房间入住记录对象 biz_room_record
 *
 * @author ruoyi
 * @date 2023-08-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_room_record")
public class CmsRoomRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 入住编号
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 房间编号
     */
    private Long roomId;

    /**
     * 身份证号
     */
    private String cardId;

    /**
     * 入住日期
     */
    private Date checkInDate;

    /**
     * 离店日期
     */
    private Date checkOutDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 房间信息
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @TableField(exist = false)
    private CmsRoom room;

    /**
     * 旅客信息
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @TableField(exist = false)
    private CmsGuest guest;
}
