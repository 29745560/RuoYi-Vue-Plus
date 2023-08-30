package com.ruoyi.app.domain.vo;

import com.ruoyi.common.annotation.Sensitive;
import com.ruoyi.common.enums.SensitiveStrategy;
import lombok.Data;

/**
 * 酒店旅客视图对象 biz_guest
 *
 * @author ruoyi
 * @date 2023-08-03
 */
@Data
public class AppGuestVo {
    private static final long serialVersionUID = 1L;

    /**
     * 身份证号
     */
    @Sensitive(strategy = SensitiveStrategy.ID_CARD)
    private String cardId;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 手机号码
     */
    @Sensitive(strategy = SensitiveStrategy.PHONE)
    private String phone;
}
