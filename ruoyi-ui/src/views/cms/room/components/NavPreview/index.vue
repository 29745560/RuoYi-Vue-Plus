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
          <el-descriptions-item label="房间编号" :span="3">
            {{ data.id }}
          </el-descriptions-item>
          <el-descriptions-item label="房间名称" :span="3">
            {{ data.name }}
          </el-descriptions-item>
          <el-descriptions-item label="入住编号" :span="3">
            {{ data.roomRecordId || '暂无旅客入住' }}
          </el-descriptions-item>
          <el-descriptions-item label="房间状态" :span="3">
            <option-tag :options="roomStatus" :value="data.status"/>
          </el-descriptions-item>
        </el-descriptions>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {getRoom} from '@/api/cms/room.js'

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
      default: '房间信息',
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
        roomRecordId: null,
        name: null,
        status: "0",
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
        this.data = response.data;
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
