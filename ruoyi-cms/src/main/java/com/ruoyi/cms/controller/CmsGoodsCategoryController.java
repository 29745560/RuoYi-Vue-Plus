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
import com.ruoyi.cms.domain.bo.CmsGoodsCategoryBo;
import com.ruoyi.cms.domain.vo.CmsGoodsCategoryVo;
import com.ruoyi.cms.service.ICmsGoodsCategoryService;

/**
 * 物品分类
 *
 * @author ruoyi
 * @date 2023-08-01
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/cms/goods/category")
public class CmsGoodsCategoryController extends BaseController {

    private final ICmsGoodsCategoryService goodsCategoryService;

    /**
     * 查询物品分类列表
     */
    @SaCheckPermission("cms:goods.category:list")
    @GetMapping("/list")
    public R<List<CmsGoodsCategoryVo>> list(CmsGoodsCategoryBo bo) {
        List<CmsGoodsCategoryVo> list = goodsCategoryService.queryList(bo);
        return R.ok(list);
    }

    /**
     * 获取物品分类详细信息
     */
    @SaCheckPermission("cms:goods.category:query")
    @GetMapping("/{id}")
    public R<CmsGoodsCategoryVo> getInfo(@NotNull(message = "分类编号不能为空") @PathVariable Long id) {
        return R.ok(goodsCategoryService.queryById(id));
    }

    /**
     * 新增物品分类
     */
    @SaCheckPermission("cms:goods.category:add")
    @Log(title = "物品分类", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody CmsGoodsCategoryBo bo) {
        return toAjax(goodsCategoryService.insertByBo(bo));
    }

    /**
     * 修改物品分类
     */
    @SaCheckPermission("cms:goods.category:edit")
    @Log(title = "物品分类", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody CmsGoodsCategoryBo bo) {
        return toAjax(goodsCategoryService.updateByBo(bo));
    }

    /**
     * 删除物品分类
     */
    @SaCheckPermission("cms:goods.category:remove")
    @Log(title = "物品分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "分类编号不能为空") @PathVariable Long[] ids) {
        return toAjax(goodsCategoryService.deleteWithValidByIds(Arrays.asList(ids)));
    }
}
