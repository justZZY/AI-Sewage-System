<template>
  <div>
    <!-- 设备传感器数据 -->
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card shadow="always">
          <div slot="header">
            <span>实时监控</span>
          </div>
          <div v-for="i in monitorNums">
            <div class="line">
              <div class="lineChat" :id="i"></div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  /*
   * 在这个模块里进行数据的监控
   * 与设备控制分离
   * 初始化时获取一遍设备数据来构造起始数据
   */
  import {monitorOption} from '../../js/chart'
  const echarts = require('echarts')
  const MONITOR_WATCH_CLASS = 'monitorWatch'
  const NODE_NUM = 20
  let monitorOptionArray = []
  let monitorChartObjArray = []

  export default {
    name: 'site_realtime_data',
    created () {
      this.websocket()
    },
    beforeDestroy () {
      this.over()
    },
    watch: {
      refreshPage: function () {
        console.log('====refresh page')
        this.$router.replace({
          path: '@/components/page/skip/realtime_data_skip',
          name: 'realtime_data_skip'
        })
      }
    },
    computed: {
      rateNums: function () {
        return this.$store.state.RateNums.rateNums
      },
      monitorNums: function () {
        console.log(this.$store.state.MonitorNums.monitorNums)
        return this.$store.state.MonitorNums.monitorNums
      },
      refreshPage () {
        return this.$store.state.Treedata.chooseData
      }
    },
    mounted () {
      // 初始化图表数据
      this.getEquipData()
    },
    methods: {
      /*
       * 启动websocket连接
       */
      websocket () {
        let ws = new WebSocket('ws://127.0.0.1:8082/websocket')
        ws.onopen = () => {
          console.log('打开websocket连接')
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
      // 获取监控的数据 会通过计算进行变动
      // args: apiBaseUrl boxNo
      // 参数根据equipmentobjarray和vuex中存储的下标进行计算
      getEquipData () {
        console.log('初始化数据')
        let index = this.$store.state.Treedata.chooseData
        let authorization = 'Bearer ' + window.jsonobj['access_token']
        let apiBaseUrl = window.equipmentobjarray[index]['box']['cs']['apiBaseUrl']
        let boxNo = window.equipmentobjarray[index]['box']['boxNo']
        this.$http.post('http://127.0.0.1:8082/equip/getEquipMonitor', {
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
          let monitorNames = getTypeNames(data, '传感')
          monitorOptionArray = getInitOptions(monitorNames, monitorOption)
          this.$store.dispatch('setMonitorNums', getTypeNums(monitorNames, MONITOR_WATCH_CLASS))
          this.getEquipValue(monitorNames, 'monitor')
        })
      },
      // 获取监控寄存器的值
      getEquipValue (names, type) {
        let index = this.$store.state.Treedata.chooseData
        let authorization = 'Bearer ' + window.jsonobj['access_token']
        let apiBaseUrl = window.equipmentobjarray[index]['box']['cs']['apiBaseUrl']
        let boxNo = window.equipmentobjarray[index]['box']['boxNo']
        this.$http.post('http://127.0.0.1:8082/equip/getEquipValue', {
          authorization: authorization,
          apiBaseUrl: apiBaseUrl,
          boxNo: boxNo,
          names: names
        }, {
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(res => {
          let values = res['data']
          console.log(values)
          if (type === 'rate') {
            // 取消频率计
            // formatRateOptionArray(values)
            // this.setRateCharts()
          } else if (type === 'monitor') {
            formatMonitorOptionArray(values)
            this.setMonitorCharts()
          }
        })
      },
      setMonitorCharts () {
        // 设置折线图
        for (let i = 0; i < monitorOptionArray.length; i++) {
          let line = echarts.init(document.getElementById(MONITOR_WATCH_CLASS + i))
          line.setOption(monitorOptionArray[i], true)
          monitorChartObjArray.push(line)
        }
      },
      /*
       * 处理WebSocket参数变动数据
       * args: 启停数据 传感器数据
       * return:
       */
      solveSocketData (boxId, data) {
        // 校验是否是当前设备数据
        console.log('boxid: ' + boxId)
        let narray = data['data'].split('_')
        console.log(narray)
        let type = narray[1]
        let name = narray[2]
        if (narray[0] === boxId) {
          // 根据type类型区别更新设备值
          if (type === '传感') {
            let value1 = narray[narray.length - 2]
            let timestamp = narray[narray.length - 1]
            this.updateMonitorData(name, value1, timestamp)
          } else if (type === '频率') {
            let value2 = narray[narray.length - 2]
            // this.updateRateData(name, value2)
          }
        } else {
          return false
        }
      },
      // websocket推送数据
      updateMonitorData (name, value, timestamp) {
        for (let i = 0; i < monitorOptionArray.length; i++) {
          if (monitorOptionArray[i]['title']['text'] === name) {
            if (monitorOptionArray[i]['xAxis'][0]['data'].length > NODE_NUM) {
              monitorOptionArray[i]['xAxis'][0]['data'].shift()
            }
            if (monitorOptionArray[i]['series'][0]['data'].length > NODE_NUM) {
              monitorOptionArray[i]['series'][0]['data'].shift()
            }
            monitorOptionArray[i]['xAxis'][0]['data'].push(timestamp)
            monitorOptionArray[i]['series'][0]['data'].push(parseFloat(value).toFixed(2))
            // 更新相应的折线图表
            monitorChartObjArray[i].setOption(monitorOptionArray[i], true)
            this.setMonitorCharts()
          }
        }
      }
    }
  }
  function getTypeNums (dataSet, type) {
    let ans = []
    for (let i = 0; i < dataSet.length; i++) {
      ans.push(type + i)
    }
    return ans
  }
  /*
   * 组装模板数组数据
   * args: dataSet:设备全称
   */
  function getInitOptions (dataSet, option) {
    let ans = []
    console.log(dataSet)
    console.log(option)
    for (let i = 0; i < dataSet.length; i++) {
      // 只取设备名
      let obj = JSON.parse(JSON.stringify(option))
      obj['title']['text'] = dataSet[i].split('_')[1]
      ans.push(obj)
    }
    console.log(ans)
    return ans
  }
  // 获取相关主题index
  function getTypeNames (dataSet, type) {
    let index = -1
    let names = []
    for (let i = 0; i < dataSet.length; i++) {
      if (dataSet[i]['name'] === type) {
        index = i
      }
    }
    console.log(dataSet)
    for (let i = 0; i < dataSet[index]['items'].length; i++) {
      names.push(dataSet[index]['items'][i]['name'])
    }
    return names
  }
  function formatMonitorOptionArray (dataSet) {
    for (let i = 0; i < monitorOptionArray.length; i++) {
      monitorOptionArray[i]['xAxis'][0]['data'].push(dataSet[i]['timestamp'])
      let names = dataSet[i]['name'].split('_')
      monitorOptionArray[i]['yAxis'][0]['name'] = names[names.length - 1]
      monitorOptionArray[i]['series'][0]['data'].push(dataSet[i]['value'].toFixed(2))
    }
  }
</script>

<style scoped>
  .line {
    width: 272px;
    float: left;
    overflow: hidden;
  }
  .lineChat {
    width: 100%;
    height: 260px;
  }
</style>
