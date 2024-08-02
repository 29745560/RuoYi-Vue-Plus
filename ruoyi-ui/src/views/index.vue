<template>
  <div class="home-container">
    <div class="app-container">
      <el-card shadow="hover">
        <span>欢迎访问XX酒店管理系统</span>
      </el-card>
      <panel-group/>
      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <line-chart/>
      </el-row>
      <el-row :gutter="32">
        <el-col :xs="24" :sm="12" :lg="12">
          <div class="chart-wrapper">
            <pie-chart/>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :lg="12">
          <div class="chart-wrapper">
            <bar-chart/>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import PanelGroup from './dashboard/PanelGroup'
import LineChart from './dashboard/LineChart'
import PieChart from './dashboard/PieChart'
import BarChart from './dashboard/BarChart'

export default {
  name: "Index",
  components: {
    PanelGroup,
    LineChart,
    PieChart,
    BarChart,
  },
  data() {
    return {};
  },
  methods: {},
};
</script>

<style scoped lang="scss">
.home-container {
  //background-color: whitesmoke;
  //height: 100vh;
  //width: 100%;

  background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 10px 10px 0;
    margin-bottom: 20px;
  }
}

@media (max-width: 1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>

