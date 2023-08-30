package com.ruoyi.app.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;
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
public class AppRoomRecord extends BaseEntity {
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
}
