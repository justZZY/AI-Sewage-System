<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="always">
          <div class="clearfix" slot="header">
            <span>传感器监控</span>
          </div>
          <div v-for="o in 4" :key="o">
            {{'列表内容 ' + o}}
          </div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card shadow="always">
          <div slot="header">
            <span>站点组态图信息</span>
          </div>
          <div>
            <span>组态图内容--undo</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col>
        <el-card shadow="always" body-style="height: 100%">
          <div slot="header">
            <span>设备控制</span>
          </div>
          <div>
            <el-table :data="devicedata" style="width: 100%" height="300" :row-class-name="tableRowClassName">
              <el-table-column prop="name" label="设备名称" align="center"></el-table-column>
              <el-table-column prop="run_feedback" label="运行反馈" align="center"></el-table-column>
              <el-table-column prop="error_feedback" label="故障反馈" align="center"></el-table-column>
              <el-table-column prop="state" label="手动/自动" align="center"></el-table-column>
              <el-table-column prop="operate" label="操作" align="center"></el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  export default {
    name: 'remote_control',
    data () {
      let test = this.getEquipMonitor()
      console.log(test)
      return {
        devicedata: [{
          name: '测试1',
          run_feedback: '测试反馈1',
          error_feedback: '测试故障反馈1',
          state: '自动测试1',
          operate: '测试测试测试测试测试1'
        }, {
          name: '测试2',
          run_feedback: '测试反馈2',
          error_feedback: '测试故障反馈2',
          state: '自动测试2',
          operate: '测试测试测试测试测试2'
        }, {
          name: '测试2',
          run_feedback: '测试反馈2',
          error_feedback: '测试故障反馈2',
          state: '自动测试2',
          operate: '测试测试测试测试测试2'
        }, {
          name: '测试2',
          run_feedback: '测试反馈2',
          error_feedback: '测试故障反馈2',
          state: '自动测试2',
          operate: '测试测试测试测试测试2'
        }, {
          name: '测试2',
          run_feedback: '测试反馈2',
          error_feedback: '测试故障反馈2',
          state: '自动测试2',
          operate: '测试测试测试测试测试2'
        }]
      }
    },
    methods: {
      tableRowClassName ({row, rowIndex}) {
        if (rowIndex === 1) {
          return 'warning-row'
        }
        return ''
      },
      // 获取监控的数据 会通过计算进行变动
      // args: apiBaseUrl boxNo
      // 参数根据equipmentobjarray和vuex中存储的下标进行计算
      getEquipMonitor () {
        let index = this.$store.state.ChooseData.chooseData
        console.log(index)
        let authorization = 'Bearer ' + window.jsonobj['access_token']
        let apiBaseUrl = window.equipmentobjarray[index]['box']['cs']['apiBaseUrl']
        let boxNo = window.equipmentobjarray[index]['box']['boxNo']
        this.$http.post('http://localhost:8081/test/getEquipMonitor', {
          Authorization: authorization,
          apiBaseUrl: apiBaseUrl,
          boxNo: boxNo
        }).then(res => {
          console.log('res=>')
          console.log(res)
        })
      }
    },
    computed: {

    }
  }
</script>

<style scoped>
  .el-row {
    margin-bottom: 20px;
  }
  .el-table .warning-row {
    background: #9d0006;
  }
</style>
