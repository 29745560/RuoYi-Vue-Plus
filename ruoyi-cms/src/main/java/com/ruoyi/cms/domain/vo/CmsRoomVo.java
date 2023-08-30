package com.ruoyi.cms.domain.vo;

import java.util.Date;
import lombok.Data;

/**
 * 房间视图对象 biz_room
 *
 * @author ruoyi
 * @date 2023-08-01
 */
@Data
public class CmsRoomVo {
    private static final long serialVersionUID = 1L;

    /**
     * 房间编号
     */
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
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
