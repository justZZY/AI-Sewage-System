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
    this.getBatteryValue()
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
      let ws = new WebSocket('ws://43.228.77.195:8082/websocket')
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
    getBatteryValue () {
      let names = ['电池电压']
      let index = this.$store.state.Treedata.chooseData
      let authorization = 'Bearer ' + window.jsonobj['access_token']
      let apiBaseUrl = window.equipmentobjarray[index]['box']['cs']['apiBaseUrl']
      let boxNo = window.equipmentobjarray[index]['box']['boxNo']
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
        batteryValue = parseFloat(res.data[0].value).toFixed(2)
      })
    }
  }
}
</script>

<style scoped>
.batteryChart {
  width: 350px;
  height: 350px;
}
</style>
