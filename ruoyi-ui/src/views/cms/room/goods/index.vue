<template>
  <div class="app-container">
    <nav-preview :id="queryParams.roomId"/>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <goods-select @selected="handleSelected"></goods-select>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-close"
          size="mini"
          :disabled="multiple"
          @click="handleRemoved"
          v-hasPermi="['cms:goods:edit']"
        >解除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="goodsList"
      @selection-change="handleSelectionChange"
      height="calc(100vh - 280px)"
    >
      <el-table-column type="selection" width="45" align="center"/>
      <el-table-column label="物品编号" prop="id" align="center" show-overflow-tooltip/>
      <el-table-column label="物品名称" prop="name" align="center" show-overflow-tooltip/>
      <el-table-column label="物品类别" prop="categoryName" align="center"/>
      <el-table-column label="物品状态" prop="status" align="center" width="100">
        <template slot-scope="scope">
          <option-tag :options="goodsStatus" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" class-name="small-padding fixed-width" align="center" width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="handleRemoved(scope.row)"
            v-hasPermi="['cms:goods:edit']"
          >解除
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
</template>

<script>
import {listGoods, addRoomGoods, delRoomGoods} from "@/api/cms/goods";
import NavPreview from "../components/NavPreview/index.vue";
import GoodsSelect from "../components/GoodsSelect/index.vue";

export default {
  name: "CmsRoomGoods",
  components: {
    NavPreview,
    GoodsSelect,
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
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        roomId: null,
      },
      // 字典
      goodsStatus: this.CmsDic.goods.status,
    };
  },
  created() {
    this.getRouterParams();
    this.getList();
  },
  methods: {
    /** 捕获路由参数 */
    getRouterParams() {
      const params = this.$route.query;
      this.queryParams.roomId = parseInt(`${params.roomId}`);
    },
    /** 查询物品列表 */
    getList() {
      this.loading = true;
      listGoods(this.queryParams).then(response => {
        this.goodsList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 分配按钮操作 */
    handleSelected(ids) {
      this.loading = true;
      const roomId = this.queryParams.roomId;
      addRoomGoods(roomId, ids).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("分配物品成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 解除按钮操作 */
    handleRemoved(row) {
      const ids = row.id || this.ids;
      const roomId = this.queryParams.roomId;
      this.$modal.confirm('是否确认解除物品编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delRoomGoods(roomId, ids);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("解除物品成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },
  },
};
</script>
