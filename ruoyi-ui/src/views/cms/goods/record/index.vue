<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="当前房间" prop="roomId">
        <el-select v-model="queryParams.roomId" placeholder="请选择" clearable>
          <el-option
            v-for="room in roomList"
            :key="room.value"
            :value="room.value"
            :label="room.label"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="换洗状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable>
          <el-option
            v-for="dict in recordStatus.formatter"
            :key="dict.value"
            :value="dict.value"
            :label="dict.label"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="选择日期" prop="">
        <el-date-picker
          v-model="queryDateRange"
          type="daterange"
          value-format="yyyy-MM-dd"
          unlink-panels
          range-separator="至"
          start-placeholder="入住日期"
          end-placeholder="离店日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['cms:goods.record:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"/>
    </el-row>

    <el-table v-loading="loading" :data="recordList">
      <el-table-column label="#" type="index" align="center" width="60"/>
      <el-table-column label="入住编号" prop="roomRecordId" align="center" show-overflow-tooltip/>
      <el-table-column label="入住房间" prop="roomName" align="center" show-overflow-tooltip/>
      <el-table-column label="物品名称" prop="goodsName" align="center" show-overflow-tooltip/>
      <el-table-column label="换洗状态" prop="status" align="center" width="100">
        <template slot-scope="scope">
          <option-tag :options="recordStatus" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="开始日期" prop="startDate" align="center" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束日期" prop="endDate" align="center" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" prop="remark" align="center" show-overflow-tooltip/>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import {listRoom} from "@/api/cms/room";
import {listGoodsRecord} from "@/api/cms/goodsRecord";

export default {
  name: "CmsGoodsRecord",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 房间换洗记录表格数据
      recordList: [],
      // 房间表格数据
      roomList: [],
      // 日期范围
      queryDateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        roomId: null,
        roomRecordId: null,
        goodsId: null,
        status: null,
      },
      // 字典
      recordStatus: this.CmsDic.goods.records.status,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询房间换洗记录列表 */
    getList() {
      this.loading = true;
      listRoom({}).then(response => {
        this.roomList = response.rows.map(it => {
          return {value: it.id, label: it.name}
        });
        return listGoodsRecord(this.addDateRange(this.queryParams, this.queryDateRange, "Date"));
      }).then(response => {
        this.recordList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryDateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('cms/goods/record/export', {
        ...this.queryParams,
      }, `Goods_Record_${new Date().getTime()}.xlsx`)
    },
  },
};
</script>
