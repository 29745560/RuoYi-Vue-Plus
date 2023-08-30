package com.ruoyi.cms.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.PageQuery;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.cms.domain.vo.CmsGuestVo;
import com.ruoyi.cms.domain.bo.CmsGuestBo;
import com.ruoyi.cms.service.ICmsGuestService;
import com.ruoyi.common.core.page.TableDataInfo;
import lombok.RequiredArgsConstructor;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 酒店旅客
 *
 * @author ruoyi
 * @date 2023-08-03
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/cms/guest")
public class CmsGuestController extends BaseController {

    private final ICmsGuestService guestService;

    /**
     * 查询酒店旅客列表
     */
    @SaCheckPermission("cms:guest:list")
    @GetMapping("/list")
    public TableDataInfo<CmsGuestVo> list(CmsGuestBo bo, PageQuery pageQuery) {
        return guestService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出酒店旅客列表
     */
    @SaCheckPermission("cms:guest:export")
    @Log(title = "酒店旅客", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(CmsGuestBo bo, HttpServletResponse response) {
        List<CmsGuestVo> list = guestService.queryList(bo);
        ExcelUtil.exportExcel(list, "酒店旅客", CmsGuestVo.class, response);
    }

    /**
     * 获取酒店旅客详细信息
     */
    @SaCheckPermission("cms:guest:query")
    @GetMapping("/{cardId}")
    public R<CmsGuestVo> getInfo(@NotNull(message = "旅客身份证不能为空") @PathVariable String cardId) {
        return R.ok(guestService.queryById(cardId));
    }

    /**
     * 新增酒店旅客
     */
    @SaCheckPermission("cms:guest:add")
    @Log(title = "酒店旅客", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody CmsGuestBo bo) {
        return toAjax(guestService.insertByBo(bo));
    }

    /**
     * 修改酒店旅客
     */
    @SaCheckPermission("cms:guest:edit")
    @Log(title = "酒店旅客", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody CmsGuestBo bo) {
        return toAjax(guestService.updateByBo(bo));
    }
}
