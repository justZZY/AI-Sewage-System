<template>
  <div>
    <el-row :gutter="20">
<!--   中贸设备不存在频率计, 暂时取消频率设置   -->
<!--      <el-col :span="10">-->
<!--        <el-card shadow="always">-->
<!--          <div slot="header">-->
<!--            <span>频率控制器</span>-->
<!--          </div>-->
<!--          <div>-->
<!--            <div v-for="(sliderObj, index) in sliderData" class="slider">-->
<!--              <span style="text-align: center">{{sliderObj.name}}</span>-->
<!--              <el-slider v-model="sliderObj.value" vertical height="194px" show-input :show-input-controls="false"-->
<!--                         :max="30"  @change="sliderChange(sliderObj.value, index)" :format-tooltip="formatRateShow">-->
<!--              </el-slider>-->
<!--            </div>-->
<!--          </div>-->
<!--        </el-card>-->
<!--      </el-col>-->
      <el-col :span="24">
        <el-card shadow="always">
          <div slot="header">
            <span>站点组态图信息</span>
          </div>
          <el-image
              style="width: 100%; height: 100%"
              :src="imgUrl">
          </el-image>
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
            <el-col :span="8" v-for="equip in devicedata" style="margin-bottom: 10px">
              <el-card class="box-card">
                <div slot="header">
                  <span>{{equip.name}}</span>
                </div>
                <div class="desc">
                  <span>手自动状态: </span><span style="color: green">{{equip.maSwitch === true? '自动' : '手动'}}</span>
                  <el-switch
                      v-model="equip.maSwitch"
                      active-color="#13ce66"
                      inactive-color="#409eff"
                      @change="autoChange(equip)">
                  </el-switch>
                </div>
                <div class="desc">
                  <span>启停状态: </span><span style="color: green">{{equip.runFeedback === '运行'? '启动' : '停止'}}</span>
                  <el-button v-if="equip.maSwitch === false && equip.runFeedback === '停止'" type="primary" plain @click="startEquipBtn(equip)">启动</el-button>
                  <el-button v-if="equip.maSwitch === true || equip.runFeedback === '运行'" type="primary" disabled plain @click="startEquipBtn(equip)">启动</el-button>
                  <el-button v-if="equip.maSwitch === false && equip.runFeedback === '运行'" type="primary" plain @click="stopEquipBtn(equip)">停止</el-button>
                  <el-button v-if="equip.maSwitch === true || equip.runFeedback === '停止'" type="primary" disabled plain @click="stopEquipBtn(equip)">停止</el-button>
                </div>
                <div class="desc">
                  <span>故障反馈: </span><span style="color: green" v-if="equip.errorFeedback === '正常'">{{equip.errorFeedback}}</span><span style="color: red" v-if="equip.errorFeedback !== '正常'">{{equip.errorFeedback}}</span>
                </div>
                <div class="desc">
                  <span>启动时间: </span>
                  <el-time-picker style="width: 120px" v-model="equip.openTime" :editable="false" :clearable="false"
                                  format="HH:mm" value-format="HH:mm" :picker-options="{format: 'HH:mm'}"
                                  placeholder="任意时间点" @change="timeChange(equip, 'open')">
                  </el-time-picker>
                </div>
                <div class="desc">
                  <span>结束时间: </span>
                  <el-time-picker style="width: 120px" v-model="equip.closeTime" :editable="false" :clearable="false"
                                  format="HH:mm" value-format="HH:mm" :picker-options="{format: 'HH:mm'}"
                                  placeholder="任意时间点" @change="timeChange(equip, 'close')">
                  </el-time-picker>
                </div>
                <div class="desc">
                  <span>运行时间: </span>
                  <el-input placeholder="运行时间" style="width: 80px" v-model="equip.runTime" prefix-icon="el-icon-timer" :disabled="true"/>
                  <el-button type="primary" plain @click="minChange(equip, 'run')">修改</el-button>
                </div>
                <div class="desc">
                  <span>停止时间: </span>
                  <el-input placeholder="运行时间" style="width: 80px" v-model="equip.stopTime" prefix-icon="el-icon-timer" :disabled="true"/>
                  <el-button type="primary" plain @click="minChange(equip, 'stop')">修改</el-button>
                </div>
              </el-card>
            </el-col>
<!--            <el-table :data="devicedata" style="width: 100%" height="300" border stripe>-->
<!--              <el-table-column prop="name" label="设备名称" align="center" width="160"></el-table-column>-->
<!--              <el-table-column prop="手自动反馈" label="手自动反馈" align="center" width="100"></el-table-column>-->
<!--              <el-table-column prop="运行反馈" label="运行反馈" align="center" width="100"></el-table-column>-->
<!--              <el-table-column prop="故障反馈" label="故障反馈" align="center" width="100"></el-table-column>-->
<!--              <el-table-column prop="手自动开关" label="手动/自动" align="center" width="160">-->
<!--                <template slot-scope="scope">-->
<!--                  <el-switch-->
<!--                      v-if="scope.row.手自动开关 !== undefined"-->
<!--                      v-model="scope.row.手自动开关"-->
<!--                      active-text="自动"-->
<!--                      inactive-text="手动"-->
<!--                      @change="autoChange(scope)">-->
<!--                  </el-switch>-->
<!--                </template>-->
<!--              </el-table-column>-->
<!--              <el-table-column prop="启停开关" label="启动/停止" align="center" min-width="190">-->
<!--                <template slot-scope="scope">-->
<!--                  <el-button v-if="scope.row.手自动开关 === false && scope.row.运行反馈 === '停止'" type="primary" plain @click="startEquipBtn(scope)">启动</el-button>-->
<!--                  <el-button v-if="scope.row.手自动开关 === true || scope.row.运行反馈 === '运行'" type="primary" disabled plain @click="startEquipBtn(scope)">启动</el-button>-->
<!--                  <el-button v-if="scope.row.手自动开关 === false && scope.row.运行反馈 === '运行'" type="primary" plain @click="stopEquipBtn(scope)">停止</el-button>-->
<!--                  <el-button v-if="scope.row.手自动开关 === true || scope.row.运行反馈 === '停止'" type="primary" disabled plain @click="stopEquipBtn(scope)">停止</el-button>-->
<!--                </template>-->
<!--              </el-table-column>-->
<!--            </el-table>-->
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-dialog title="时间调整" :visible.sync="dialogVisible" width="30%">
      <el-slider v-model="sliderValue" :min="sliderMin" :max="sliderMax"/>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="sendMinutes()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  // import {equipName} from '../../js/site_detail'
  export default {
    name: 'remote_control',
    data () {
      this.getEquipMonitor()
      return {
        imgUrl: 'http://43.228.77.195:8083/pic/EquipState.png',
        devicedata: [],
        sliderData: [], // 设置频率滑动条json数组
        // 1.14新增
        // 时间拖动条相关
        dialogVisible: false,
        dialogName: 'null',
        sliderValue: 0,
        sliderMin: 0,
        sliderMax: 100
      }
    },
    computed: {
      refreshPage () {
        return this.$store.state.Treedata.chooseData
      }
    },
    watch: {
      refreshPage: function () {
        this.$router.replace({
          path: '@/components/page/skip/control_skip',
          name: 'control_skip'
        })
      }
    },
    methods: {
      setTimer () {
        if (this.timer === null) {
          this.timer = setInterval(this.getEquipMonitor, 1000)
        }
      },
      // 获取监控的数据 会通过计算进行变动
      // args: apiBaseUrl boxNo
      // 参数根据equipmentobjarray和vuex中存储的下标进行计算
      getEquipMonitor () {
        let index = this.$store.state.Treedata.chooseData
        console.log(index)
        let authorization = 'Bearer ' + window.jsonobj['access_token']
        let apiBaseUrl = window.equipmentobjarray[index]['box']['cs']['apiBaseUrl']
        let boxNo = window.equipmentobjarray[index]['box']['boxNo']
        this.$http.post('http://43.228.77.195:8082/equip/getEquipMonitor', {
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
          console.log('====equipDataArray')
          console.log(equipDataArray)
          this.getEquipValue(equipDataArray, 'device')
          // 请求频率设备控制数据 (中贸设备不存在频率计)
          // let rateControlNameArray = findRateName(data)
          // console.log(rateControlNameArray)
          // this.getEquipValue(rateControlNameArray, 'rate')
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
        this.$http.post('http://43.228.77.195:8082/equip/getEquipValue', {
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
            let index = this.$store.state.Treedata.chooseData
            let stationName = window.equipmentobjarray[index]['alias']
            this.devicedata = formatDeviceData(values, dataArray, stationName)
            console.log(this.devicedata)
          } else if (type === 'rate') {
            this.sliderData = formatSliderData(values)
          }
        })
      },
      // autoChange (scope) {
      //   console.log(scope)
      //   let key = '手自动开关'
      //   let name = scope['row']['name']
      //   let value = 0
      //   if (scope['row']['手自动开关'] === true) {
      //     value = 1
      //   }
      //   this.$confirm('此操作将修改设备' + name + '的值, 是否继续?', '提示', {
      //     confirmButtonText: '确定',
      //     cancelButtonText: '取消',
      //     type: 'warning'
      //   }).then(() => {
      //     this.setEquipValue(formatName(key, name), 0, value)
      //   }).catch(() => {
      //     scope['row']['手自动开关'] = !scope['row']['手自动开关']
      //     this.$message({
      //       type: 'info',
      //       message: '已取消修改'
      //     })
      //   })
      // },
      autoChange (scope) {
        console.log(scope)
        let key = '手自动开关'
        let name = scope['name']
        let value = 0
        if (scope['maSwitch'] === true) {
          value = 1
        }
        this.$confirm('此操作将修改设备' + name + '的值, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.setEquipValue(formatName(key, name), 0, value)
        }).catch(() => {
          scope['手自动开关'] = !scope['手自动开关']
          this.$message({
            type: 'info',
            message: '已取消修改'
          })
        })
      },
      // startEquipBtn (scope) {
      //   console.log(scope)
      //   let index = this.$store.state.Treedata.chooseData
      //   let stationName = window.equipmentobjarray[index]['alias']
      //   // 在fbox设备中,只有学院实训台启停开关是分开的
      //   let key = (stationName === '软件学院实训台' ? '启动开关' : '启停开关')
      //   let name = scope['row']['name']
      //   let keyName = formatName(key, name)
      //   let type = 0
      //   let value = 1
      //   this.$confirm('此操作将启动设备' + name + ', 是否继续?', '提示', {
      //     confirmButtonText: '确定',
      //     cancelButtonText: '取消',
      //     type: 'warning'
      //   }).then(() => {
      //     this.setEquipValue(keyName, type, value)
      //   }).catch(() => {
      //     scope['row']['启停开关'] = !scope['row']['启停开关']
      //     this.$message({
      //       type: 'info',
      //       message: '已取消修改'
      //     })
      //   })
      // },
      // stopEquipBtn (scope) {
      //   console.log(scope)
      //   let index = this.$store.state.Treedata.chooseData
      //   let stationName = window.equipmentobjarray[index]['alias']
      //   // 在fbox设备中,只有学院实训台启停开关是分开的
      //   let key = (stationName === '软件学院实训台' ? '停止开关' : '启停开关')
      //   let name = scope['row']['name']
      //   let keyName = formatName(key, name)
      //   let type = 0
      //   // 在fbox设备中,只有学院实训台启停开关是分开的,其他设备的停止是置0
      //   let value = (stationName === '软件学院实训台' ? 1 : 0)
      //   this.$confirm('此操作将关闭设备' + name + ', 是否继续?', '提示', {
      //     confirmButtonText: '确定',
      //     cancelButtonText: '取消',
      //     type: 'warning'
      //   }).then(() => {
      //     this.setEquipValue(keyName, type, value)
      //   }).catch(() => {
      //     this.$message({
      //       type: 'info',
      //       message: '已取消修改'
      //     })
      //   })
      // },
      // 1.14新启停控制
      startEquipBtn (scope) {
        console.log(scope)
        let index = this.$store.state.Treedata.chooseData
        let stationName = window.equipmentobjarray[index]['alias']
        // 在fbox设备中,只有学院实训台启停开关是分开的
        let key = (stationName === '云南大学测试平台' ? '启动开关' : '启停开关')
        let name = scope['name']
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
          this.$message({
            type: 'info',
            message: '已取消修改'
          })
        })
      },
      stopEquipBtn (scope) {
        console.log(scope)
        let index = this.$store.state.Treedata.chooseData
        let stationName = window.equipmentobjarray[index]['alias']
        // 在fbox设备中,只有学院实训台启停开关是分开的
        let key = (stationName === '云南大学测试平台' ? '停止开关' : '启停开关')
        let name = scope['name']
        let keyName = formatName(key, name)
        let type = 0
        // 在fbox设备中,只有学院实训台启停开关是分开的,其他设备的停止是置0
        let value = (stationName === '云南大学测试平台' ? 1 : 0)
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
        this.$http.post('http://43.228.77.195:8082/equip/setEquipValue', {
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
          setTimeout(() => {
            this.$router.replace({
              path: '@/components/page/skip/control_skip',
              name: 'control_skip'
            })
          }, 1000)
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
      },
      // 1.14 新增
      // 启停时间控制
      timeChange (scope, type) {
        console.log(scope)
        let label = type === 'open' ? '开机时间' : '关机时间'
        let name = scope['name']
        let newTime = type === 'open' ? scope['openTime'] : scope['closeTime']
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
      /*
       * 1.14 新增
       * 关于运行时间的修改
       * 先显示时间拖动条
       * 再提示是否确认修改
       */
      minChange (scope, type) {
        console.log(scope)
        this.dialogVisible = true
        let startTime = scope.openTime
        let endTime = scope.closeTime
        // 减去相异时间
        let subMinutes = type === 'run' ? scope.stopTime : scope.runTime
        // 计算时间差 A+B-C
        this.sliderMax = calcTimes(startTime, endTime, Number(subMinutes))
        console.log('slider max:' + this.sliderMax)
        this.sliderValue = type === 'run' ? scope.runTime : scope.stopTime // 当前列分钟
        this.dialogName = '定时_' + scope.name + '_' + (type === 'run' ? '运行时间' : '停止时间')
      },
      sendMinutes () {
        // 发送数据后把所有数据还原初始值
        console.log(this.sliderValue)
        this.setEquipValue(this.dialogName, 0, this.sliderValue)
        this.dialogVisible = false
      }
    },
    created () {
      clearInterval(this.timer)
      this.timer = null
      // 开始定时器
      // this.setTimer()
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
   * 1.14 新增时间设备获取
   */
  function findEquipName (data) {
    let indexState = -1
    let indexRemote = -1
    let indexTime = -1
    for (let i = 0; i < data.length; i++) {
      if (data[i]['name'] === '状态') {
        indexState = i
      } else if (data[i]['name'] === '远程') {
        indexRemote = i
      } else if (data[i]['name'] === '定时') {
        indexTime = i
      }
    }
    let newData = []
    if (indexState !== -1 && indexRemote !== -1 && indexTime !== -1) {
      // 组合数组
      newData = data[indexState]['items'].concat(data[indexRemote]['items']).concat(data[indexTime]['items'])
    } else if (indexState !== -1 && indexRemote !== -1 && indexTime === -1) {
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
  function formatDeviceData (datas, labelArray, name) {
    console.log(datas)
    let firstMergeArray = []
    for (let i = 0; i < datas.length; i++) {
      let originName = datas[i]['name']
      // 分割字符串名
      let names = originName.split('_')
      let deviceName = names[1]
      let labelName = names[2]
      let label = getDeviceLabel(labelArray, datas, i, labelName)
      // 组装数据
      let index = getDeviceIndex(firstMergeArray, deviceName)
      if (index === -1) {
        firstMergeArray.push({'name': deviceName, [labelName]: label})
      } else {
        firstMergeArray[index][labelName] = label
      }
    }
    console.log(firstMergeArray)
    // 1.14 新增时间数据
    // 格式化deviceArray以满足需求
    if (name !== '云南大学测试平台') {
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
          stopTime: firstMergeArray[i]['停止时间'],
          rsSwitch: firstMergeArray[i]['启停开关'],
          maSwitch: firstMergeArray[i]['手自动开关'],
          errorFeedback: firstMergeArray[i]['故障反馈'],
          runFeedback: firstMergeArray[i]['运行反馈']
        }
        secondMergeArray.push(obj)
      }
      // 1.14 新增设备筛选逻辑
      let thirdArray = []
      for (let i = 0; i < secondMergeArray.length; i++) {
        if (secondMergeArray[i].name !== '急停' && secondMergeArray[i].name !== '液位' &&
          secondMergeArray[i].name !== '机柜') {
          thirdArray.push(secondMergeArray[i])
        }
      }
      return thirdArray
    } else {
      let ynuArray = []
      for (let i = 0; i < firstMergeArray.length; i++) {
        let rsFlag = firstMergeArray[i]['启动开关'] === true
        let obj = {
          name: firstMergeArray[i]['name'],
          rsSwitch: rsFlag,
          maSwitch: firstMergeArray[i]['手自动开关'],
          errorFeedback: firstMergeArray[i]['故障反馈'],
          runFeedback: firstMergeArray[i]['运行反馈']
        }
        ynuArray.push(obj)
      }
      return ynuArray
    }
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
      // 暂时保留
      // 1.14 增加传值
      label = value
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
  function calcTimes (startTime, endTime, subMinutes) {
    let sTimes = startTime.split(':')
    let eTimes = endTime.split(':')
    let shour = Number(sTimes[0])
    let smins = Number(sTimes[1])
    let ehour = Number(eTimes[0])
    let emins = Number(eTimes[1])
    let res = (ehour - shour) * 60 + (emins - smins) - subMinutes
    return res > 60 ? 60 : res
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
  .desc {
    padding: 10px 0;
  }
  .box-card {
    width: 330px;
  }
</style>
