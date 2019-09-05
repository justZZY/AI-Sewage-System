<template>
  <div>
    <el-card>
      <div slot="header">
        <span>定时控制器</span>
      </div>
      <div>
        <el-table :data="timeDataArray" style="width: 100%" height="580" border stripe>
          <el-table-column prop="name" label="设备名称" align="center"></el-table-column>
          <el-table-column prop="openTime" label="开机时间" align="center">
            <template slot-scope="scope">
              <el-time-picker style="width: 120px" v-model="scope.row.openTime" :editable="false" :clearable="false"
                  format="HH:mm" value-format="HH:mm" :picker-options="{format: 'HH:mm'}"
                  placeholder="任意时间点" @change="timeChange(scope)">
              </el-time-picker>
            </template>
          </el-table-column>
          <el-table-column prop="closeTime" label="关机时间" align="center">
            <template slot-scope="scope">
              <el-time-picker style="width: 120px" v-model="scope.row.closeTime" :editable="false" :clearable="false"
                              format="HH:mm" value-format="HH:mm" :picker-options="{format: 'HH:mm'}"
                              placeholder="任意时间点" @change="timeChange(scope)">
              </el-time-picker>
            </template>
          </el-table-column>
          <el-table-column prop="runTime" label="运行时间" align="center" width="190">
            <template slot-scope="scope">
              <el-input placeholder="运行分钟" style="width: 80px" v-model="scope.row.runTime" prefix-icon="el-icon-timer"></el-input>
              <el-button type="primary" plain @click="minChange(scope)">修改</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="stopTime" label="停止时间" align="center" width="190">
            <template slot-scope="scope">
              <el-input placeholder="停止分钟" style="width: 80px" v-model="scope.row.runTime" prefix-icon="el-icon-timer"></el-input>
              <el-button type="primary" plain @click="minChange(scope)">修改</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script>
  export default {
    name: 'time_control',
    data () {
      return {
        timeDataArray: []
      }
    },
    created () {
      this.getEquipMonitor()
    },
    // // 站点数据格式不统一 暂时弃用
    // computed: {
    //   refreshPage () {
    //     return this.$store.state.Treedata.chooseData
    //   }
    // },
    // watch: {
    //   refreshPage: function () {
    //     this.getEquipMonitor()
    //   }
    // },
    methods: {
      // 获取监控的数据 会通过计算进行变动
      // args: apiBaseUrl boxNo
      // 参数根据equipmentobjarray和vuex中存储的下标进行计算
      getEquipMonitor () {
        let index = this.$store.state.Treedata.chooseData
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
          // 请求设备控制数据
          let timeNameArray = findTimeName(data)
          console.log(timeNameArray)
          this.getTimeValue(timeNameArray)
        })
      },
      getTimeValue (timeNameArray) {
        let index = this.$store.state.Treedata.chooseData
        let authorization = 'Bearer ' + window.jsonobj['access_token']
        let apiBaseUrl = window.equipmentobjarray[index]['box']['cs']['apiBaseUrl']
        let boxNo = window.equipmentobjarray[index]['box']['boxNo']
        console.log(timeNameArray)
        this.$http.post('http://localhost:8081/test/getEquipValue', {
          authorization: authorization,
          apiBaseUrl: apiBaseUrl,
          boxNo: boxNo,
          names: timeNameArray
        }).then(res => {
          this.timeDataArray = formatTimeData(res)
          console.log(this.timeDataArray)
        })
      },
      // 启停时间控制
      timeChange (scope) {
        console.log(scope)
        let title = scope['column']['property']
        let label = scope['column']['label']
        let name = scope['row']['name']
        let newTime = scope['row'][title]
        let timeArray = newTime.split(':')
        let equipName = '定时_' + name + '_'
        this.$confirm('此操作将修改设备' + name + '的' + label + '为' + newTime + ', 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let openReg = /开机.*/
          let closeReg = /关机.*/
          // 用于存储时间分割命名
          let timeNameArray = []
          if (openReg.test(label)) {
            timeNameArray[0] = '开机小时'
            timeNameArray[1] = '开机分钟'
            for (let i = 0; i < timeArray.length; i++) {
              this.setEquipValue(equipName + timeNameArray[i], 0, timeArray[i])
            }
          }
          if (closeReg.test(label)) {
            timeNameArray[0] = '关机小时'
            timeNameArray[1] = '关机分钟'
            for (let i = 0; i < timeArray.length; i++) {
              this.setEquipValue(equipName + timeNameArray[i], 0, timeArray[i])
            }
          }
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消修改'
          })
        })
      },
      /* 关于运行时间的修改
       * 先显示时间拖动条
       * 再提示是否确认修改
       */
      minChange (scope) {
        console.log(scope)
      },
      /*
       * 通用接口
       * 向服务器传值,修改相关的寄存器值
       */
      setEquipValue (name, type, value) {
        let index = this.$store.state.Treedata.chooseData
        let authorization = 'Bearer ' + window.jsonobj['access_token']
        let apiBaseUrl = window.equipmentobjarray[index]['box']['cs']['apiBaseUrl']
        let boxNo = window.equipmentobjarray[index]['box']['boxNo']
        this.$http.post('http://localhost:8081/test/setEquipValue', {
          authorization: authorization,
          apiUrl: apiBaseUrl,
          boxNo: boxNo,
          name: name,
          type: type,
          value: value
        }).then(res => {
          console.log(res)
          this.$message({
            type: 'success',
            message: '修改成功'
          })
        })
      }
    }
  }
  function findTimeName (data) {
    console.log(data)
    let index = -1
    let nameArray = []
    for (let i = 0; i < data.length; i++) {
      if (data[i]['name'] === '定时') {
        index = i
      }
    }
    for (let i = 0; i < data[index]['items'].length; i++) {
      nameArray.push(data[index]['items'][i]['name'])
    }
    return nameArray
  }
  /*
   * 组合时间控制设备
   */
  function formatTimeData (res) {
    let array = res['data']
    let firstMergeArray = []
    // 第一遍解析 将同名对象整合到一个json对象中
    for (let i = 0; i < array.length; i++) {
      let names = array[i]['name'].split('_')
      let name = names[1]
      let type = names[2]
      let index = findNameInJsonArray(firstMergeArray, name)
      if (index !== -1) {
        firstMergeArray[index][type] = array[i]['value']
      } else {
        let obj = {name: name, [type]: array[i]['value']}
        firstMergeArray.push(obj)
      }
    }
    console.log(firstMergeArray)
    let secondMergeArray = []
    for (let i = 0; i < firstMergeArray.length; i++) {
      // 通过命名规则保证存在以下命名数据
      let openHour = firstMergeArray[i]['开机小时']
      let openMin = firstMergeArray[i]['开机分钟']
      let closeHour = firstMergeArray[i]['关机小时']
      let closeMin = firstMergeArray[i]['关机分钟']
      let obj = {
        name: firstMergeArray[i]['name'],
        openTime: mergeTime(openHour, openMin),
        closeTime: mergeTime(closeHour, closeMin),
        runTime: firstMergeArray[i]['运行时间'],
        stopTime: firstMergeArray[i]['停止时间']
      }
      secondMergeArray.push(obj)
    }
    console.log(secondMergeArray)
    return secondMergeArray
  }
  /*
   * 如果name存在于json数组中 返回true
   * 不存在则返回-1
   */
  function findNameInJsonArray (jsonArray, name) {
    for (let i = 0; i < jsonArray.length; i++) {
      if (jsonArray[i]['name'] === name) {
        return i
      }
    }
    return -1
  }
  /*
   * 合并时间小时:分钟
   */
  function mergeTime (hour, min) {
    if (hour < 10) {
      hour = '0' + hour
    }
    if (min < 10) {
      min = '0' + min
    }
    return hour + ':' + min
  }
</script>

<style scoped>
</style>
