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
      <el-form-item label="身份证号" prop="cardId">
        <el-input
          v-model="queryParams.cardId"
          placeholder="请填写"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号码" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请填写"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="注册来源" prop="source">
        <el-select v-model="queryParams.source" placeholder="请选择" clearable>
          <el-option
            v-for="dict in sourceOptions.formatter"
            :key="dict.value"
            :value="dict.value"
            :label="dict.label"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="用户状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable>
          <el-option
            v-for="dict in statusOptions.formatter"
            :key="dict.value"
            :value="dict.value"
            :label="dict.label"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['cms:user:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['cms:user:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['cms:user:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"/>
    </el-row>

    <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
      <el-table-column type="expand" align="center" width="35">
        <template slot-scope="props">
          <el-row>
            <el-col :span="24">
              <div style="margin: 20px">
                <el-divider content-position="left">用户信息</el-divider>
                <el-descriptions
                  direction="horizontal"
                  :column="12"
                  :colon="false"
                  border
                >
                  <el-descriptions-item label="微信用户唯一标识" :span="4">
                    {{ props.row.openId }}
                  </el-descriptions-item>
                  <el-descriptions-item label="微信开放平台唯一标识" :span="4">
                    {{ props.row.unionId }}
                  </el-descriptions-item>
                  <el-descriptions-item label="会话密钥" :span="4">
                    {{ props.row.sessionKey }}
                  </el-descriptions-item>
                  <el-descriptions-item label="备注" :span="12">
                    {{ props.row.remark }}
                  </el-descriptions-item>
                </el-descriptions>
              </div>
            </el-col>
          </el-row>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="45" align="center"/>
      <el-table-column label="用户编号" prop="id" align="center" width="180"/>
      <el-table-column label="头像" prop="avatar" align="center" width="80">
        <template slot-scope="scope">
          <el-avatar :src="scope.row.avatar" :size="40" icon="el-icon-user-solid"/>
        </template>
      </el-table-column>
      <el-table-column label="身份证号" prop="cardId" align="center" show-overflow-tooltip/>
      <el-table-column label="微信昵称" prop="nickname" align="center" show-overflow-tooltip/>
      <el-table-column label="手机" prop="phone" align="center" show-overflow-tooltip/>
      <el-table-column label="邮箱" prop="email" align="center" show-overflow-tooltip/>
      <el-table-column label="注册来源" align="center" width="80">
        <template slot-scope="scope">
          <option-tag :value="scope.row.source" :options="sourceOptions" effect="plain"/>
        </template>
      </el-table-column>
      <el-table-column label="帐号状态" align="center" width="80">
        <template slot-scope="scope">
          <option-tag :value="scope.row.status" :options="statusOptions"/>
        </template>
      </el-table-column>
      <el-table-column label="注册时间" prop="createTime" align="center" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最后登录时间" prop="loginDate" align="center" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.loginDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最后登录IP" prop="loginIp" align="center" width="160" show-overflow-tooltip/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['cms:user:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['cms:user:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 修改微信用户对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      :destroy-on-close="false"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      width="500px"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="身份证号" prop="cardId">
          <el-input v-model="form.cardId" placeholder="请填写" maxlength="50" clearable/>
        </el-form-item>
        <el-form-item label="邮箱地址" prop="email">
          <el-input v-model="form.email" placeholder="请填写" maxlength="50" clearable/>
        </el-form-item>
        <el-form-item label="用户状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in statusOptions.formatter"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注信息" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4 }"
            placeholder="请输入"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listUser, getUser, updateUser, delUser} from "@/api/cms/user";

export default {
  name: "CmsUser",
  data() {
    return {
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 微信用户表格数据
      userList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        cardId: null,
        phone: null,
        source: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      // 字典
      statusOptions: this.CmsDic.user.status,
      sourceOptions: this.CmsDic.user.source,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询微信用户列表 */
    getList() {
      this.loading = true;
      listUser(this.queryParams).then(response => {
        this.userList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        cardId: null,
        email: null,
        status: "0",
      };
      this.resetForm("form");
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids;
      getUser(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改微信用户";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateUser(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除微信用户编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delUser(ids);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('cms/user/export', {
        ...this.queryParams,
      }, `User_${new Date().getTime()}.xlsx`)
    },
  },
};
</script>
