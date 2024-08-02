package com.ruoyi.cms.controller;

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
import com.ruoyi.common.core.domain.PageQuery;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.cms.domain.bo.CmsRoomBo;
import com.ruoyi.cms.domain.vo.CmsRoomVo;
import com.ruoyi.cms.service.ICmsRoomService;

/**
 * 房间
 *
 * @author ruoyi
 * @date 2023-08-01
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/cms/room")
public class CmsRoomController extends BaseController {

    private final ICmsRoomService roomService;

    /**
     * 查询房间列表
     */
    @SaCheckPermission("cms:room:list")
    @GetMapping("/list")
    public TableDataInfo<CmsRoomVo> list(CmsRoomBo bo, PageQuery pageQuery) {
        return roomService.queryPageList(bo, pageQuery);
    }

    /**
     * 获取房间详细信息
     */
    @SaCheckPermission("cms:room:query")
    @GetMapping("/{id}")
    public R<CmsRoomVo> getInfo(@NotNull(message = "房间编号不能为空") @PathVariable Long id) {
        return R.ok(roomService.queryById(id));
    }

    /**
     * 新增房间
     */
    @SaCheckPermission("cms:room:add")
    @Log(title = "房间", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody CmsRoomBo bo) {
        return toAjax(roomService.insertByBo(bo));
    }

    /**
     * 修改房间
     */
    @SaCheckPermission("cms:room:edit")
    @Log(title = "房间", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody CmsRoomBo bo) {
        return toAjax(roomService.updateByBo(bo));
    }

    /**
     * 旅客退房
     */
    @SaCheckPermission("cms:room:edit")
    @Log(title = "房间", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/checkout/{id}/{payStatus}")
    public R<Void> checkout(@NotNull(message = "房间编号不能为空") @PathVariable Long id,
                            @NotNull(message = "支付状态不能为空") @PathVariable String payStatus) {
        return toAjax(roomService.checkout(id, payStatus));
    }

    /**
     * 删除房间
     */
    @SaCheckPermission("cms:room:remove")
    @Log(title = "房间", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "房间编号不能为空") @PathVariable Long[] ids) {
        return toAjax(roomService.deleteWithValidByIds(Arrays.asList(ids)));
    }
}
