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
      <el-form-item label="物品名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请填写"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="选择分类" prop="categoryId">
        <treeselect
          style="width: 200px"
          v-model="queryParams.categoryId"
          :options="categoryOptions"
          :normalizer="normalizer"
          :show-count="true"
          placeholder="请选择"
        />
      </el-form-item>
      <el-form-item label="选择房间" prop="roomId">
        <el-select v-model="queryParams.roomId" placeholder="请选择" clearable>
          <el-option
            v-for="room in roomList"
            :key="room.value"
            :value="room.value"
            :label="room.label"
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
          v-hasPermi="['cms:goods:add']"
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
          v-hasPermi="['cms:goods:edit']"
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
          v-hasPermi="['cms:goods:remove']"
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
          v-hasPermi="['cms:goods:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="goodsList" @selection-change="handleSelectionChange">
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
                  <el-descriptions-item label="NFC秘文" :span="12">
                    {{ props.row.secret }}
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
      <el-table-column label="物品编号" prop="id" align="center" show-overflow-tooltip/>
      <el-table-column label="物品名称" prop="name" align="center" show-overflow-tooltip/>
      <el-table-column label="物品图片" prop="coverUrl" align="center" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.coverUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="物品类别" prop="categoryName" align="center"/>
      <el-table-column label="房间名称" prop="roomName" align="center" show-overflow-tooltip/>
      <el-table-column label="物品分值" prop="score" align="center"/>
      <el-table-column label="物品状态" prop="status" align="center" width="100">
        <template slot-scope="scope">
          <option-tag :options="goodsStatus" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" class-name="small-padding fixed-width" align="center" width="120">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['cms:goods:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['cms:goods:remove']"
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

    <!-- 添加或修改物品对话框 -->
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
        <el-form-item label="物品编号" prop="id">
          <el-input v-model="form.id" :disabled="form.isUpdate" placeholder="请填写" maxlength="50" clearable/>
        </el-form-item>
        <el-form-item label="NFC秘文" prop="secret">
          <el-input
            v-model="form.secret"
            :disabled="form.isUpdate"
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4 }"
            placeholder="请填写"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="物品名称" prop="name">
          <el-input v-model="form.name" placeholder="请填写" maxlength="50" clearable/>
        </el-form-item>
        <el-form-item label="物品类别" prop="categoryId">
          <treeselect
            v-model="form.categoryId"
            :options="categoryOptions"
            :normalizer="normalizer"
            :show-count="true"
            placeholder="请选择"
          />
        </el-form-item>
        <el-form-item label="物品图片" prop="cover">
          <image-upload v-model="form.cover"/>
        </el-form-item>
        <el-form-item label="选择房间" prop="roomId">
          <el-select v-model="form.roomId" placeholder="请选择" clearable>
            <el-option
              v-for="room in roomList"
              :key="room.value"
              :value="room.value"
              :label="room.label"
              :disabled="room.disabled"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="物品分值" prop="score">
          <el-input-number v-model="form.score" :min="1" :max="100" :step-strictly="true" :step="1"/>
        </el-form-item>
        <el-form-item label="物品状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in goodsStatus.formatter"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
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
  listGoods,
  getGoods,
  addGoods,
  updateGoods,
  delGoods,
} from "@/api/cms/goods";
import {listRoom} from "@/api/cms/room";
import {listCategory} from '@/api/cms/category'
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "CmsGoods",
  components: {
    Treeselect,
  },
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
      // 物品表格数据
      goodsList: [],
      // 房间表格数据
      roomList: [],
      // 物品分类树选项
      categoryOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        name: null,
        roomId: null,
        categoryId: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          {required: true, message: "物品编号不能为空", trigger: "blur"},
        ],
        secret: [
          {required: true, message: "NFC秘文不能为空", trigger: "blur"},
        ],
        name: [
          {required: true, message: "物品名称不能为空", trigger: "blur"},
        ],
        categoryId: [
          {required: true, message: "物品分类不能为空", trigger: "blur"},
        ],
      },
      // 字典
      goodsStatus: this.CmsDic.goods.status,
    };
  },
  created() {
    this.getTreeselect();
    this.getList();
  },
  methods: {
    /** 查询物品列表 */
    getList() {
      this.loading = true;
      listRoom({}).then(response => {
        this.roomList = response.rows.map(it => {
          return {
            value: it.id,
            label: it.name,
            status: it.status,
            disabled: it.status === '9',
          }
        });
        return listGoods(this.queryParams)
      }).then(response => {
        this.goodsList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询物品分类下拉树结构 */
    getTreeselect() {
      listCategory().then(response => {
        this.categoryOptions = [];
        const data = {id: 0, name: '顶级节点', children: []};
        data.children = this.handleTree(response.data, "id", "parentId");
        this.categoryOptions.push(data);
      });
    },
    /** 转换物品分类数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.id,
        label: node.name,
        children: node.children,
      };
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        isUpdate: false,
        id: null,
        secret: null,
        name: null,
        cover: null,
        roomId: null,
        categoryId: null,
        score: "1",
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
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getTreeselect();
      this.open = true;
      this.title = "添加物品";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      this.getTreeselect();
      const id = row.id || this.ids;
      getGoods(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.form.isUpdate = true;
        this.open = true;
        this.title = "修改物品";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.isUpdate) {
            updateGoods(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addGoods(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除物品编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delGoods(ids);
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
      this.download('cms/goods/export', {
        ...this.queryParams,
      }, `Goods_${new Date().getTime()}.xlsx`)
    },
  },
};
</script>
