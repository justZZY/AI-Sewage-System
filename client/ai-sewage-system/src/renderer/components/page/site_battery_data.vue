<template>
  <div>
    <!--  设备电源信息  -->
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card shadow="always">
          <div slot="header">
            <span>电池信息</span>
          </div>
          <div>
            <el-col :span="8">
              <!--  实时电压  -->
              <el-card shadow="always">
                <div slot="header">
                  <span>实时电压</span>
                </div>
                <div>
                  <div class="batteryChart" id="batteryChart"></div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="16">
              <el-card shadow="always">
                <div slot="header">
                  <span>电压控制</span>
                </div>
                <div>
                  <el-table :data="batteryTableList" stripe style="width: 100%">
                    <el-table-column prop="name" label="电源控制项"></el-table-column>
                    <el-table-column prop="value" label="设置值"></el-table-column>
                    <el-table-column label="操作">
                      <template slot-scope="scope">
                        <el-button size="mini" type="danger" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                </div>
              </el-card>
            </el-col>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {rateOption} from '../../js/chart'
const echarts = require('echarts')
let batteryChart = {}
let batteryValue = 0
let batteryOption = {}

export default {
  name: 'site_battery_data',
  data () {
    return {
      batteryTableList: []
    }
  },
  watch: {
    refreshPage: function () {
      this.$router.replace({
        path: '@/components/page/skip/battery_data_skip',
        name: 'battery_data_skip'
      })
    }
  },
  computed: {
    refreshPage () {
      return this.$store.state.Treedata.chooseData
    }
  },
  created () {
    this.getBatteryNames()
  },
  beforeDestroy () {
    this.over()
  },
  mounted () {
    this.drawBatteryChart()
    this.websocket()
  },
  methods: {
    drawBatteryChart () {
      batteryChart = echarts.init(document.getElementById('batteryChart'))
      batteryOption = JSON.parse(JSON.stringify(rateOption))
      console.log(batteryOption)
      batteryOption.title.text = '电池电压'
      batteryOption.series[0].max = 180
      batteryOption.series[0].detail.formatter = '{value}V'
      batteryOption.series[0].data[0].value = batteryValue
      batteryChart.setOption(batteryOption, true)
    },
    /*
     * 用来获取实时电池电压信息
     */
    websocket () {
      let ws = new WebSocket('ws://182.254.148.104:8082/websocket')
      ws.onopen = () => {
        console.log('打开电池websocket连接')
      }
      ws.onmessage = res => {
        let index = this.$store.state.Treedata.chooseData
        let boxId = window.equipmentobjarray[index]['box']['id']
        // 做数据解析
        this.solveSocketData(boxId, res)
      }
      ws.onclose = () => {
        console.log('连接已关闭')
      }
      // 界面跳转websocket关闭函数
      this.over = () => {
        ws.close()
      }
    },
    solveSocketData (boxID, message) {
      let narray = message.data.split('_')
      if (narray[0] === boxID && narray[1] === '电池电压') {
        console.log(narray)
        batteryValue = parseFloat(narray[2]).toFixed(2)
        batteryOption.series[0].data[0].value = batteryValue
        batteryChart.setOption(batteryOption, true)
      }
    },
    // 获取电池设备信息
    getBatteryNames () {
      console.log('获取电池信息')
      let index = this.$store.state.Treedata.chooseData
      let authorization = 'Bearer ' + window.jsonobj['access_token']
      let apiBaseUrl = window.equipmentobjarray[index]['box']['cs']['apiBaseUrl']
      let boxNo = window.equipmentobjarray[index]['box']['boxNo']
      this.$http.post('http://182.254.148.104:8082/equip/getEquipMonitor', {
        authorization: authorization,
        apiBaseUrl: apiBaseUrl,
        boxNo: boxNo
      }, {
        headers: {
          'Authorization': this.$store.state.ShiroToken.token
        }
      }).then(res => {
        // 取电池信息
        this.getBatteryValue(getNamesFromData(res['data']))
      })
    },
    getBatteryValue (names) {
      let index = this.$store.state.Treedata.chooseData
      let authorization = 'Bearer ' + window.jsonobj['access_token']
      let apiBaseUrl = window.equipmentobjarray[index]['box']['cs']['apiBaseUrl']
      let boxNo = window.equipmentobjarray[index]['box']['boxNo']
      this.$http.post('http://182.254.148.104:8082/equip/getEquipValue', {
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
        // 获取除去电池信息的设备值列表
        this.batteryTableList = getBatteryValueBesideRate(res.data)
      })
    },
    handleEdit (index, row) {
      this.$prompt('请输入修改的整数值', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /(^[1-9]\d*$)/,
        inputErrorMessage: '数值格式不正确'
      }).then(({value}) => {
        if (Number(value) !== row.value) {
          // 提交到远程服务器
          this.setEquipValue(row.name, 0, value)
        } else {
          this.$message.error('与修改前数值相同，取消修改')
        }
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消输入'
        })
      })
    },
    // 通用请求接口
    setEquipValue (name, type, value) {
      let index = this.$store.state.Treedata.chooseData
      let authorization = 'Bearer ' + window.jsonobj['access_token']
      let apiBaseUrl = window.equipmentobjarray[index]['box']['cs']['apiBaseUrl']
      let boxNo = window.equipmentobjarray[index]['box']['boxNo']
      this.$http.post('http://182.254.148.104:8082/equip/setEquipValue', {
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
            path: '@/components/page/skip/battery_data_skip',
            name: 'battery_data_skip'
          })
        }, 1000)
      })
    }
  }
}

function getNamesFromData (dataList) {
  let batteryList = []
  let names = []
  for (let i = 0; i < dataList.length; i++) {
    if (dataList[i].name === '电池') {
      batteryList = dataList[i].items
    }
  }
  for (let batteryListKey in batteryList) {
    if (batteryList.hasOwnProperty(batteryListKey)) {
      names.push(batteryList[batteryListKey].name)
    }
  }
  return names
}

function getBatteryValueBesideRate (dataList) {
  let ans = []
  for (const i in dataList) {
    if (dataList.hasOwnProperty(i) && dataList[i].name !== '电池电压') {
      ans.push({name: dataList[i].name, value: dataList[i].value})
    }
  }
  return ans
}
</script>

<style scoped>
.batteryChart {
  width: 350px;
  height: 350px;
}
</style>
