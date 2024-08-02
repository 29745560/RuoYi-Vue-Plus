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
      <el-form-item label="选择客房" prop="roomId">
        <el-select v-model="queryParams.roomId" placeholder="请选择" clearable>
          <el-option
            v-for="room in roomList"
            :key="room.value"
            :value="room.value"
            :label="room.label"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="支付状态" prop="pay">
        <el-select v-model="queryParams.pay" placeholder="请选择" clearable>
          <el-option
            v-for="dict in payStatus.formatter"
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['cms:room.record:add']"
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
          v-hasPermi="['cms:room.record:edit']"
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
          v-hasPermi="['cms:room.record:remove']"
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
          v-hasPermi="['cms:room.record:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
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
                  <el-descriptions-item label="入住编号" :span="6">
                    {{ props.row.id }}
                  </el-descriptions-item>
                  <el-descriptions-item label="客房编号" :span="6">
                    {{ props.row.roomId }}
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
                </el-descriptions>
              </div>
            </el-col>
          </el-row>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="45" align="center"/>
      <el-table-column label="入住客房" prop="room.name" align="center" show-overflow-tooltip/>
      <el-table-column label="身份证号" prop="cardId" align="center" show-overflow-tooltip/>
      <el-table-column label="入住日期" prop="checkInDate" align="center" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.checkInDate, '{y}/{m}/{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="离店日期" prop="checkOutDate" align="center" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.checkOutDate, '{y}/{m}/{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="客房单价" prop="unitPrice" align="center" :formatter="monFormatter"/>
      <el-table-column label="消费总额" prop="totalAmount" align="center" :formatter="monFormatter"/>
      <el-table-column label="支付状态" prop="pay" align="center" width="100">
        <template slot-scope="scope">
          <option-tag :options="payStatus" :value="scope.row.pay"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['cms:room.record:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['cms:room.record:remove']"
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

    <!-- 添加或修改客房入住记录对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      :destroy-on-close="false"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      width="600px"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <template v-if="isnull(form.id)">
          <el-form-item label="入住客房" prop="roomId">
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
          <el-form-item label="选择日期" prop="dateRange">
            <el-date-picker
              v-model="form.dateRange"
              type="daterange"
              value-format="yyyy-MM-dd"
              unlink-panels
              range-separator="至"
              start-placeholder="入住日期"
              end-placeholder="离店日期"
              :picker-options="pickerOptions"
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="身份证号" prop="cardId">
            <el-input
              v-model="form.cardId"
              placeholder="请填写"
              maxlength="50"
              clearable
            />
          </el-form-item>
        </template>
        <el-form-item label="宾客姓名" prop="realname">
          <el-input v-model="form.realname" placeholder="请填写" maxlength="50" clearable/>
        </el-form-item>
        <el-form-item label="联系方式" prop="contact">
          <el-input v-model="form.contact" placeholder="请填写" maxlength="50" clearable/>
        </el-form-item>
        <el-form-item label="宾客性别" prop="sex">
          <el-radio-group v-model="form.sex">
            <el-radio
              v-for="dict in guestSex.formatter"
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
  listRoomRecord,
  getRoomRecord,
  addRoomRecord,
  updateRoomRecord,
  delRoomRecord,
} from "@/api/cms/roomRecord";
import {listRoom} from "@/api/cms/room";

export default {
  name: "CmsRoomRecord",
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
      // 客房入住记录表格数据
      recordList: [],
      // 客房表格数据
      roomList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      queryDateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        roomId: null,
        cardId: null,
        pay: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        roomId: [
          {required: true, message: "请填写", trigger: "blur"},
        ],
        cardId: [
          {required: true, message: "请填写", trigger: "blur"},
        ],
        dateRange: [
          {required: true, message: "请选择入住离店日期", trigger: "blur"},
          {required: true, type: 'array', message: '请选择入住离店日期', trigger: 'change'},
        ],
      },
      //支付状态
      payStatus: this.CmsDic.room.records.pay,
      guestSex: this.CmsDic.guest.sex,
    };
  },
  computed: {
    pickerOptions() {
      return {
        disabledDate(time) {
          const times = new Date(new Date().toLocaleDateString()).getTime() + 30 * 8.64e7 - 1;
          return time.getTime() < Date.now() - 8.64e7 || time.getTime() > times;
        },
      }
    },
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询客房入住记录列表 */
    getList() {
      this.loading = true;
      listRoom({}).then(response => {
        this.roomList = response.rows.map(it => {
          return {
            value: it.id,
            label: it.name,
            status: it.status,
            disabled: it.status !== '0',
          }
        });
        return listRoomRecord(this.addDateRange(this.queryParams, this.queryDateRange, "Date"))
      }).then(response => {
        this.recordList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    monFormatter(row, column, cellValue) {
      return `￥${cellValue}`;
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
        roomId: null,
        cardId: null,
        checkInDate: null,
        checkOutDate: null,
        pay: 0,
        dateRange: [],
        remark: null,
        // 以下为宾客信息
        realname: null,
        contact: null,
        sex: "0",
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
      this.queryDateRange = [];
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
      this.open = true;
      this.title = "添加客房入住记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids;
      getRoomRecord(id).then(response => {
        let data = response.data;
        let formValues = {
          id: data.id,
          roomId: data.roomId,
          cardId: data.cardId,
          pay: data.pay,
          realname: data.guest.realname,
          contact: data.guest.contact,
          sex: data.guest.sex,
          remark: data.remark,
        };
        if (!this.isnull(data.checkInDate) && !this.isnull(data.checkOutDate)) {
          formValues.dateRange = [data.checkInDate, data.checkOutDate];
        }
        this.form = formValues;
        this.loading = false;
        this.open = true;
        this.title = "修改客房入住记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          const form_date_range = this.form.dateRange;
          if (!this.isnull(form_date_range) && form_date_range.length > 1) {
            this.form.checkInDate = form_date_range[0];
            this.form.checkOutDate = form_date_range[1];
            delete this.form.dateRange;
          }
          if (!this.isnull(this.form.id)) {
            updateRoomRecord(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addRoomRecord(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除客房入住记录编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delRoomRecord(ids);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('cms/room/record/export', {
        ...this.queryParams,
      }, `Room_Record_${new Date().getTime()}.xlsx`)
    },
  },
};
</script>
