<template>
  <div>
    <div align="right">
      <el-button type="primary" size="medium" @click="addSite_monitorVisible = true">添加</el-button>
    </div>
    <el-dialog title="添加站点监控" :visible.sync="addSite_monitorVisible"  width="30%">
      <el-form :model="addMonitor" ref="addMonitor" label-width="150px">
        <el-form-item label="站点名称：" prop="siteName" :label-width="formLabelWidth">{{addMonitor.siteName}}</el-form-item>
        <el-form-item label="地址：" prop="address" :label-width="formLabelWidth">{{addMonitor.address}}</el-form-item>
        <el-form-item label="监控器名称：" :label-width="formLabelWidth" prop="monitorName">
          <el-input v-model="addMonitor.monitorName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="监控器接入地址：" :label-width="formLabelWidth" prop="url">
          <el-input v-model="addMonitor.url" autocomplete="off"></el-input>
        </el-form-item>
        <el-row>
          <el-col :span="4" :offset="10">
            <el-form-item>
              <el-button type="primary" @click="onAddSubmit('addMonitor')">提交</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item>
              <el-button @click="resetAddForm('addMonitor')">重置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-dialog>
    <br />

    <el-card class="box-card">
      <el-table :data="monitors" style="width: 100%">
        <el-table-column label="监控站点" width="240">
          <template slot-scope="scope">
            <el-button type="text" @click="clickVideo(scope.$index, scope.row)">{{scope.row.monitorName}}</el-button>
            <el-dialog title="监控视频" :visible.sync="videoVisible" @close="closeVideo">
              <!-- <iframe
                src="http://hls01open.ys7.com/openlive/5d0b898798c14aafa5cc8e7865e519e3.hd.m3u8"
                width="600"
                height="400"
                id="ysOpenDevice"
                allowfullscreen
              ></iframe> -->
              <div class="video_con">
                <video controls class="myVideo" ref="video"></video>
              </div>
            </el-dialog>
          </template>
        </el-table-column>
        <el-table-column prop="siteName" label="站点"  width="250"></el-table-column>
        <el-table-column prop="address" label="地址"  width="450"></el-table-column>
        <el-table-column prop="installDate" label="安装日期" width="270"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="medium" @click="editSiteMonitor(scope.$index, scope.row)">修改</el-button>
            <el-dialog title="修改站点监控" :visible.sync="editSite_monitorVisible">
              <el-form :model="changeMonitor" :rules="rules" ref="changeMonitor">
                <el-form-item label="监控器名称：" :label-width="formLabelWidth" prop="monitorName">
                  <el-input v-model="changeMonitor.monitorName" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="站点名称：" prop="siteName" :label-width="formLabelWidth">{{changeMonitor.siteName}}</el-form-item>
                <el-form-item label="地址：" prop="address" :label-width="formLabelWidth">{{changeMonitor.address}}</el-form-item>
                <el-form-item label="监控接入地址：" prop="url" :label-width="formLabelWidth">
                  <el-input v-model="changeMonitor.url" autocomplete="off"></el-input>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer"  >
                <el-button type="primary" @click="onChangeSubmit('changeMonitor')">确 定</el-button>
                <el-button @click="resetEditForm('changeMonitor')">重 置</el-button>
              </div>
            </el-dialog>
            <el-button size="medium" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>


<script>
  let Hls = require('hls.js')
  export default {
    name: 'site_monitor',
    data () {
      return {
        currentSite: {},
        monitors: [],
        monitorSample: {
          monitorName: '',
          siteName: '',
          address: '',
          installDate: '',
          url: ''
        },
        videoVisible: false,
        addSite_monitorVisible: false,
        editSite_monitorVisible: false,
        changeMonitor: {},
        rules: {
          monitorName: [{ required: true, message: '请输入站点监控名称', trigger: 'blur' }],
          url: [{ required: true, message: '请输入站点监控接入地址', trigger: 'blur' }]
        },
        formLabelWidth: '150px',
        currentIndex: '',
        addMonitor: {},
        newMonitor: {
          name: '',
          date: '',
          url: ''
        },
        sourceUrl: 'http://hls01open.ys7.com/openlive/5d0b898798c14aafa5cc8e7865e519e3.hd.m3u8'
      }
    },
    created () {
      this.getSiteDetail()
    },
    watch: {
      refreshPage: function () {
        this.$router.replace({
          path: '@/components/page/skip/monitor_skip',
          name: 'monitor_skip'
        })
      }
    },
    computed: {
      refreshPage () {
        return this.$store.state.Treedata.chooseData
      }
    },
    methods: {
      getSiteDetail () {
        let index = this.$store.state.Treedata.chooseData
        let boxUid = window.equipmentobjarray[index].boxUid
        this.$http.post('http://182.254.148.104:8082/equip/getSiteDetail', {
          siteID: boxUid
        }, {
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(res => {
          console.log(res)
          this.currentSite = res.data
          console.log(res.data)
          this.addMonitor = Object.assign({}, this.monitorSample)
          this.addMonitor.siteName = res.data.name
          this.addMonitor.address = res.data.address
          console.log('测试')
          console.log(this.monitorSample)
          console.log(this.addMonitor)
          let monitors = []
          if (res.data.monitors === undefined) {
            res.data.monitors = []
          } else {
            if (res.data.monitors.length > 0) {
              res.data.monitors = JSON.parse(res.data.monitors)
              for (let i = 0; i < res.data.monitors.length; i++) {
                let tempMonitor = Object.assign({}, this.monitorSample)
                tempMonitor.siteName = res.data.name
                tempMonitor.address = res.data.address
                tempMonitor.monitorName = res.data.monitors[i].name
                tempMonitor.installDate = res.data.monitors[i].date
                tempMonitor.url = res.data.monitors[i].url
                monitors.push(tempMonitor)
              }
              this.monitors = monitors.slice()
            } else {
              res.data.monitors = []
            }
          }
          console.log(this.monitors)
        })
      },
      editSiteMonitor (index, row) {
        this.currentIndex = index
        console.log(index)
        this.changeMonitor = Object.assign({}, this.monitors[index])
        console.log(this.changeMonitor)
        this.editSite_monitorVisible = true
      },
      handleDelete (index, row) {
        console.log(index, row)
        this.currentSite.monitors.splice(index, 1)
        this.monitors.splice(index, 1)
        console.log(this.currentSite.monitors)
        this.$http.post('http://182.254.148.104:8082/equip/setSiteDetail', this.currentSite, {
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(res => {
          console.log(res.data.msg)
        })
      },
      onChangeSubmit (formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            console.log(this.changeMonitor)
            this.currentSite.monitors[this.currentIndex].name = this.changeMonitor.monitorName
            this.currentSite.monitors[this.currentIndex].url = this.changeMonitor.url
            console.log(this.currentIndex)
            this.$http.post('http://182.254.148.104:8082/equip/setSiteDetail', this.currentSite, {
              headers: {
                'Authorization': this.$store.state.ShiroToken.token
              }
            }).then(res => {
              console.log(res.data.msg)
              this.getSiteDetail()
              this.editSite_monitorVisible = false
            })
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },
      resetEditForm (formName) {
        this.$refs[formName].resetFields()
      },
      onAddSubmit (formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let date = new Date()
            let year = date.getFullYear()
            let mon = date.getMonth() + 1
            let day = date.getDate()
            let tempMonitor = Object.assign({}, this.newMonitor)
            tempMonitor.date = year + '-' + mon + '-' + day
            tempMonitor.name = this.addMonitor.monitorName
            tempMonitor.url = this.addMonitor.url
            this.currentSite.monitors.push(tempMonitor)
            this.$http.post('http://182.254.148.104:8082/equip/setSiteDetail', this.currentSite, {
              headers: {
                'Authorization': this.$store.state.ShiroToken.token
              }
            }).then(res => {
              console.log(res.data.msg)
              this.getSiteDetail()
              this.addSite_monitorVisible = false
            })
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },
      resetAddForm (formName) {
        this.$refs[formName].resetFields()
      },
      getStream (source) {
        if (Hls.isSupported()) {
          this.hls = new Hls()
          this.hls.loadSource(source)
          this.hls.attachMedia(this.$refs.video)
          this.hls.on(Hls.Events.MANIFEST_PARSED, () => {
            console.log('加载成功')
            this.$refs.video.play()
          })
          this.hls.on(Hls.Events.ERROR, (event, data) => {
            // console.log(event, data);
            // 监听出错事件
            console.log('加载失败')
          })
        } else if (this.$refs.video.canPlayType('application/vnd.apple.mpegurl')) {
          this.$refs.video.src = this.sourceUrl
          this.$refs.video.addEventListener('loadedmetadata', () => { this.$refs.video.play() })
        }
      },
      // 停止流
      videoPause () {
        if (this.hls) {
          this.$refs.video.pause()
          this.hls.destroy()
          this.hls = null
          console.log('关闭视频流')
        }
      },
      clickVideo (index, row) {
        this.videoVisible = true
        console.log(index, row)
        this.getStream(row.url)
      },
      closeVideo () {
        this.videoPause()
      }
    },
    beforeDestroy () {
      this.videoPause()
    }
  }
</script>

<style>
  .Monitor{
    width: 65%;
    margin-left: 20%;
  }
  .video_con{
    width: 800px;
    height: 600px;
  }
  .myVideo{
    width: 800px;
    height: 600px;
  }
</style>
