<template>
  <el-container>
    <el-main>
      <el-form :model="jobAddForm" :rules="rules" ref="jobAddForm" label-width="100px" class="demo-jobAddForm" >
        <el-form-item label="问题分类" prop="jobTypeName" required>
          <el-button  :type="jobAddForm.jobTypeName==''?'':'primary'" plain @click="jobTypeSelectVisible=true" >{{jobAddForm.jobTypeName==''?'请选择问题类型':jobAddForm.jobTypeName}}</el-button>
        </el-form-item>
        <el-form-item label="内容" prop="content"  >
          <el-input type="textarea" v-model="jobAddForm.content" :autosize="{ minRows: 5, maxRows: 20}" ></el-input>
        </el-form-item>
        <el-form-item label="上传附件" prop="fileList" style="width: fit-content" >
          <el-upload action="http://127.0.0.1:8082/file/singleupload"
                     list-type="picture-card"
                     :file-list="jobAddForm.fileList"
                     :multiple="true"
                     :show-file-list="true"
                     :with-credentials="true"
                     :before-upload="handleBeforeUpload"
                     :on-remove="handleRemove"
                     :on-error="handleError"
                     :on-success="handleSuccess"
                     :on-preview="handlePictureCardPreview"
                     :headers="{ 'Authorization': this.$store.state.ShiroToken.token }">
            <i class="el-icon-plus"></i>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过5M</div>
          </el-upload>
          <el-dialog :visible.sync="dialogVisible" size="tiny">
            <img width="100%" :src="dialogImageUrl" alt>
          </el-dialog>
        </el-form-item>

        <el-form-item label="拍照" prop="photoList" style="width: fit-content" >
          <div>
            <ul class="el-upload-list el-upload-list--picture-card">
              <li tabindex="0" class="el-upload-list__item is-success" v-for="item in photoList" :key="item">
                <img :src="'http://127.0.0.1:8082/file/download/' + item" alt="" class="el-upload-list__item-thumbnail"><a class="el-upload-list__item-name">
                <i class="el-icon-document"></i>
              </a>
                <label class="el-upload-list__item-status-label">
                  <i class="el-icon-upload-success el-icon-check"></i>
                </label>
                <i class="el-icon-close"></i>
                <i class="el-icon-close-tip"></i>
                <span class="el-upload-list__item-actions">
                    <span class="el-upload-list__item-preview">
                      <i class="el-icon-zoom-in" @click="pictureCardPreview('http://127.0.0.1:8082/file//download/' + item)"></i>
                    </span>
                    <span class="el-upload-list__item-delete">
                      <i class="el-icon-delete" @click="deleteFromPhotoList(item)"></i>
                    </span>
                  </span>
              </li>
            </ul>
            <!-- 拍照按钮 -->
            <div tabindex="0" class="el-upload el-upload--picture-card"  @click="openCamera">
              <i class="el-icon-camera-solid"></i>
            </div>
          </div>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm('jobAddForm')">立即创建</el-button>
          <router-link to="job_control"> <el-button>返回</el-button></router-link>
        </el-form-item>
      </el-form>
    </el-main>
    <el-dialog
        title="选择问题类型"
        :visible.sync=jobTypeSelectVisible
        center
    >
      <el-tabs class="jobtype" tab-position="top" type="border-card"  style="height: 500px">

        <el-tab-pane v-for="(val,key) in jobTypeListJson" :label="key" v-bind:key="key" >
          <el-radio-group v-model="jobAddForm.jobTypeName"  >
            <el-radio v-for="(item,k) in val" :label="item.jobTypeName"  v-bind:key="k" border></el-radio>
          </el-radio-group>
        </el-tab-pane>

        <el-tab-pane label="其他">
          <el-radio-group v-model="jobAddForm.jobTypeName">
            <el-radio  label="自定义描述" border></el-radio>
          </el-radio-group>
        </el-tab-pane>
      </el-tabs>
      <span slot="footer" class="dialog-footer">
          <el-button type="primary"  @click="jobTypeSelectVisible=false">确 定</el-button>
      </span>
    </el-dialog>
    <camera v-on:listenToChildEvent="receiveDataFromCamera" ref="camera"></camera>
  </el-container>
</template>




<script>
  import camera from './../common/camera' // 引入组件
  export default {
    name: 'job_create',
    components: { camera },
    created () {
      this.queryJobTypeList()
    },
    data () {
      return {
        jobTypeListJson: { },
        jobTypeSelectVisible: false,
        photoList: [],
        jobAddForm: {
          jobTypeName: '',
          content: '',
          fileList: []
        },
        rules: {
          jobTypeName: [
            {required: true, message: '请选择问题类型', trigger: ['blur', 'change']}
          ]
        },
        // 上传图片
        dialogImageUrl: '',
        dialogVisible: false
      }
    },

    methods: {
      initParams: function () {
        this.jobTypeListJson = { }
        this.jobTypeSelectVisible = false
        this.photoList = []
        this.jobAddForm = {
          jobTypeName: '',
          content: '',
          fileList: [],
          email: '',
          telephone: ''
        }
        // 上传图片
        this.dialogImageUrl = ''
        this.dialogVisible = false
      },
      submitForm (formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            const loading = this.$loading({
              lock: true,
              text: 'Loading',
              spinner: 'el-icon-loading',
              background: 'rgba(0, 0, 0, 0.7)'
            })
            let temp = this.jobAddForm.fileList
            if (this.jobAddForm.fileList !== null && this.jobAddForm.fileList !== undefined) {
              this.jobAddForm.fileList = this.jobAddForm.fileList.concat(this.photoList) // 注意提交的是jobAddForm.fileList (注意concat方法不修改原数组，只返回新数组)
            }
            this.$http.post('http://127.0.0.1:8082/job/add', this.jobAddForm,
              {
                headers: {
                  'Authorization': this.$store.state.ShiroToken.token
                }
              }).then(function (response) {
              let result = response.data
              this.$alert(result.msg, '提示', {
                confirmButtonText: '确定',
                callback: action => {
                  loading.close()
                  // eslint-disable-next-line eqeqeq
                  if (result.code == 1) {
                    this.$router.push('job_control')
                  }
                }
              })
            }.bind(this)).catch(function (error) {
              this.$alert('服务器异常，请稍后再试！', '提示')
              this.jobAddForm.fileList = temp
              console.log(error)
              loading.close()
            })
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },
      queryJobTypeList () {
        this.$http.post('http://127.0.0.1:8082/job/type/queryall', null, {
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(response => { // 这是从本地请求的数据接口，
          let result = response.data
          let json = result.data
          this.jobTypeListJson = json
        }).catch(function (error) { // 请求失败处理
          this.$alert('服务器异常，请稍后再试！', '提示')
          console.log(error)
          this.jobTypeListJson = null
        }.bind(this))
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
        this.jobAddForm.fileList.forEach(function (item, index, arr) {
          // eslint-disable-next-line eqeqeq
          if (item == file.response.data) {
            arr.splice(index, 1)
          }
        })
      },
      handlePictureCardPreview (file) {
        this.dialogImageUrl = file.url
        this.dialogVisible = true
      },
      pictureCardPreview (url) {
        this.dialogImageUrl = url
        this.dialogVisible = true
      },
      handleSuccess (result, file, fileList) {
        fileList = null
        let fileId = result.data
        this.jobAddForm.fileList[this.jobAddForm.fileList.length] = fileId
      },
      handleError (rr, file, fileList) {
        console.log(rr)
        console.log(file)
        console.log(fileList)
        this.$message.error('上传失败')
      },
      openCamera () {
        this.$refs.camera.openCamera()
      },
      // 删除拍下的图片
      deleteFromPhotoList (imgId) {
        this.photoList = this.photoList.filter(function (item) {
          return item !== imgId
        })
      },
      // 摄像头拍照回调方法
      receiveDataFromCamera (photoObj) {
        this.photoList.splice(this.photoList.length, 1, photoObj)
        console.log(this.photoList)
      }
    }
  }
</script>

<style scoped>
  .el-form{
    margin-top: 1em;
    height: 100%;
  }
  .el-tabs.jobtype .el-radio {
    margin-top: 10px;
    margin-bottom: 10px;
    margin-left: 10px;
    margin-right: 10px;
  }
</style>
