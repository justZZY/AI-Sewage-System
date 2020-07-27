<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card shadow="always">
          <div slot="header">
            <el-dropdown @command="handleDropDownCommand">
            <span class="el-dropdown-link">
              历史数据<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="day">日表</el-dropdown-item>
                <el-dropdown-item command="month">月表</el-dropdown-item>
                <el-dropdown-item command="quarter">月季度表</el-dropdown-item>
                <el-dropdown-item command="monthYear">月年表</el-dropdown-item>
                <el-dropdown-item command="quarterYear">季度年表</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
          <el-tabs :tab-position="tabPosition" style="height: 400px;">
            <el-tab-pane label="ph值">
              <div id="ph" style="width: 700px; height: 400px"></div>
            </el-tab-pane>
            <el-tab-pane label="氨氮值">
              <div id="an" style="width: 700px; height: 400px"></div>
            </el-tab-pane>
            <el-tab-pane label="累计流量">
              <div id="acc_flow" style="width: 700px; height: 400px"></div>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import {monitorOption, rateOption, barOption} from '../../js/chart'
  const echarts = require('echarts')

  export default {
    name: 'site_history_data',
    data () {
      return {
        tabPosition: 'left'
      }
    },
    mounted () {
      // 初始化图表数据
      this.setDataRecordCharts()
    },
    watch: {
      refreshPage: function () {
        this.$router.replace({
          path: '@/components/page/skip/history_data_skip',
          name: 'history_data_skip'
        })
      }
    },
    computed: {
      refreshPage () {
        return this.$store.state.Treedata.chooseData
      }
    },
    methods: {
      setDataRecordCharts () {
        let phBar = echarts.init(document.getElementById('ph'))
        let anBar = echarts.init(document.getElementById('an'))
        let accFlowBar = echarts.init(document.getElementById('acc_flow'))
        let phOption = JSON.parse(JSON.stringify(barOption))
        let anOption = JSON.parse(JSON.stringify(barOption))
        let accFlowOption = JSON.parse(JSON.stringify(barOption))
        phOption.legend.data = ['进水ph值', '出水ph值']
        phOption.series[0].name = '进水ph值'
        phOption.series[0].data = getRandomArray(5, 10, 5)
        phOption.series[1].name = '出水ph值'
        phOption.series[1].data = getRandomArray(5, 10, 5)
        anOption.legend.data = ['进水氨氮值', '出水氨氮值']
        anOption.series[0].name = '进水氨氮值'
        anOption.series[0].data = getRandomArray(0, 5, 5)
        anOption.series[1].name = '出水氨氮值'
        anOption.series[1].data = getRandomArray(0, 5, 5)
        accFlowOption.legend.data = ['累计进水量', '累计出水量']
        accFlowOption.series[0].name = '累计进水量'
        accFlowOption.series[0].data = getRandomArray(100, 500, 5)
        accFlowOption.series[1].name = '累计出水量'
        accFlowOption.series[1].data = getRandomArray(100, 500, 5)
        phBar.setOption(phOption, true)
        anBar.setOption(anOption, true)
        accFlowBar.setOption(accFlowOption, true)
      },
      handleDropDownCommand (command) {
        this.$message('click on item ' + command)
      }
    }
  }
  function getRandomArray (bottom, top, num) {
    let ans = []
    for (let i = 0; i < num; i++) {
      ans.push((Math.random() * (top - bottom) + bottom).toFixed(2))
    }
    return ans
  }
</script>

<style scoped>
  .el-dropdown-link {
    cursor: pointer;
    color: #409EFF;
  }
  .el-icon-arrow-down {
    font-size: 12px;
  }
</style>
