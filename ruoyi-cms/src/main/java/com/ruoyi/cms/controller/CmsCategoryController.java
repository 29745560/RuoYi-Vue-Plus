package com.ruoyi.cms.controller;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.cms.domain.vo.CmsCategoryVo;
import com.ruoyi.cms.domain.bo.CmsCategoryBo;
import com.ruoyi.cms.service.ICmsCategoryService;

/**
 * 物品分类
 *
 * @author ruoyi
 * @date 2023-08-01
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/cms/category")
public class CmsCategoryController extends BaseController {

    private final ICmsCategoryService categoryService;

    /**
     * 查询物品分类列表
     */
    @SaCheckPermission("cms:category:list")
    @GetMapping("/list")
    public R<List<CmsCategoryVo>> list(CmsCategoryBo bo) {
        List<CmsCategoryVo> list = categoryService.queryList(bo);
        return R.ok(list);
    }

    /**
     * 获取物品分类详细信息
     */
    @SaCheckPermission("cms:category:query")
    @GetMapping("/{id}")
    public R<CmsCategoryVo> getInfo(@NotNull(message = "分类编号不能为空") @PathVariable Long id) {
        return R.ok(categoryService.queryById(id));
    }

    /**
     * 新增物品分类
     */
    @SaCheckPermission("cms:category:add")
    @Log(title = "物品分类", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody CmsCategoryBo bo) {
        return toAjax(categoryService.insertByBo(bo));
    }

    /**
     * 修改物品分类
     */
    @SaCheckPermission("cms:category:edit")
    @Log(title = "物品分类", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody CmsCategoryBo bo) {
        return toAjax(categoryService.updateByBo(bo));
    }

    /**
     * 删除物品分类
     */
    @SaCheckPermission("cms:category:remove")
    @Log(title = "物品分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "分类编号不能为空") @PathVariable Long[] ids) {
        return toAjax(categoryService.deleteWithValidByIds(Arrays.asList(ids)));
    }
}
