<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="10">
        <el-card shadow="always">
          <div slot="header">
            <span>频率控制器</span>
          </div>
          <div>
            <div v-for="(sliderObj, index) in sliderData" class="slider">
              <span style="text-align: center">{{sliderObj.name}}</span>
              <el-slider v-model="sliderObj.value" vertical height="194px" show-input :show-input-controls="false"
                         :max="30"  @change="sliderChange(sliderObj.value, index)" :format-tooltip="formatRateShow">
              </el-slider>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card shadow="always">
          <div slot="header">
            <span>站点组态图信息</span>
          </div>
          <div>
            <img src="@/assets/EquipState.png" class="equipImage">
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
            <el-table :data="devicedata" style="width: 100%" height="300" border stripe>
              <el-table-column prop="name" label="设备名称" align="center" width="160"></el-table-column>
              <el-table-column prop="手自动反馈" label="手自动反馈" align="center" width="100"></el-table-column>
              <el-table-column prop="运行反馈" label="运行反馈" align="center" width="100"></el-table-column>
              <el-table-column prop="故障反馈" label="故障反馈" align="center" width="100"></el-table-column>
              <el-table-column prop="手自动开关" label="手动/自动" align="center" width="160">
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
              <el-table-column prop="启停开关" label="启动/停止" align="center" min-width="190">
                <template slot-scope="scope">
                  <el-button v-if="scope.row.手自动开关 === false && scope.row.运行反馈 === '停止'" type="primary" plain @click="startEquipBtn(scope)">启动</el-button>
                  <el-button v-if="scope.row.手自动开关 === true || scope.row.运行反馈 === '运行'" type="primary" disabled plain @click="startEquipBtn(scope)">启动</el-button>
                  <el-button v-if="scope.row.手自动开关 === false && scope.row.运行反馈 === '运行'" type="primary" plain @click="stopEquipBtn(scope)">停止</el-button>
                  <el-button v-if="scope.row.手自动开关 === true || scope.row.运行反馈 === '停止'" type="primary" disabled plain @click="stopEquipBtn(scope)">停止</el-button>
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
  // 取消jQuery操作
  // window.$ = window.jQuery = require('jquery')
  export default {
    name: 'remote_control',
    data () {
      this.getEquipMonitor()
      return {
        devicedata: [],
        sliderData: [] // 设置频率滑动条json数组
      }
    },
    // // 多数站点存在数据格式不统一问题
    // // 暂时弃用切换刷新
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
      setTimer () {
        if (this.timer === null) {
          this.timer = setInterval(this.getEquipMonitor, 5000)
        }
      },
      /*
       * 启动websocket连接
       */
      // websocket () {
      //   let ws = new WebSocket('ws://localhost:8081/websocket')
      //   ws.onopen = () => {
      //     console.log('打开websocket连接')
      //   }
      //   ws.onmessage = res => {
      //     // let index = this.$store.state.Treedata.chooseData
      //     // let boxId = window.equipmentobjarray[index]['box']['id']
      //     // 做数据解析
      //     // solveSocketData(this.devicedata, boxId, res)
      //     console.log(res)
      //   }
      //   ws.onclose = () => {
      //     console.log('连接已关闭')
      //   }
      //   // 界面跳转websocket关闭函数
      //   this.over = () => {
      //     ws.close()
      //   }
      // },
      // 获取监控的数据 会通过计算进行变动
      // args: apiBaseUrl boxNo
      // 参数根据equipmentobjarray和vuex中存储的下标进行计算
      getEquipMonitor () {
        let index = this.$store.state.Treedata.chooseData
        console.log(index)
        let authorization = 'Bearer ' + window.jsonobj['access_token']
        let apiBaseUrl = window.equipmentobjarray[index]['box']['cs']['apiBaseUrl']
        let boxNo = window.equipmentobjarray[index]['box']['boxNo']
        this.$http.post('http://localhost:8081/equip/getEquipMonitor', {
          authorization: authorization,
          apiBaseUrl: apiBaseUrl,
          boxNo: boxNo
        }, {
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(res => {
          let data = res['data']
          console.log(data)
          // 请求设备控制数据
          let equipDataArray = findEquipName(data)
          console.log(equipDataArray)
          this.getEquipValue(equipDataArray, 'device')
          // 请求频率设备控制数据
          let rateControlNameArray = findRateName(data)
          console.log(rateControlNameArray)
          this.getEquipValue(rateControlNameArray, 'rate')
        })
      },
      // 获取监控寄存器的值
      getEquipValue (dataArray, type) {
        let index = this.$store.state.Treedata.chooseData
        let authorization = 'Bearer ' + window.jsonobj['access_token']
        let apiBaseUrl = window.equipmentobjarray[index]['box']['cs']['apiBaseUrl']
        let boxNo = window.equipmentobjarray[index]['box']['boxNo']
        let names = getMonitorNames(dataArray)
        console.log(names)
        this.$http.post('http://localhost:8081/equip/getEquipValue', {
          authorization: authorization,
          apiBaseUrl: apiBaseUrl,
          boxNo: boxNo,
          names: names
        }, {
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(res => {
          console.log(res)
          let values = res['data']
          if (type === 'device') {
            this.devicedata = formatDeviceData(values, dataArray)
          } else if (type === 'rate') {
            this.sliderData = formatSliderData(values)
          }
        })
      },
      autoChange (scope) {
        console.log(scope)
        let key = '手自动开关'
        let name = scope['row']['name']
        let value = 0
        if (scope['row']['手自动开关'] === true) {
          value = 1
        }
        this.$confirm('此操作将修改设备' + name + '的值, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.setEquipValue(formatName(key, name), 0, value)
        }).catch(() => {
          scope['row']['手自动开关'] = !scope['row']['手自动开关']
          this.$message({
            type: 'info',
            message: '已取消修改'
          })
        })
      },
      startEquipBtn (scope) {
        console.log(scope)
        let key = '启动开关'
        let name = scope['row']['name']
        let keyName = formatName(key, name)
        let type = 0
        let value = 1
        this.$confirm('此操作将启动设备' + name + ', 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.setEquipValue(keyName, type, value)
        }).catch(() => {
          scope['row']['启停开关'] = !scope['row']['启停开关']
          this.$message({
            type: 'info',
            message: '已取消修改'
          })
        })
      },
      stopEquipBtn (scope) {
        console.log(scope)
        let key = '停止开关'
        let name = scope['row']['name']
        let keyName = formatName(key, name)
        let type = 0
        let value = 1
        this.$confirm('此操作将关闭设备' + name + ', 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.setEquipValue(keyName, type, value)
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消修改'
          })
        })
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
        this.$http.post('http://localhost:8081/equip/setEquipValue', {
          authorization: authorization,
          apiUrl: apiBaseUrl,
          boxNo: boxNo,
          name: name,
          type: type,
          value: value
        }, {
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(res => {
          console.log(res)
          this.$message({
            type: 'success',
            message: '修改成功'
          })
        })
      },
      sliderChange (value, index) {
        console.log(value)
        console.log(index)
        this.$confirm('此操作将更改设备' + this.sliderData[index]['name'] + '频率上限为' + value + 'Hz, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.setEquipValue(this.sliderData[index]['originName'], 0, value)
          this.sliderData[index]['preValue'] = value
        }).catch(() => {
          this.sliderData[index]['value'] = this.sliderData[index]['preValue']
          this.$message({
            type: 'info',
            message: '已取消修改'
          })
        })
      },
      formatRateShow (value) {
        return value + ' Hz'
      }
    },
    created () {
      clearInterval(this.timer)
      this.timer = null
      // 开始定时器
      this.setTimer()
      // this.websocket()
    },
    beforeDestroy () {
      clearInterval(this.timer)
      this.timer = null
      // this.over()
    },
    mounted () {
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
    for (let i = 0; i < data.length; i++) {
      if (data[i]['name'] === '状态') {
        indexState = i
      } else if (data[i]['name'] === '远程') {
        indexRemote = i
      }
    }
    let newData = []
    if (indexState !== -1 && indexRemote !== -1) {
      // 组合数组
      newData = data[indexState]['items'].concat(data[indexRemote]['items'])
    }
    return newData
  }

  /*
   * 做正则解析 只取评率控制数据
   */
  function findRateName (data) {
    let rateReg = /.*设定/
    let index = -1
    let nameArray = []
    for (let i = 0; i < data.length; i++) {
      if (data[i]['name'] === '频率') {
        index = i
      }
    }
    for (let i = 0; i < data[index]['items'].length; i++) {
      if (rateReg.test(data[index]['items'][i]['name'])) {
        nameArray.push(data[index]['items'][i])
      }
    }
    return nameArray
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
   * 设置控制条组合json数据
   */
  function formatSliderData (datas) {
    console.log(datas)
    let sliderData = []
    for (let i = 0; i < datas.length; i++) {
      let name = datas[i]['name'].split('_')[1]
      let value = datas[i]['value']
      let obj = {
        originName: datas[i]['name'],
        preValue: value,
        name: name,
        value: value
      }
      sliderData.push(obj)
    }
    return sliderData
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

  /*
   * 组装寄存器name
   * 规则: 反馈=>状态 开关=>远程 **=>传感
   */
  function formatName (key, name) {
    let ans = ''
    let reg = /.*开关/
    if (reg.test(key)) {
      ans = '远程'
    }
    return ans + '_' + name + '_' + key
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
  .slider {
    margin-left: 20px;
    float: left;
  }
  .el-slider {
    margin-top: 10px;
    margin-left: 35%;
  }
  .equipImage {
    height: 260px;
  }
</style>
