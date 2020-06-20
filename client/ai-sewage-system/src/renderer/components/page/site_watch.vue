<template>
  <div>
    <!-- 站点详情 -->
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card shadow="always">
          <div slot="header">
            <span>站点详情</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="onSiteClick">编辑信息</el-button>
          </div>
          <div>
            <el-row :gutter="10">
              <el-col :span="24">
                <el-carousel :interval="4000" type="card" height="300px">
                  <el-carousel-item v-for="item in siteForm.uploadImgIDArray" :key="item" >
                    <img :src="'http://127.0.0.1:8082/file/download/'+item" style="width:100%">
                  </el-carousel-item>
                </el-carousel>
              </el-col>
            </el-row>
            <el-row type="flex" justify="center">
              <el-col :span="18">
                <el-table :data="table" style="width: 100%" :show-header="false">
                  <el-table-column prop="name1" width="100"/>
                  <el-table-column prop="value1" width="180">
                    <template scope="scope">
                      <span style="color: blue">{{scope.row.value1}}</span>
                    </template>
                  </el-table-column>
                  <el-table-column prop="name2" width="100"/>
                  <el-table-column prop="value2" width="180">
                    <template scope="scope">
                      <span style="color: blue">{{scope.row.value2}}</span>
                    </template>
                  </el-table-column>
                </el-table>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 用于编辑站点信息 -->
    <el-dialog title="编辑信息" :visible.sync="dialogSiteFormVisible">
      <el-row :gutter="10">
        <el-col :span="24">
          <el-col :span="10">
            <el-upload action="http://127.0.0.1:8082/file/singleupload"
                       list-type="picture-card"
                       :before-upload="handleBeforeUpload"
                       :on-remove="handleRemove"
                       :on-error="handleError"
                       :on-success="handleImgSuccess"
                       :on-preview="handlePictureCardPreview"
                       :headers="{ 'Authorization': this.$store.state.ShiroToken.token }">
              <i class="el-icon-plus"></i>
              <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过5M</div>
            </el-upload>
            <el-dialog :visible.sync="dialogPreVisible">
              <img width="100%" :src="dialogPreImageUrl" alt="">
            </el-dialog>
          </el-col>
          <el-col :span="14">
            <el-form ref="siteForm" :model="siteForm" label-width="120px">
              <el-form-item label="站点编号">
                <el-input v-model="siteForm.id" disabled></el-input>
              </el-form-item>
              <el-form-item label="站点名称">
                <el-input v-model="siteForm.name"></el-input>
              </el-form-item>
              <el-form-item label="站点地址">
                <el-input v-model="siteForm.address"></el-input>
              </el-form-item>
              <el-form-item label="安装时间">
                <el-date-picker
                    v-model="siteForm.date"
                    type="date"
                    placeholder="选择日期"
                    format="yyyy 年 MM 月 dd 日"
                    value-format="yyyy年MM月dd日">
                </el-date-picker>
              </el-form-item>
              <el-form-item label="站点经度">
                <el-input v-model="siteForm.longitude"></el-input>
              </el-form-item>
              <el-form-item label="站点纬度">
                <el-input v-model="siteForm.latitude"></el-input>
              </el-form-item>
              <el-form-item label="站点类型">
                <el-input v-model="siteForm.type"></el-input>
              </el-form-item>
              <el-form-item label="设备处理量">
                <el-input v-model="siteForm.efficiency"></el-input>
              </el-form-item>
              <el-form-item label="处理工艺">
                <el-input v-model="siteForm.process"></el-input>
              </el-form-item>
              <el-form-item label="出水标准">
                <el-input v-model="siteForm.standard"></el-input>
              </el-form-item>
              <el-form-item label="运维人员">
                <el-input v-model="siteForm.operator"></el-input>
              </el-form-item>
              <el-form-item label="联系电话">
                <el-input v-model="siteForm.phone"></el-input>
              </el-form-item>
              <el-form-item label="风机参数">
                <el-input v-model="siteForm.fan"></el-input>
              </el-form-item>
              <el-form-item label="提升泵参数">
                <el-input v-model="siteForm.boost"></el-input>
              </el-form-item>
              <el-form-item label="回流泵参数">
                <el-input v-model="siteForm.reflux"></el-input>
              </el-form-item>
              <el-form-item label="罐体参数">
                <el-input v-model="siteForm.can"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="onSiteSubmit">立即修改</el-button>
                <el-button @click="onSiteCancel">取消</el-button>
              </el-form-item>
            </el-form>
          </el-col>
        </el-col>
      </el-row>
    </el-dialog>
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
    <!-- 设备频率数据 -->
    <!-- 正式设备并不支持 取消频率计-->
<!--    <el-row :gutter="20">-->
<!--      <el-col :span="24">-->
<!--        <el-card shadow="always">-->
<!--          <div slot="header">-->
<!--            <span>频率监控</span>-->
<!--          </div>-->
<!--          <div v-for="i in rateNums">-->
<!--            <div class="gauge">-->
<!--              <div class="gaugeChart" :id="i"></div>-->
<!--            </div>-->
<!--          </div>-->
<!--        </el-card>-->
<!--      </el-col>-->
<!--    </el-row>-->
  </div>
</template>

<script>
  /*
   * 在这个模块里进行数据的监控
   * 与设备控制分离
   * 初始化时获取一遍设备数据来构造起始数据
   */
  import {monitorOption, rateOption, barOption} from '../../js/chart'
  // import {siteDetail} from '../../js/site_detail' 2020.06.20 改为从服务器获取
  const echarts = require('echarts')
  const MONITOR_WATCH_CLASS = 'monitorWatch'
  // const RATE_WATCH_CLASS = 'rateWatch'
  const NODE_NUM = 20
  // let rateOptionArray = [] 取消频率设备
  let monitorOptionArray = []
  // let rateChartObjArray = []
  let monitorChartObjArray = []
  export default {
    name: 'site_watch',
    data () {
      return {
        siteForm: {},
        table: [],
        tabPosition: 'left',
        dialogSiteFormVisible: false,
        dialogPreVisible: false,
        dialogPreImageUrl: '',
        waitMergeImgArray: []
      }
    },
    created () {
      this.websocket()
      this.siteGet()
    },
    beforeDestroy () {
      this.over()
    },
    computed: {
      rateNums: function () {
        return this.$store.state.RateNums.rateNums
      },
      monitorNums: function () {
        return this.$store.state.MonitorNums.monitorNums
      },
      refreshPage () {
        return this.$store.state.Treedata.chooseData
      }
    },
    watch: {
      refreshPage: function () {
        this.$router.replace({
          path: '@/components/page/skip/site_skip',
          name: 'site_skip'
        })
      }
    },
    mounted () {
      // 初始化图表数据
      this.getEquipData()
      this.setDataRecordCharts()
    },
    methods: {
      /*
       * 获取远程json数据
       * 查找指定key的数据
       */
      siteGet () {
        let index = this.$store.state.Treedata.chooseData
        let boxUid = window.equipmentobjarray[index].boxUid
        console.log(boxUid)
        // 2020.06.20 通过远程获取修改信息
        // let site = siteDetail.find(s => s.id === Number(boxUid))
        this.$http.post('http://127.0.0.1:8082/equip/getSiteDetail', {
          siteID: boxUid
        }, {
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(res => {
          console.log(res)
          this.siteForm = res.data
          this.table = tableGet(this.siteForm)
        })
      },
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
          // 请求数据 取消频率计
          // let rateNames = getTypeNames(data, '频率')
          let monitorNames = getTypeNames(data, '传感')
          // 初始化仪表盘模板 取消频率计
          // rateOptionArray = getInitOptions(rateNames, rateOption)
          monitorOptionArray = getInitOptions(monitorNames, monitorOption)
          // 构造类名数组
          // this.$store.dispatch('setRateNums', getTypeNums(rateNames, RATE_WATCH_CLASS))
          this.$store.dispatch('setMonitorNums', getTypeNums(monitorNames, MONITOR_WATCH_CLASS))
          // this.getEquipValue(rateNames, 'rate')
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
      // 设置所有仪表 取消频率计
      // setRateCharts () {
      //   // 设置仪表盘
      //   for (let i = 0; i < rateOptionArray.length; i++) {
      //     let chart = echarts.init(document.getElementById(RATE_WATCH_CLASS + i))
      //     chart.setOption(rateOptionArray[i], true)
      //     rateChartObjArray.push(chart)
      //   }
      // },
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
            this.updateRateData(name, value2)
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
      },
      // 取消频率计设备 2020.06.19
      // updateRateData (name, value) {
      //   for (let i = 0; i < rateOptionArray.length; i++) {
      //     if (rateOptionArray[i]['title']['text'] === name) {
      //       rateOptionArray[i]['series'][0]['data'][0]['value'] = parseFloat(value).toFixed(2)
      //       // 更新相应的仪表盘
      //       console.log(rateOptionArray[i])
      //       rateChartObjArray[i].setOption(rateOptionArray[i], true)
      //       this.setRateCharts()
      //     }
      //   }
      // }
      onSiteClick () {
        this.dialogSiteFormVisible = true
        this.waitMergeImgArray = []
      },
      handleDropDownCommand (command) {
        this.$message('click on item ' + command)
      },
      handleBeforeUpload (file) {
        console.log(file)
        if (file.size > 5452595) {
          this.$message.error('文件大小不能超过5M')
          return false
        }
        if (!file.type.toLowerCase().startsWith('image')) {
          this.$message.error('只能上传图像文件')
          return false
        }
      },
      handleRemove (file, fileList) {
        this.siteForm.uploadImgIDArray.forEach((item, index, arr) => {
          if (item === file.response.data) {
            arr.splice(index, 1)
          }
        })
        console.log(this.waitMergeImgArray)
      },
      handleImgSuccess (result, file) {
        if (result.code === 1) {
          let imgID = result.data
          this.waitMergeImgArray.push(imgID)
          console.log(this.waitMergeImgArray)
        } else {
          this.$message.error('服务器异常, 上传失败')
        }
      },
      handlePictureCardPreview (file) {
        this.dialogPreImageUrl = file.url
        this.dialogPreVisible = true
      },
      handleError (rr, file, fileList) {
        console.log(rr)
        console.log(file)
        console.log(fileList)
        this.$message.error('上传失败')
      },
      onSiteSubmit () {
        console.log(this.siteForm)
        this.siteForm.uploadImgIDArray = this.waitMergeImgArray
        this.$http.post('http://127.0.0.1:8082/equip/setSiteDetail', this.siteForm, {
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(res => {
          console.log(res)
          if (res.data.code === 1) {
            this.dialogSiteFormVisible = false
            // 刷新一下页面
            this.$router.replace({
              path: '@/components/page/skip/site_skip',
              name: 'site_skip'
            })
          } else {
            this.$message.error('服务器异常, 修改失败')
          }
        })
      },
      onSiteCancel () {
        this.dialogSiteFormVisible = false
      },
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
  // 初始化图表数据 取消频率计
  // function formatRateOptionArray (dataSet) {
  //   for (let i = 0; i < rateOptionArray.length; i++) {
  //     rateOptionArray[i]['series'][0]['data'][0]['value'] = dataSet[i]['value'].toFixed(2)
  //   }
  // }
  function formatMonitorOptionArray (dataSet) {
    for (let i = 0; i < monitorOptionArray.length; i++) {
      monitorOptionArray[i]['xAxis'][0]['data'].push(dataSet[i]['timestamp'])
      let names = dataSet[i]['name'].split('_')
      monitorOptionArray[i]['yAxis'][0]['name'] = names[names.length - 1]
      monitorOptionArray[i]['series'][0]['data'].push(dataSet[i]['value'].toFixed(2))
    }
  }
  // 构建表单
  function tableGet (site) {
    let ans = []
    ans.push({name1: '站点名称', value1: site.name, name2: '设备处理量', value2: site.efficiency})
    ans.push({name1: '站点地址', value1: site.address, name2: '处理工艺', value2: site.process})
    ans.push({name1: '安装时间', value1: site.date, name2: '出水标准', value2: site.standard})
    ans.push({name1: '站点经度', value1: site.longitude, name2: '运维人员', value2: site.operator})
    ans.push({name1: '站点纬度', value1: site.latitude, name2: '联系电话', value2: site.phone})
    ans.push({name1: '站点类型', value1: site.type, name2: '站点编号', value2: site.id})
    ans.push({name1: '风机参数', value1: site.fan, name2: '回流泵参数', value2: site.reflux})
    ans.push({name1: '提升泵参数', value1: site.boost, name2: '罐体参数', value2: site.can})
    return ans
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
  .el-row {
    margin-bottom: 20px;
  }
  .line {
    width: 272px;
    float: left;
    overflow: hidden;
  }
  .lineChat {
    width: 100%;
    height: 260px;
  }
  .gauge {
    width: 250px;
    float: left;
    overflow: hidden;
  }
  .gaugeChart {
    width: 100%;
    height: 230px;
  }
  .el-dropdown-link {
    cursor: pointer;
    color: #409EFF;
  }
  .el-icon-arrow-down {
    font-size: 12px;
  }
</style>
