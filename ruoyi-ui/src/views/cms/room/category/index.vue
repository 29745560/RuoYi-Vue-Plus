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
      <el-form-item label="类型名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请填写"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="床位个数" prop="beds">
        <el-input
          v-model="queryParams.beds"
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['cms:goods.category:add']"
        >新增
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="categoryList">
      <el-table-column label="类型名称" prop="name" align="center" show-overflow-tooltip/>
      <el-table-column label="当前价格" prop="price" align="center" :formatter="monFormatter"/>
      <el-table-column label="床位个数" prop="beds" align="center" :formatter="numFormatter"/>
      <el-table-column label="备注" prop="remark" align="center" show-overflow-tooltip/>
      <el-table-column label="操作" class-name="small-padding fixed-width" align="center" fixed="right" width="180">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['cms:goods.category:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['cms:goods.category:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改客房类型对话框 -->
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
        <el-form-item label="类型名称" prop="name">
          <el-input v-model="form.name" placeholder="请填写" maxlength="50" clearable/>
        </el-form-item>
        <el-form-item label="当前价格" prop="price">
          <el-input-number
            v-model="form.price"
            :min="0"
            :max="9999"
            :precision="2"
            :step="0.01"
            :step-strictly="true"
          />
        </el-form-item>
        <el-form-item label="床位个数" prop="beds">
          <el-input-number
            v-model="form.beds"
            :min="1"
            :max="5"
            :step="1"
            :step-strictly="true"
          />
        </el-form-item>
        <el-form-item label="备注信息" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4 }"
            placeholder="请填写"
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
import {
  listRoomCategory,
  getRoomCategory,
  addRoomCategory,
  updateRoomCategory,
  delRoomCategory,
} from "@/api/cms/roomCategory.js";

export default {
  name: "CmsRoomCategory",
  data() {
    let checkBeds = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('床位个数不能为空'));
      }
      setTimeout(() => {
        if (!Number.isInteger(value)) {
          callback(new Error('请输入数字值'));
        } else {
          if (value < 1 || value > 5) {
            callback(new Error('数量必须是1至5'));
          } else {
            callback();
          }
        }
      }, 200);
    };
    let checkPrice = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('价格不能为空'));
      }
      setTimeout(() => {
        if (Number.isNaN(value)) {
          callback(new Error('请输入数字值'));
        } else {
          if (value < 0 || value > 9999) {
            callback(new Error('价格必须是0至9999元'));
          } else {
            callback();
          }
        }
      }, 200);
    };
    return {
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 客房类型表格数据
      categoryList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        name: null,
        beds: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          {required: true, message: "类型名称不能为空", trigger: "blur"},
        ],
        price: [
          {validator: checkPrice, trigger: 'blur'},
        ],
        beds: [
          {validator: checkBeds, trigger: 'blur'},
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询客房类型列表 */
    getList() {
      this.loading = true;
      listRoomCategory(this.queryParams).then(response => {
        this.categoryList = response.data;
        this.loading = false;
      });
    },
    monFormatter(row, column, cellValue) {
      return `￥${cellValue}`;
    },
    numFormatter(row, column, cellValue) {
      return `${cellValue}床`;
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
        name: null,
        price: 0,
        beds: 1,
        remark: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加客房类型";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id;
      getRoomCategory(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改客房类型";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateRoomCategory(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addRoomCategory(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
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
      const ids = row.id;
      this.$modal.confirm('是否确认删除客房类型编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delRoomCategory(ids);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
        this.loading = false;
      });
    },
  },
};
</script>
