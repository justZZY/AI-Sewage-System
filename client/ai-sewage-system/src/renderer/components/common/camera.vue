<!--
  使用本组件的方法
  1.在要使用本组件的父组件中添加引用：
    <camera v-on:listenToChildEvent="receiveDataFromCamera" ref="camera"></camera>
  2.在父组件中引入模块并添加组件到components
    2.1配置：import camera from './../common/camera' // 引入组件
    2.2配置：components: { camera }
  3.在父组件method中定义一个同1的方法名：如receiveDataFromCamera
    receiveDataFromCamera (data){
      console.log(data)  // 这个data就是当前拍照组件点击拍照后上传成功时返回的照片id}
    }
-->
<template>
  <el-dialog
      title="拍照"
      :visible.sync="dialogVisible"
      @close="closeCamera"
      :fullscreen=true
      :show-close=true
      width="100%"
      center>
    <div style="align-content: center;text-align: center"><!--图片展示-->
      <video ref="video"   autoplay v-show="running"></video>
      <!--canvas截取流-->
      <canvas ref="canvas" v-show="!running" style="object-fit: fill;" ></canvas>
    </div>
    <span slot="footer" class="dialog-footer">
    <el-button @click="photograph">{{running?'拍照':'重拍'}}</el-button>
    <el-button type="primary" v-if="!running" @click="uploadPhoto">上传</el-button>
  </span>
  </el-dialog>

</template>
<script>
  /**
   * 将以base64的图片url数据转换为Blob
   * @param urlData
   *            用url方式表示的base64图片数据
   */
  function convertBase64UrlToBlob (urlData) {
    let bytes = window.atob(urlData.split(',')[1]) // 去掉url的头，并转换为byte
    // 处理异常,将ascii码小于0的转换为大于0
    let ab = new ArrayBuffer(bytes.length)
    let ia = new Uint8Array(ab)
    for (let i = 0; i < bytes.length; i++) {
      ia[i] = bytes.charCodeAt(i)
    }
    let file = new File([ab], 'job_problem.png', {type: 'image/png'})
    return file
  }

  export default {
    data () {
      return {
        dialogVisible: false,
        running: true, // 是否正在拍照（video状态）
        videoSize: {
          width: {ideal: 1280, max: 1920},
          height: {ideal: 720, max: 1080}},
        zoom: 0.8 // 放大缩小倍数
      }
    },
    created: function () {
      // const {width, height} = require('electron').screen.getPrimaryDisplay().workAreaSize
      this.videoSize.width.max = window.innerWidth
      this.videoSize.height.max = window.innerHeight
    },
    // 初始化方法
    mounted: function () {
      // TODO
    },
    // 实例销毁前方法
    beforeDestroy: function () {
      console.log('page-camera 销毁')
      this.closeCamera() // 关闭摄像头
    },
    methods: {
      // 调用摄像头
      callCamera () {
        console.log('开启摄像头')
        let constraints = {
          audio: false,
          video: this.videoSize
        }
        navigator.mediaDevices.getUserMedia(constraints)
          .then(function (mediaStream) {
            this.$refs['video'].srcObject = mediaStream
            this.$refs['video'].play()
          }.bind(this)).catch(function (err) { console.log(err.name + ': ' + err.message) }) // 总是在最后检查错误
      },

      // 拍照
      photograph () {
        if (this.running) { // 未拍照，则拍
          let ctx = this.$refs['canvas'].getContext('2d')
          // 把当前视频帧内容渲染到canvas上
          this.$refs['canvas'].width = this.$refs['video'].clientWidth
          this.$refs['canvas'].height = this.$refs['video'].clientHeight
          ctx.drawImage(this.$refs['video'], 0, 0, this.$refs['canvas'].width, this.$refs['canvas'].height)
        }
        // 已拍照则重拍
        this.running = !this.running
      },
      // 开启拍照模式
      openCamera () {
        this.running = true
        this.callCamera() // 开启摄像头
        this.dialogVisible = true
        console.log('摄像头组件初始化')
      },
      // 关闭摄像头
      closeCamera () {
        this.dialogVisible = false
        console.log('关闭摄像头')
        if (this.$refs['video'] === undefined || !this.$refs['video'].srcObject) return
        let stream = this.$refs['video'].srcObject
        let tracks = stream.getTracks()
        tracks.forEach(track => {
          track.stop()
        })
        this.$refs['video'].srcObject = null
      },
      uploadPhoto () {
        let canvas = this.$refs['canvas']
        let image_code = canvas.toDataURL('image/png') // 要传给后台的base64
        let param = new FormData() // 创建form对象
        param.append('file', convertBase64UrlToBlob(image_code), 'job_problem' + new Date().getTime() + '_' + Math.floor((Math.random() * 10) + 1)) // 通过append向form对象添加数据
        this.httpRequest(
          'http://182.254.148.104:8082/file/singleupload', null, param,
          function (code, msg, data) {
            if (code >= 1 && data !== null && data !== undefined) {
              this.sendDataToParent(data)
              this.closeCamera()
            } else {
              this.$alert(msg, '提示')
            }
          }.bind(this)
        )
      },
      // 向父组件传值
      sendDataToParent (imgid) {
        this.$emit('listenToChildEvent', imgid)
      },
      // 通用请求方法
      httpRequest: function (url, params, form, callback) {
        const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })
        this.$http({
          url: url,
          params: params,
          data: form,
          headers: { 'Authorization': this.$store.state.ShiroToken.token },
          method: 'post'
        }).then(response => {
          let result = response.data
          let code = result.code
          let msg = result.msg
          let data = result.data
          if (code <= -1) {
            this.$alert(msg, '提示')
          } else {
            callback(code, msg, data)
          }
          loading.close()
        }).catch(function (error) { // 请求失败处理
          this.$alert('服务器异常，请稍后再试', '提示')
          console.log(error)
          loading.close()
        }.bind(this))
      }
    }
  }
</script>


<style scoped>
  .el-dialog{
    background-color: black;
  }
  .el-dialog video{
    object-fit: fill;
  }
</style>
