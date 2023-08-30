package com.ruoyi.cms.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.PageQuery;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.cms.domain.bo.CmsGuestScoreBo;
import com.ruoyi.cms.domain.vo.CmsGuestScoreVo;
import com.ruoyi.cms.service.ICmsGuestScoreService;
import lombok.RequiredArgsConstructor;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 旅客积分
 *
 * @author ruoyi
 * @date 2023-08-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/cms/guest/score")
public class CmsGuestScoreController extends BaseController {

    private final ICmsGuestScoreService guestScoreService;

    /**
     * 查询旅客积分列表
     */
    @SaCheckPermission("cms:guest.score:list")
    @GetMapping("/list")
    public TableDataInfo<CmsGuestScoreVo> list(CmsGuestScoreBo bo, PageQuery pageQuery) {
        return guestScoreService.queryPageGuestScoreList(bo, pageQuery);
    }

    /**
     * 导出旅客积分列表
     */
    @SaCheckPermission("cms:guest.score:export")
    @Log(title = "旅客积分", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(CmsGuestScoreBo bo, HttpServletResponse response) {
        List<CmsGuestScoreVo> list = guestScoreService.queryGuestScoreList(bo);
        ExcelUtil.exportExcel(list, "旅客积分", CmsGuestScoreVo.class, response);
    }

    /**
     * 获取旅客积分详细信息
     *
     * @param cardId 主键
     */
    @SaCheckPermission("cms:guest.score:query")
    @GetMapping("/{cardId}")
    public R<CmsGuestScoreVo> getInfo(@NotNull(message = "身份证号不能为空") @PathVariable String cardId) {
        return R.ok(guestScoreService.queryGuestScoreById(cardId));
    }
}
