<template>
  <div class="preview-box">
    <el-page-header :content="title" @back="goBack"/>
    <el-row v-loading="loading" class="mt20 mb20">
      <el-col :span="24">
        <el-descriptions
          direction="horizontal"
          :column="3"
          :colon="false"
          size="medium"
          border
        >
          <el-descriptions-item label="客房编号" :span="3">
            {{ data.id }}
          </el-descriptions-item>
          <el-descriptions-item label="客房名称" :span="3">
            {{ data.name }}
          </el-descriptions-item>
          <el-descriptions-item label="客房类型" :span="3">
            {{ data.category.name }} | {{ data.category.beds }}床
          </el-descriptions-item>
          <el-descriptions-item label="客房价格" :span="3">
            ￥{{ data.category.price }} / 晚
          </el-descriptions-item>
          <el-descriptions-item label="客房状态" :span="3">
            <option-tag :options="roomStatus" :value="data.status"/>
          </el-descriptions-item>
        </el-descriptions>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {getRoom} from '@/api/cms/room.js'
import {getRoomCategory} from '@/api/cms/roomCategory.js'

export default {
  name: "CmsRoomNavPreview",
  components: {},
  props: {
    id: {
      type: [Number, String],
      required: false,
    },
    title: {
      type: String,
      default: '客房信息',
      required: false,
    },
  },
  watch: {
    id: {
      handler(newVal, oldVal) {
        if (newVal && newVal !== oldVal) {
          this.getData();
        }
      },
      deep: false,
      immediate: true,
    },
  },
  data() {
    return {
      loading: true,
      data: {
        id: null,
        categoryId: null,
        name: null,
        status: "0",
        category: {
          name: null,
          price: 0,
          beds: 0,
        },
      },
      roomStatus: this.CmsDic.room.status,
    };
  },
  computed: {},
  created() {},
  mounted() {
    //this.$nextTick(() => {});
  },
  methods: {
    getData() {
      this.loading = true;
      getRoom(this.id).then(response => {
        const resData = response.data;
        this.data.id = resData.id;
        this.data.categoryId = resData.categoryId;
        this.data.name = resData.name;
        this.data.status = resData.status;
        return getRoomCategory(resData.categoryId);
      }).then(response => {
        const resData = response.data;
        this.data.category.name = resData.name;
        this.data.category.price = resData.price;
        this.data.category.beds = resData.beds;
        this.loading = false;
      });
    },
    /** 返回上一页 */
    goBack() {
      this.$router.go(-1);
    },
  },
};
</script>
<style lang="scss" scoped>
.preview-box {
  .card-preview {
    ::v-deep .el-card__body {
      padding: 10px;
      text-align: center;
    }
  }
}
</style>
