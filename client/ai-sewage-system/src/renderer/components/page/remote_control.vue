<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="always">
          <div class="clearfix" slot="header">
            <span>传感器监控</span>
          </div>
          <div>
            <el-table :data="monitordata" height="275" stripe>
              <el-table-column prop="name" label="传感器名称" align="center"></el-table-column>
              <el-table-column prop="value" label="数值" align="center"></el-table-column>
            </el-table>
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
    <el-row class="bottomRow" :gutter="20">
      <el-col>
        <el-card shadow="always" body-style="height: 100%">
          <div slot="header">
            <span>设备控制</span>
          </div>
          <div>
            <el-table :data="devicedata" style="width: 100%" height="300" border stripe :row-class-name="tableRowClassName">
              <el-table-column prop="name" label="设备名称" align="center"></el-table-column>
              <el-table-column prop="运行反馈" label="运行反馈" align="center"></el-table-column>
              <el-table-column prop="故障反馈" label="故障反馈" align="center"></el-table-column>
              <el-table-column prop="手自动开关" label="手动/自动" align="center">
                <template slot-scope="scope">
                  <el-switch
                    v-if="scope.row.手自动开关 !== undefined" 
                    v-model="scope.row.手自动开关"
                    active-text="自动"
                    inactive-text="手动"
                    @change="autoChange(scope)">
                  </el-switch>
                </template>
              </el-table-column>
              <el-table-column prop="启停开关" label="启动/停止" align="center">
                <template slot-scope="scope">
                  <el-switch
                    v-if="scope.row.启停开关 !== undefined"
                    v-model="scope.row.启停开关"
                    active-text="启动"
                    inactive-text="停止"
                    @change="runChange(scope)">
                  </el-switch>
                </template>
              </el-table-column>
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
        devicedata: []
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
          let values = res['data']
          let deviceData = formatDeviceData(values, dataArray)
          console.log(deviceData)
          this.devicedata = deviceData
        })
      },
      autoChange (scope) {
        console.log(scope)
        let name = scope['row']['name']
        this.$confirm('此操作将修改设备' + name + '的值, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$message({
            type: 'success',
            message: '修改成功'
          })
        }).catch(() => {
          scope['row']['手自动开关'] = !scope['row']['手自动开关']
          this.$message({
            type: 'info',
            message: '已取消修改'
          })
        })
      },
      runChange (scope) {
        console.log(scope)
        let name = scope['row']['name']
        this.$confirm('此操作将修改设备' + name + '的值, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$message({
            type: 'success',
            message: '修改成功'
          })
        }).catch(() => {
          scope['row']['启停开关'] = !scope['row']['启停开关']
          this.$message({
            type: 'info',
            message: '已取消修改'
          })
        })
      }
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
      } else if (data[i]['name'] === '设备控制') {
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
  function formatDeviceData (datas, labelArray) {
    console.log(datas)
    let deviceArray = []
    for (let i = 0; i < datas.length; i++) {
      let originName = datas[i]['name']
      // 分割字符串名
      let names = originName.split('_')
      let deviceName = names[1]
      let labelName = names[2]
      let label = getDeviceLabel(labelArray, datas, i, labelName)
      // 组装数据
      let index = getDeviceIndex(deviceArray, deviceName)
      if (index === -1) {
        deviceArray.push({'name': deviceName, [labelName]: label})
      } else {
        deviceArray[index][labelName] = label
      }
    }
    return deviceArray
  }
  /*
   * 获取相同设备名的index
   */
  function getDeviceIndex (deviceArray, name) {
    for (let index = 0; index < deviceArray.length; index++) {
      const element = deviceArray[index]
      if (element['name'] === name) {
        return index
      }
    }
    return -1
  }
  /*
   * 获取设备label名
   * 判定太多单独提出来作为一个函数
   */
  function getDeviceLabel (labelArray, datas, i, labelName) {
    let dataType = datas[i]['dataType']
    let value = datas[i]['value']
    // 位类型获取标记label
    let label = ''
    // 匹配正则 判定是否返回bool型数据
    let feedbackReg = /.*反馈/
    let switchReg = /.*开关/
    let feedbackFlag = feedbackReg.test(labelName)
    let switchFlag = switchReg.test(labelName)
    if (dataType === 0 && feedbackFlag) {
      if (value === 0) {
        label = labelArray[i]['label']['ftext']
      } else {
        label = labelArray[i]['label']['ttext']
      }
    } else if (dataType === 0 && switchFlag) {
      if (value === 0) {
        label = false
      } else {
        label = true
      }
    } else {
    }
    return label
  }
</script>

<style scoped>
  .el-row {
    margin-bottom: 20px;
  }
  .el-table .warning-row {
    background: #9d0006;
  }
  .bottomRow {
    margin-bottom: 0px;
  }
</style>
