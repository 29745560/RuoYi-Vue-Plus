package com.ruoyi.cms.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 房间对象 biz_room
 *
 * @author ruoyi
 * @date 2023-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_room")
public class CmsRoom extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 房间编号
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 房间名称
     */
    private String name;

    /**
     * 入住编号
     */
    private Long roomRecordId;

    /**
     * 房间状态（0空闲中 1整理中 2入住中 9已关闭）
     */
    private String status;

    /**
     * 删除标志（0正常 2删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 备注
     */
    private String remark;
}
