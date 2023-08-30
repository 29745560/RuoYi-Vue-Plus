package com.ruoyi.cms.domain.bo;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.validate.QueryGroup;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 旅客积分业务对象 biz_guest_score
 *
 * @author ruoyi
 * @date 2023-08-14
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CmsGuestScoreBo extends BaseEntity {

    /**
     * 旅客编号
     */
    @NotBlank(message = "旅客编号不能为空", groups = {QueryGroup.class})
    private String cardId;

    /**
     * 账户状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
}
