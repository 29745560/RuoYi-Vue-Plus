<template>
  <div class="rs-drawer">
    <div class="input-box">
      <el-button
        type="primary"
        plain
        icon="el-icon-check"
        size="mini"
        @click="handleShowDrawer"
        v-hasPermi="['cms:goods:edit']"
      >分配
      </el-button>
    </div>

    <el-drawer
      :title="title"
      :visible.sync="open"
      :append-to-body="true"
      :wrapperClosable="false"
      :close-on-press-escape="false"
      :destroy-on-close="true"
      @opened="handleOpened"
      @closed="handleClosed"
      direction="rtl"
      size="1000px"
    >
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
              icon="el-icon-check"
              size="mini"
              :disabled="multiple"
              @click="handleSelect"
              v-hasPermi="['cms:goods:edit']"
            >选择
            </el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="goodsList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="45" align="center"/>
          <el-table-column label="物品编号" prop="id" align="center" show-overflow-tooltip/>
          <el-table-column label="物品名称" prop="name" align="center" show-overflow-tooltip/>
          <el-table-column label="物品图片" prop="coverUrl" align="center" width="100">
            <template slot-scope="scope">
              <image-preview :src="scope.row.coverUrl" :width="50" :height="50"/>
            </template>
          </el-table-column>
          <el-table-column label="物品分类" prop="categoryName" align="center"/>
          <el-table-column label="物品类型" prop="type" align="center" width="100">
            <template slot-scope="scope">
              <option-tag :options="goodsType" :value="scope.row.type" effect="plain"/>
            </template>
          </el-table-column>
          <el-table-column label="操作" class-name="small-padding fixed-width" align="center" width="80">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                @click="handleSelect(scope.row)"
                v-hasPermi="['cms:goods:edit']"
              >选择
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
      </div>
    </el-drawer>
  </div>
</template>

<script>
import {listGoods} from '@/api/cms/goods'
import {listGoodsCategory} from '@/api/cms/goodsCategory.js'
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "CmsRoomGoodsSelectDrawer",
  components: {
    Treeselect,
  },
  props: {},
  watch: {
    // demo:{
    //   handler(newVal, oldVal) {
    //   },
    //   deep:true,
    //   immediate:true,
    // }
  },
  data() {
    return {
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
      // 物品分类树选项
      categoryOptions: [],
      // 是否显示抽屉
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        categoryId: null,
        type: null,
        status: null,
      },
      // 字典
      goodsType: this.CmsDic.goods.type,
    };
  },
  computed: {
    title() {
      return '请选择可分配的物品';
    },
  },
  created() {
    console.log("drawer----->created");
  },
  mounted() {
    console.log("drawer----->mounted");
    this.$nextTick(() => {})
  },
  methods: {
    /** 查询物品列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {"select_usable": "true"};
      // 查询所有可分配的物品
      listGoods(this.queryParams).then(response => {
        this.goodsList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询物品分类下拉树结构 */
    getTreeselect() {
      listGoodsCategory().then(response => {
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
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
    /** 打开侧边栏 */
    handleShowDrawer() {
      this.open = true;
    },
    /** 打开侧边栏动画结束时的回调 */
    handleOpened() {
      this.getTreeselect();
      this.getList();
    },
    /** 关闭侧边栏动画结束时的回调 */
    handleClosed() {
      this.loading = true;
      this.ids = [];
      this.goodsList = [];
      this.categoryOptions = [];
      this.queryParams.pageNum = 1;
      this.queryParams.pageSize = 10;
      this.queryParams.name = null;
      this.queryParams.categoryId = null;
    },
    /** 选中按钮操作 */
    handleSelect(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认选择物品编号为"' + ids + '"的数据项？').then(() => {
        this.$emit('selected', ids);
        this.open = false;
      })
    },
  },
};
</script>
<style lang="scss" scoped>
.rs-drawer {
  //display: inline-block;
  .input-box {
  }
}
</style>
