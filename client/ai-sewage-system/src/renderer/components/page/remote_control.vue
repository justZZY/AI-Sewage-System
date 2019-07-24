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
          authorization: authorization,
          apiBaseUrl: apiBaseUrl,
          boxNo: boxNo
        }).then(res => {
          let data = res['data']
          console.log(data)
          let dataArray = findEquipName(data)
          // 组装表数据
          console.log(dataArray)
          let ans = this.getEquipValue(dataArray)
          console.log(ans)
          return dataArray
        })
      },
      // 获取监控寄存器的值
      getEquipValue (dataArray) {
        let index = this.$store.state.ChooseData.chooseData
        let authorization = 'Bearer ' + window.jsonobj['access_token']
        let apiBaseUrl = window.equipmentobjarray[index]['box']['cs']['apiBaseUrl']
        let boxNo = window.equipmentobjarray[index]['box']['boxNo']
        let names = getMonitorNames(dataArray)
        console.log(names)
        this.$http.post('http://localhost:8081/test/getEquipValue', {
          authorization: authorization,
          apiBaseUrl: apiBaseUrl,
          boxNo: boxNo,
          names: names
        }).then(res => {
          console.log(res)
        })
      }
    },
    computed: {

    }
  }
  /*
   * 判定设备数据是否需要组合
   * arg: data:服务器上获取的json数据
   * return: 处理后的控制json数组
   *
   * --undo 优化 根据所需要的label数据生成相应的array数据
   */
  function findEquipName (data) {
    let indexState = -1
    let indexRemote = -1
    let indexEquip = -1
    for (let i = 0; i < data.length; i++) {
      if (data[i]['name'] === '状态') {
        indexState = i
      } else if (data[i]['name'] === '远程') {
        indexRemote = i
      } else if (name === '设备控制') {
        indexEquip = i
      }
    }
    let newData = []
    if (indexState !== -1 && indexRemote !== -1) {
      // 组合数组
      newData = data[indexState]['items'].concat(data[indexRemote]['items'])
    } else if (indexEquip !== -1) {
      // 单数组
      newData = data[indexEquip]['items']
    }
    return newData
  }
  /*
   * 获取寄存器设备名称作为json参数传出
   * return: names
   */
  function getMonitorNames (dataArray) {
    let names = []
    for (let i = 0; i < dataArray.length; i++) {
      names.push(dataArray[i]['name'])
    }
    return names
  }
  /*
   * 根据正则组合设备控制数据
   * arg: data:原始json拼凑的设备数据
   * return: 设备控制表数据
   */
  // function getDeviceData (data) {
  //
  // }
</script>

<style scoped>
  .el-row {
    margin-bottom: 20px;
  }
  .el-table .warning-row {
    background: #9d0006;
  }
</style>
