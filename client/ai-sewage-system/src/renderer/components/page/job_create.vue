<template>
  <el-form :model="jobAddForm" :rules="rules" ref="jobAddForm" label-width="100px" class="demo-jobAddForm"  >
    <el-form-item label="工单标题" prop="title" required>
      <el-input v-model="jobAddForm.title"></el-input>
    </el-form-item>
    <el-form-item label="问题分类" prop="jobTypeId" required>
      <el-select v-model="jobAddForm.jobTypeId" placeholder="请选择问题类型" :loading="jobTypeLoading">
        <el-option
            v-for="item in jobTypeList"
            :key="item.id"
            :label="item.jobTypeName"
            :value="item.id">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="内容" prop="content" required>
      <el-input type="textarea" v-model="jobAddForm.content" :autosize="{ minRows: 10, maxRows: 20}" ></el-input>
    </el-form-item>
    <el-form-item label="上传附件" prop="fileList" style="width: fit-content">
      <el-upload
          class="upload-demo"
          ref="upload"
          action="https://jsonplaceholder.typicode.com/posts/"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :file-list="jobAddForm.fileList"
          :auto-upload="false">
        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
        <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
        <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
      </el-upload>
    </el-form-item>
    <el-form-item  prop="email" label="联系邮箱">
      <el-input v-model="jobAddForm.email"></el-input>
    </el-form-item>
    <el-form-item  prop="telephone" label="联系电话">
      <el-input v-model="jobAddForm.telephone"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm('jobAddForm')">立即创建</el-button>
      <router-link to="job_control"> <el-button>返回</el-button></router-link>
    </el-form-item>
  </el-form>
</template>

<script>
  export default {
    name: 'job_create',
    created () {
      this.queryJobTypeList()
    },
    data () {
      return {
        jobTypeList: [{}, {}],
        jobAddForm: {
          title: '',
          jobTypeId: '',
          content: '',
          fileList: [],
          email: '',
          telephone: ''
        },
        rules: {
          title: [
            {required: true, message: '请输入工单问题标题', trigger: ['blur', 'change']}
          ],
          jobTypeId: [
            {required: true, message: '请选择问题类型', trigger: ['blur', 'change']}
          ],
          content: [
            {required: true, message: '请输入问题具体描述', trigger: ['blur', 'change']}
          ],
          email: [
            {type: 'email', required: true, message: '邮箱格式不正确', trigger: ['blur', 'change']}
          ],
          telephone: [
            {pattern: /^1[34578]\d{9}$/, required: true, message: '手机格式不正确', trigger: ['blur', 'change']}
          ]
        }
      }
    },

    methods: {
      submitForm (formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            const loading = this.$loading({
              lock: true,
              text: 'Loading',
              spinner: 'el-icon-loading',
              background: 'rgba(0, 0, 0, 0.7)'
            })
            this.$http.post('http://localhost:8081/job/add', { 'form': this.jobAddForm },
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
                  if (result.code == 100) {
                    this.$router.push('job_control')
                  }
                }
              })
            }.bind(this)).catch(function (error) {
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
        this.jobTypeLoading = true
        this.$http.post('http://localhost:8081/job/type/queryall', null, {
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(response => { // 这是从本地请求的数据接口，
          let result = response.data
          let list = result.data
          this.jobTypeList = list
          this.jobTypeLoading = false
        }).catch(function (error) { // 请求失败处理
          console.log(error)
          this.jobTypeList = null
          this.jobTypeLoading = false
        }.bind(this))
      },
      submitUpload () {
        this.$refs.upload.submit()
      },
      handleRemove (file, fileList) {
        console.log(file, fileList)
      },
      handlePreview (file) {
        console.log(file)
      }
    }
  }
</script>

<style scoped>
  .el-form{
    margin-top: 1em;
    height: 100%;
  }
</style>