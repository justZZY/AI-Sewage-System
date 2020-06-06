<template>
  <div id="app">
    <router-view></router-view>
    <el-dialog
        title="正在更新新版本,请稍候..."
        :visible.sync="dialogVisible"
        width="60%"
        :close-on-click-modal="closeOnClickModal"
        :close-on-press-escape="closeOnPressEscape"
        :show-close="showClose"
        center
    >
      <div style="width:100%;height:20vh;line-height:20vh;text-align:center">
        <el-progress
            status="success"
            :text-inside="true"
            :stroke-width="20"
            :percentage="percentage"
            :width="strokeWidth"
            :show-text="true"
        ></el-progress>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  let ipcRenderer = require('electron').ipcRenderer
  let _this = this
  // 接收主进程版本更新消息
  ipcRenderer.on('message', (event, arg) => {
    // for (var i = 0 i < arg.length i++) {
    console.log(arg)
    if (arg.cmd === 'update-available') {
      // 显示升级对话框
      _this.dialogVisible = true
    } else if (arg.cmd === 'download-progress') {
      // 更新升级进度
      /**
       *
       * message{bytesPerSecond: 47673
      delta: 48960
      percent: 0.11438799862426002
      total: 42801693
      transferred: 48960
      }
       */
      console.log(arg.message.percent)
      _this.percentage = Math.round(parseFloat(arg.message.percent))
    } else if (arg.cmd === 'error') {
      _this.dialogVisible = false
      _this.$message('更新失败')
    }
    // }
  })
  // 5秒后开始检测新版本
  let timeOut = window.setTimeout(() => {
    ipcRenderer.send('checkForUpdate')
  }, 5000)
  clearTimeout()
  // 间隔1小时检测一次
  let interval = window.setInterval(() => {
    ipcRenderer.send('checkForUpdate')
  }, 3600000)

  export default {
    name: 'ai-sewage-system',
    data () {
      return {
        dialogVisible: false,
        closeOnClickModal: false,
        closeOnPressEscape: false,
        showClose: false,
        percentage: 0,
        strokeWidth: 200
      }
    },
    mounted () {
      _this = this
    },
    destroyed () {
      window.clearInterval(interval)
      window.clearInterval(timeOut)
    }
  }
</script>

<style>
  /* CSS */
</style>
