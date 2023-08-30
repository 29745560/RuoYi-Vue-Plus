package com.ruoyi.cms.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.PageQuery;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.cms.domain.vo.CmsUserVo;
import com.ruoyi.cms.domain.bo.CmsUserBo;
import com.ruoyi.cms.service.ICmsUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 微信用户
 *
 * @author ruoyi
 * @date 2023-08-16
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/cms/user")
public class CmsUserController extends BaseController {

    private final ICmsUserService userService;

    /**
     * 查询微信用户列表
     */
    @SaCheckPermission("cms:user:list")
    @GetMapping("/list")
    public TableDataInfo<CmsUserVo> list(CmsUserBo bo, PageQuery pageQuery) {
        return userService.selectUserPageList(bo, pageQuery);
    }

    /**
     * 导出微信用户列表
     */
    @SaCheckPermission("cms:user:export")
    @Log(title = "微信用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(CmsUserBo bo, HttpServletResponse response) {
        List<CmsUserVo> list = userService.selectUserList(bo);
        ExcelUtil.exportExcel(list, "微信用户", CmsUserVo.class, response);
    }

    /**
     * 获取微信用户详细信息
     */
    @SaCheckPermission("cms:user:query")
    @GetMapping("/{id}")
    public R<CmsUserVo> getInfo(@NotNull(message = "用户编号不能为空") @PathVariable Long id) {
        return R.ok(userService.selectUserById(id));
    }

    /**
     * 修改微信用户
     */
    @SaCheckPermission("cms:user:edit")
    @Log(title = "微信用户", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody CmsUserBo bo) {
        return toAjax(userService.updateUser(bo));
    }

    /**
     * 删除微信用户
     */
    @SaCheckPermission("cms:user:remove")
    @Log(title = "微信用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "用户编号不能为空") @PathVariable Long[] ids) {
        return toAjax(userService.deleteUserByIds(Arrays.asList(ids)));
    }
}
