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
      <el-form-item label="客房名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请填写"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="入住编号" prop="roomRecordId">
        <el-input
          v-model="queryParams.roomRecordId"
          placeholder="请填写"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="客房状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable>
          <el-option
            v-for="dict in roomStatus.formatter"
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['cms:room:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['cms:room:edit']"
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
          v-hasPermi="['cms:room:remove']"
        >删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="roomList" @selection-change="handleSelectionChange">
      <el-table-column type="expand" align="center" width="35">
        <template slot-scope="props">
          <el-row>
            <el-col :span="24">
              <div style="margin: 20px">
                <el-descriptions
                  direction="horizontal"
                  :column="12"
                  :colon="false"
                  border
                >
                  <el-descriptions-item label="客房编号" :span="6">
                    {{ props.row.id }}
                  </el-descriptions-item>
                  <el-descriptions-item label="入住编号" :span="6">
                    {{ props.row.roomRecordId }}
                  </el-descriptions-item>
                  <el-descriptions-item label="创建时间" :span="6">
                    <span>{{ parseTime(props.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                  </el-descriptions-item>
                  <el-descriptions-item label="修改时间" :span="6">
                    <span>{{ parseTime(props.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                  </el-descriptions-item>
                  <el-descriptions-item label="创建者" :span="6">
                    {{ props.row.createBy }}
                  </el-descriptions-item>
                  <el-descriptions-item label="更新者" :span="6">
                    {{ props.row.updateBy }}
                  </el-descriptions-item>
                  <el-descriptions-item label="备注信息" :span="12">
                    {{ props.row.remark }}
                  </el-descriptions-item>
                </el-descriptions>
              </div>
            </el-col>
          </el-row>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="45" align="center"/>
      <el-table-column label="客房名称" prop="name" align="center" show-overflow-tooltip/>
      <el-table-column label="客房类型" prop="categoryId" align="center" :formatter="categoryNameFormatter"
                       show-overflow-tooltip/>
      <el-table-column label="客房价格" prop="categoryId" align="center" :formatter="categoryPriceFormatter"/>
      <el-table-column label="客房床位" prop="categoryId" align="center" :formatter="categoryBedsFormatter"/>
      <el-table-column label="客房状态" prop="status" align="center" width="120">
        <template slot-scope="scope">
          <option-tag :options="roomStatus" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" class-name="small-padding fixed-width" align="center" fixed="right">
        <template slot-scope="scope">
          <!-- 退房操作 -->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-house"
            @click="handleCheckout(scope.row)"
            v-hasPermi="['cms:room:edit', 'cms:room.record:edit']"
            :disabled="scope.row.status !== '2'"
          >退房
          </el-button>
          <!-- 物品列表 -->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-box"
            @click="handleOpenGoods(scope.row)"
            v-hasPermi="['cms:room:edit', 'cms:goods:edit']"
          >物品
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['cms:room:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['cms:room:remove']"
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

    <!-- 添加或修改客房对话框 -->
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
        <el-form-item label="客房名称" prop="name">
          <el-input v-model="form.name" placeholder="请填写" maxlength="50" clearable/>
        </el-form-item>
        <el-form-item v-if="form.status==='0'" label="客房类型" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择">
            <el-option
              v-for="dict in roomCategoryList"
              :key="dict.id"
              :label="dict.name"
              :value="dict.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="客房状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in roomStatus.formatter"
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
  listRoom,
  getRoom,
  addRoom,
  updateRoom,
  delRoom,
  checkoutRoom,
} from "@/api/cms/room";
import {listRoomCategory} from "@/api/cms/roomCategory";

export default {
  name: "CmsRoom",
  components: {},
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
      // 客房表格数据
      roomList: [],
      // 客房类型列表
      roomCategoryList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        categoryId: null,
        roomRecordId: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          {required: true, message: "客房名称不能为空", trigger: "blur"},
        ],
        categoryId: [
          {required: true, message: "请选择客房类型", trigger: ["blur", "change"]},
        ],
      },
      // 字典
      roomStatus: this.CmsDic.room.status,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询客房列表 */
    getList() {
      this.loading = true;
      listRoomCategory({}).then(response => {
        this.roomCategoryList = response.data;
        return listRoom(this.queryParams);
      }).then(response => {
        this.roomList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    categoryNameFormatter(row, column, cellValue) {
      const category = this.roomCategoryList.find(i => i.id === cellValue);
      return this.isnull(category) ? "" : (category.name || "");
    },
    categoryPriceFormatter(row, column, cellValue) {
      const category = this.roomCategoryList.find(i => i.id === cellValue);
      return this.isnull(category) ? "-" : `￥${(category.price || '-')}`;
    },
    categoryBedsFormatter(row, column, cellValue) {
      const category = this.roomCategoryList.find(i => i.id === cellValue);
      return this.isnull(category) ? "-" : `${(category.beds || '-')}床`;
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
        categoryId: null,
        roomRecordId: null,
        status: "0",
        remark: null,
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加客房";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids;
      getRoom(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改客房";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateRoom(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addRoom(this.form).then(response => {
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
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除编号为"' + ids + '"的客房？').then(() => {
        this.loading = true;
        return delRoom(ids);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
        this.loading = false;
      });
    },
    /**
     * 打开物品列表
     */
    handleOpenGoods(row) {
      const id = row.id;
      this.$router.push({
        path: "/hotel/room/goods",
        query: {roomId: id},
      })
    },
    /**
     * 宾客退房
     */
    handleCheckout(row) {
      const id = row.id;
      const name = row.name;
      this.$confirm('客房[' + name + '] 确认退房？', '系统提示', {
        distinguishCancelAndClose: true,
        confirmButtonText: '已支付',
        cancelButtonText: '未支付',
      }).then(() => {
        //已支付
        checkoutRoom(id, "1").then(() => {
          this.loading = false;
          this.getList();
          this.$modal.msgSuccess("退房成功");
        })
      }).catch(action => {
        //未支付
        if (action === 'cancel') {
          checkoutRoom(id, "0").then(() => {
            this.loading = false;
            this.getList();
            this.$modal.msgSuccess("退房成功");
          })
        }
      })
    },
  },
};
</script>
