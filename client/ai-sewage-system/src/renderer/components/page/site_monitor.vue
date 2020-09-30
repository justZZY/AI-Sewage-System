<template>
  <div>
    <div align="right">
      <el-button type="primary" size="medium" @click="addSite_monitorVisible = true">添加</el-button>
    </div>
    <el-dialog title="添加站点监控器" :visible.sync="addSite_monitorVisible" class="Monitor" >
      <el-form :model="addMonitorForm" ref="addMonitorForm" label-width="150px">
        <el-row>
          <el-col :span="18">
            <el-form-item label="监控站点名称" prop="addMonitor" :rules=" [
                { required: true, message: '请输入活动名称', trigger: 'blur' }]">
              <el-select v-model="addMonitorForm.addMonitor" clearable placeholder="请选择" @change='getValue' @clear='clearSelection' >
                <el-option
                    v-for="(item,index) in siteNoMonitors"
                    :key="index"
                    :label="item.name"
                    :value="index">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="18">
            <el-form-item label="站点地址">
              {{addMonitorForm.address}}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="18">
            <el-form-item label="监控器接入地址" prop="url" :rules=" [ { required: true, message: '请输入监控器接入地址', trigger: 'blur' }]">
              <el-input v-model="addMonitorForm.url"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="4" :offset="10">
            <el-form-item>
              <el-button type="primary" @click="onAddSubmit('addMonitorForm')">提交</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item>
              <el-button @click="addSite_monitorVisible = false">取消</el-button>
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
            <el-button type="text" @click="clickVideo(scope.$index, scope.row)">{{scope.row.name}}</el-button>
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
        <el-table-column prop="address" label="地址"  width="450"></el-table-column>
        <el-table-column prop="monitorDate" label="安装日期" width="270"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="medium" @click="editSiteMonitor(scope.$index, scope.row)">修改</el-button>
            <el-dialog title="修改" :visible.sync="editSite_monitorVisible" class="Monitor">
              <el-form label-width="120px"   :model="change_monitor" ref="change_monitor">
                <el-row>
                  <el-col :span="16">
                    <el-form-item label="站点名称">{{currentName}}</el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="16">
                    <el-form-item
                      label="站点地址"
                    >{{currentAddress}}</el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="16">
                    <el-form-item label="修改监控地址" prop="monitor" :rules=" [{ required: true, message: '请输入修改监控地址', trigger: 'blur' }]">
                      <el-input v-model="change_monitor.monitor"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="onSubmit('change_monitor')">确 定</el-button>
                <el-button @click="editSite_monitorVisible = false">取 消</el-button>
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
      monitors: [],
      siteNoMonitors: [],
      videoVisible: false,
      addSite_monitorVisible: false,
      editSite_monitorVisible: false,
      change_monitor: {
        monitor: ''
      },
      currentIndex: '',
      currentName: '',
      currentAddress: '',
      addMonitorForm: {
        addMonitor: '',
        address: '',
        url: ''
      },
      sourceUrl: 'http://hls01open.ys7.com/openlive/5d0b898798c14aafa5cc8e7865e519e3.hd.m3u8'
    }
  },
  created () {
    this.getAllSiteDetail()
  },
  methods: {
    getAllSiteDetail () {
      this.$http.get('http://43.228.77.195:8082/equip/getAllSiteDetail', {
        headers: {
          'Authorization': this.$store.state.ShiroToken.token
        }
      }).then(res => {
        console.log(res)
        for (let i = 0; i < res.data.length; i++) {
          if (res.data[i].monitor === '') {
            this.siteNoMonitors.push(res.data[i])
          } else {
            this.monitors.push(res.data[i])
          }
          console.log(this.monitors)
          console.log(this.siteNoMonitors)
        }
      })
    },
    getValue () {
      console.log(this.addMonitorForm.addMonitor)
      this.addMonitorForm.address = this.siteNoMonitors[this.addMonitorForm.addMonitor].address
    },
    clearSelection () {
      this.addMonitorForm.address = ''
    },
    editSiteMonitor (index, row) {
      this.currentIndex = index
      console.log(index)
      this.currentName = this.monitors[index].name
      this.currentAddress = this.monitors[index].address
      console.log(this.currentName)
      console.log(this.currentAddress)
      console.log(this.monitors[index].monitor)
      console.log(this.monitors)
      this.editSite_monitorVisible = true
    },
    handleDelete (index, row) {
      console.log(index, row)
      this.monitors[index].monitor = ''
      let changedMonitor = this.monitors[index]
      this.monitors.splice(index, 1)
      console.log(changedMonitor)
      this.siteNoMonitors.push(changedMonitor)
      this.$http.post('http://43.228.77.195:8082/equip/setSiteDetail', changedMonitor, {
        headers: {
          'Authorization': this.$store.state.ShiroToken.token
        }
      }).then(res => {
        console.log(res.data.msg)
      })
      console.log(this.monitors)
      console.log(this.siteNoMonitors)
    },
    onSubmit (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.monitors[this.currentIndex].monitor = this.change_monitor.monitor
          let changedMonitor = this.monitors[this.currentIndex]
          console.log(this.currentIndex)
          this.$http.post('http://43.228.77.195:8082/equip/setSiteDetail', changedMonitor, {
            headers: {
              'Authorization': this.$store.state.ShiroToken.token
            }
          }).then(res => {
            console.log(res.data.msg)
            this.change_monitor.monitor = ''
            this.editSite_monitorVisible = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    onAddSubmit (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let date = new Date()
          let year = date.getFullYear()
          let mon = date.getMonth() + 1
          let day = date.getDate()
          this.addMonitorForm.monitorDate = year + '-' + mon + '-' + day
          this.siteNoMonitors[this.addMonitorForm.addMonitor].monitor = this.addMonitorForm.url
          let changedMonitor = this.siteNoMonitors[this.addMonitorForm.addMonitor]
          this.siteNoMonitors.splice(this.addMonitorForm.addMonitor, 1)
          this.monitors.push(changedMonitor)
          this.$http.post('http://43.228.77.195:8082/equip/setSiteDetail', changedMonitor, {
            headers: {
              'Authorization': this.$store.state.ShiroToken.token
            }
          }).then(res => {
            console.log(res.data.msg)
            this.addMonitorForm.addMonitor = ''
            this.addMonitorForm.url = ''
            this.addMonitorForm.address = ''
            this.addSite_monitorVisible = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
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
      this.getStream(row.monitor)
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
