package com.ruoyi.cms.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.PageQuery;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.cms.domain.vo.CmsGoodsRecordVo;
import com.ruoyi.cms.domain.bo.CmsGoodsRecordBo;
import com.ruoyi.cms.service.ICmsGoodsRecordService;

/**
 * 房间换洗记录
 *
 * @author ruoyi
 * @date 2023-08-09
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/cms/goods/record")
public class CmsGoodsRecordController extends BaseController {

    private final ICmsGoodsRecordService goodsRecordService;

    /**
     * 查询房间换洗记录列表
     */
    @SaCheckPermission("cms:goods.record:list")
    @GetMapping("/list")
    public TableDataInfo<CmsGoodsRecordVo> list(CmsGoodsRecordBo bo, PageQuery pageQuery) {
        return goodsRecordService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出房间换洗记录列表
     */
    @SaCheckPermission("cms:goods.record:export")
    @Log(title = "房间换洗记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(CmsGoodsRecordBo bo, HttpServletResponse response) {
        List<CmsGoodsRecordVo> list = goodsRecordService.queryList(bo);
        ExcelUtil.exportExcel(list, "房间换洗记录", CmsGoodsRecordVo.class, response);
    }

    /**
     * 获取房间换洗记录详细信息
     */
    @SaCheckPermission("cms:goods.record:query")
    @GetMapping("/{id}")
    public R<CmsGoodsRecordVo> getInfo(@NotNull(message = "入住编号不能为空") @PathVariable Long id) {
        return R.ok(goodsRecordService.queryById(id));
    }
}
