package com.ruoyi.cms.controller;

import java.util.List;
import java.util.Arrays;
import javax.servlet.http.HttpServletResponse;
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
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.cms.domain.vo.CmsGoodsVo;
import com.ruoyi.cms.domain.bo.CmsGoodsBo;
import com.ruoyi.cms.service.ICmsGoodsService;

/**
 * 物品
 *
 * @author ruoyi
 * @date 2023-08-04
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/cms/goods")
public class CmsGoodsController extends BaseController {

    private final ICmsGoodsService goodsService;

    /**
     * 查询物品列表
     */
    @SaCheckPermission("cms:goods:list")
    @GetMapping("/list")
    public TableDataInfo<CmsGoodsVo> list(CmsGoodsBo bo, PageQuery pageQuery) {
        return goodsService.selectPageGoodsList(bo, pageQuery);
    }

    /**
     * 导出物品列表
     */
    @SaCheckPermission("cms:goods:export")
    @Log(title = "物品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(CmsGoodsBo bo, HttpServletResponse response) {
        List<CmsGoodsVo> list = goodsService.selectGoodsList(bo);
        ExcelUtil.exportExcel(list, "物品", CmsGoodsVo.class, response);
    }

    /**
     * 获取物品详细信息
     */
    @SaCheckPermission("cms:goods:query")
    @GetMapping("/{id}")
    public R<CmsGoodsVo> getInfo(@NotNull(message = "物品编号不能为空") @PathVariable String id) {
        return R.ok(goodsService.selectGoodsById(id));
    }

    /**
     * 新增物品
     */
    @SaCheckPermission("cms:goods:add")
    @Log(title = "物品", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody CmsGoodsBo bo) {
        return toAjax(goodsService.insertGoods(bo));
    }

    /**
     * 修改物品
     */
    @SaCheckPermission("cms:goods:edit")
    @Log(title = "物品", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody CmsGoodsBo bo) {
        return toAjax(goodsService.updateGoods(bo));
    }

    /**
     * 删除物品
     */
    @SaCheckPermission("cms:goods:remove")
    @Log(title = "物品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "物品编号不能为空") @PathVariable String[] ids) {
        return toAjax(goodsService.deleteGoodsByIds(Arrays.asList(ids)));
    }

    /**
     * 添加物品与房间的关联
     */
    @SaCheckPermission("cms:goods:edit")
    @Log(title = "物品-房间", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PostMapping("/room/{roomId}/{ids}")
    public R<Void> addRoom(@NotNull(message = "房间编号不能为空") @PathVariable Long roomId,
                           @NotEmpty(message = "物品编号不能为空") @PathVariable String[] ids) {
        return toAjax(goodsService.updateGoodsRoomByIds(true, roomId, Arrays.asList(ids)));
    }

    /**
     * 删除物品与房间的关联
     */
    @SaCheckPermission("cms:goods:edit")
    @Log(title = "物品-房间", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @DeleteMapping("/room/{roomId}/{ids}")
    public R<Void> removeRoom(@NotNull(message = "房间编号不能为空") @PathVariable Long roomId,
                              @NotEmpty(message = "物品编号不能为空") @PathVariable String[] ids) {
        return toAjax(goodsService.updateGoodsRoomByIds(false, roomId, Arrays.asList(ids)));
    }
}
