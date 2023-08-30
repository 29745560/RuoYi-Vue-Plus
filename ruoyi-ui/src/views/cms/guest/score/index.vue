<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="身份证号" prop="cardId">
        <el-input
          v-model="queryParams.cardId"
          placeholder="请填写"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['cms:guest.score:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"/>
    </el-row>

    <el-table v-loading="loading" :data="scoreList">
      <el-table-column label="#" type="index" align="center" width="60"/>
      <el-table-column label="身份证号" prop="cardId" show-overflow-tooltip/>
      <el-table-column label="总计积分" prop="scoreTotal" align="center"/>
      <el-table-column label="可用积分" prop="scoreUsable" align="center"/>
      <el-table-column label="账户状态" prop="status" align="center"/>
      <el-table-column label="创建时间" prop="createTime" align="center" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" prop="updateTime" align="center" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" prop="remark" show-overflow-tooltip/>
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
import {listGuestScore} from "@/api/cms/guestScore";

export default {
  name: "CmsGuestScore",
  data() {
    return {
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 旅客积分表格数据
      scoreList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        cardId: null,
        status: null,
      },
      // 字典
      scoreStatus: this.CmsDic.guest.score.status,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询旅客积分列表 */
    getList() {
      this.loading = true;
      listGuestScore(this.queryParams).then(response => {
        this.scoreList = response.rows;
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('cms/guest/score/export', {
        ...this.queryParams,
      }, `Guest_Score_${new Date().getTime()}.xlsx`)
    },
  },
};
</script>
