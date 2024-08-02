package com.ruoyi.cms.controller;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
import com.ruoyi.cms.domain.bo.CmsRoomCategoryBo;
import com.ruoyi.cms.domain.vo.CmsRoomCategoryVo;
import com.ruoyi.cms.service.ICmsRoomCategoryService;
import lombok.RequiredArgsConstructor;

/**
 * 客房类型
 *
 * @author ruoyi
 * @date 2024-01-29
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/cms/room/category")
public class CmsRoomCategoryController extends BaseController {

    private final ICmsRoomCategoryService roomCategoryService;

    /**
     * 查询客房类型列表
     */
    @SaCheckPermission("cms:room.category:list")
    @GetMapping("/list")
    public R<List<CmsRoomCategoryVo>> list(CmsRoomCategoryBo bo) {
        return R.ok(roomCategoryService.selectRoomCategoryList(bo));
    }

    /**
     * 获取客房类型详细信息
     */
    @SaCheckPermission("cms:room.category:query")
    @GetMapping("/{id}")
    public R<CmsRoomCategoryVo> getInfo(@NotNull(message = "客房类型编号不能为空") @PathVariable Long id) {
        return R.ok(roomCategoryService.selectRoomCategoryById(id));
    }

    /**
     * 新增客房类型
     */
    @SaCheckPermission("cms:room.category:add")
    @Log(title = "客房类型", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody CmsRoomCategoryBo bo) {
        return toAjax(roomCategoryService.insertRoomCategory(bo));
    }

    /**
     * 修改客房类型
     */
    @SaCheckPermission("cms:room.category:edit")
    @Log(title = "客房类型", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody CmsRoomCategoryBo bo) {
        return toAjax(roomCategoryService.updateRoomCategory(bo));
    }

    /**
     * 删除客房类型
     */
    @SaCheckPermission("cms:room.category:remove")
    @Log(title = "客房类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "客房类型编号不能为空") @PathVariable Long[] ids) {
        return toAjax(roomCategoryService.deleteRoomCategoryByIds(Arrays.asList(ids)));
    }
}
