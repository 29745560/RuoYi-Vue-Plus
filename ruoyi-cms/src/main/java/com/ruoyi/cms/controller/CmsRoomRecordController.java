package com.ruoyi.cms.controller;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
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
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.cms.domain.CmsGuest;
import com.ruoyi.cms.domain.CmsRoom;
import com.ruoyi.cms.domain.CmsRoomRecord;
import com.ruoyi.cms.domain.bo.CmsRoomRecordBo;
import com.ruoyi.cms.domain.vo.CmsRoomRecordExportVo;
import com.ruoyi.cms.service.ICmsRoomRecordService;

/**
 * 房间入住记录
 *
 * @author ruoyi
 * @date 2023-08-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/cms/room/record")
public class CmsRoomRecordController extends BaseController {

    private final ICmsRoomRecordService roomRecordService;

    /**
     * 查询房间入住记录列表
     */
    @SaCheckPermission("cms:room.record:list")
    @GetMapping("/list")
    public TableDataInfo<CmsRoomRecord> list(CmsRoomRecordBo bo, PageQuery pageQuery) {
        return roomRecordService.selectPageRoomRecordList(bo, pageQuery);
    }

    /**
     * 导出房间入住记录列表
     */
    @SaCheckPermission("cms:room.record:export")
    @Log(title = "房间入住记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(CmsRoomRecordBo bo, HttpServletResponse response) {
        List<CmsRoomRecord> list = roomRecordService.selectRoomRecordList(bo);
        List<CmsRoomRecordExportVo> listVo = BeanUtil.copyToList(list, CmsRoomRecordExportVo.class);
        for (int i = 0; i < list.size(); i++) {
            CmsRoom room = list.get(i).getRoom();
            CmsGuest guest = list.get(i).getGuest();
            CmsRoomRecordExportVo vo = listVo.get(i);
            if (ObjectUtil.isNotEmpty(room)) {
                vo.setRoomName(room.getName());
                vo.setRealname(StringUtils.blankToDefault(guest.getRealname(), "未填写"));
                vo.setPhone(StringUtils.blankToDefault(guest.getPhone(), "未填写"));
            }
        }
        ExcelUtil.exportExcel(listVo, "房间入住记录", CmsRoomRecordExportVo.class, response);
    }

    /**
     * 获取房间入住记录详细信息
     */
    @SaCheckPermission("cms:room.record:query")
    @GetMapping("/{id}")
    public R<CmsRoomRecord> getInfo(@NotNull(message = "房间入住记录编号不能为空") @PathVariable Long id) {
        return R.ok(roomRecordService.selectRoomRecordById(id));
    }

    /**
     * 新增房间入住记录
     */
    @SaCheckPermission("cms:room.record:add")
    @Log(title = "房间入住记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody CmsRoomRecordBo bo) {
        return toAjax(roomRecordService.insertRoomRecord(bo));
    }

    /**
     * 修改房间入住记录
     */
    @SaCheckPermission("cms:room.record:edit")
    @Log(title = "房间入住记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody CmsRoomRecordBo bo) {
        return toAjax(roomRecordService.updateRoomRecord(bo));
    }

    /**
     * 删除房间入住记录
     */
    @SaCheckPermission("cms:room.record:remove")
    @Log(title = "房间入住记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "房间入住记录编号不能为空") @PathVariable Long[] ids) {
        return toAjax(roomRecordService.deleteRoomRecordByIds(Arrays.asList(ids)));
    }
}
