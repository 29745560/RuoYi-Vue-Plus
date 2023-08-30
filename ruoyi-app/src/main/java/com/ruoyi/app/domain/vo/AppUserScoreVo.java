package com.ruoyi.app.domain.vo;

import lombok.Data;

/**
 * 微信用户积分视图对象 biz_guest_score
 *
 * @author ruoyi
 * @date 2023-08-16
 */
@Data
public class AppUserScoreVo {
    private static final long serialVersionUID = 1L;

    /**
     * 总计积分
     */
    private String scoreTotal;

    /**
     * 可用积分
     */
    private String scoreUsable;
}
