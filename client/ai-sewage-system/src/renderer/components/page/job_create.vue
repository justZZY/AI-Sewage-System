<template>
  <el-container>
    <el-main>
      <el-form :model="jobAddForm" :rules="rules" ref="jobAddForm" label-width="100px" class="demo-jobAddForm" >
        <el-form-item label="问题分类" prop="jobTypeName" required>
          <el-button  :type="jobAddForm.jobTypeName==''?'':'primary'" plain @click="jobTypeSelectVisible=true" >{{jobAddForm.jobTypeName==''?'请选择问题类型':jobAddForm.jobTypeName}}</el-button>
        </el-form-item>
        <el-form-item label="问题描述" prop="content"  >
          <el-input type="textarea" v-model="jobAddForm.content" :autosize="{ minRows: 5, maxRows: 20}" placeholder="具体描述问题/故障.."></el-input>
        </el-form-item>
        <el-form-item label="故障站点" prop="site" required>
          <el-cascader v-model="jobAddForm.site" :options="sitesOptions" :props="sitesOptionsprops" @change="selectSite" :show-all-levels="false" size="medium"></el-cascader>
        </el-form-item>
        <el-form-item label="站点地址" prop="siteAddr"  >
          <el-input  v-model="jobAddForm.siteAddr" size="medium"></el-input>
        </el-form-item>
        <el-form-item label="严重程度"  prop="severity">
          <el-radio-group v-model="jobAddForm.severity" size="medium">
            <el-radio border label="一般"></el-radio>
            <el-radio border label="严重"></el-radio>
            <el-radio border label="非常严重"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="工单优先级"  prop="priority">
          <el-radio-group v-model="jobAddForm.priority" size="medium">
            <el-radio border label="一般"></el-radio>
            <el-radio border label="紧急"></el-radio>
            <el-radio border label="非常紧急"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="期望解决时间"  prop="expectedTime">
          <el-radio-group v-model="jobAddForm.expectedTime" size="medium">
            <el-radio v-for="(value,key) in [2,12,48]" :key="key" border :label="value" >{{value}}小时内</el-radio>
          </el-radio-group>
          <el-autocomplete
            size="medium"
            class="inline-input"
            v-model="jobAddForm.expectedTime"
            :fetch-suggestions="querySearch"
            @select="handleSelect"
            placeholder="小时"
            label="小时"
            :clearable="true"
            style="width:120px;margin-left:40px"
          ></el-autocomplete>
        </el-form-item>

        <el-form-item label="相关附件" prop="fileList" style="width: fit-content" >
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

        <el-tab-pane label="自定义描述">
          <el-input  v-model="jobAddForm.jobTypeName"  placeholder="自定义问题类别" :clearable="true"></el-input>
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
  import {siteDetail} from '../../js/site_detail'
  export default {
    name: 'job_create',
    components: { camera },
    created () {
      this.queryJobTypeList()
      this.querySitesMap2()
    },
    data () {
      return {
        jobTypeListJson: { },
        sitesMap: {},
        sitesOptions: this.$store.state.Treedata.treedata,
        sitesOptionsprops: {value: 'boxUid'},
        jobTypeSelectVisible: false,
        photoList: [],
        jobAddForm: {
          jobTypeName: '',
          content: '',
          fileList: [],
          expectedTime: 48,
          severity: '一般',
          priority: '一般',
          site: '',
          siteID: ''
        },
        rules: {
          jobTypeName: [
            {required: true, message: '请选择问题类型', trigger: ['blur', 'change']}
          ],
          site: [
            {required: true, message: '请选择故障站点', trigger: ['blur', 'change']}
          ]
        },
        // 上传图片
        dialogImageUrl: '',
        dialogVisible: false
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
            let form = JSON.parse(JSON.stringify(this.jobAddForm))
            form.fileList = this.jobAddForm.fileList.concat(this.photoList) // 注意提交的是jobAddForm.fileList (注意concat方法不修改原数组，只返回新数组)
            form.site = this.sitesMap[String(this.jobAddForm.site[2])].name
            this.$http.post('http://127.0.0.1:8082/job/add', form,
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
              console.log(error)
              loading.close()
            }.bind(this))
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
      /** 根据服务器配置文件 */
      querySitesMap1 () {
        this.$http.post('http://127.0.0.1:8082/job/site/list', null, {
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(response => { // 这是从本地请求的数据接口，
          let result = response.data
          let sites = result.data
          for (let i = 0; i < sites.length; i++) {
            let siteJson = sites[i]
            this.sitesMap[String(siteJson.id)] = siteJson
          }
        }).catch(function (error) { // 请求失败处理
          this.$alert('服务器异常，请稍后再试！', '提示')
          console.log(error)
          this.sitesMap = null
          this.sitesOptions = null
        }.bind(this))
      },
      /** 根据设备控制模块的缓存 */
      querySitesMap2 () {
        const equipmentobjarray = window.equipmentobjarray
        for (let i = 0; i < equipmentobjarray.length; i++) {
          let equipmentObj = equipmentobjarray[i]
          let site = {}
          site.name = equipmentObj.alias
          site.address = equipmentObj.box.address
          this.sitesMap[String(equipmentObj.boxUid)] = site
        }
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
      },
      // 2020.6.15 自定义时间
      querySearch (queryString, cb) {
        const times = [
          // {value: '半小时内', hours: 0.5},
          {value: '6小时内', hours: 6},
          {value: '24小时内', hours: 24},
          {value: '36小时内', hours: 36},
          {value: '3天内', hours: 3 * 24},
          {value: '15天内', hours: 15 * 24},
          {value: '30天内', hours: 30 * 24},
          {value: '60天内', hours: 60 * 24}
        ]
        // 调用 callback 返回建议列表的数据
        cb(times)
      },
      handleSelect (item) {
        this.jobAddForm.expectedTime = item.hours
      },
      // 选择站点
      selectSite (item) {
        let siteID = item[2]
        let site = this.sitesMap[String(siteID)]
        if (siteID === undefined || site === undefined) {
          this.jobAddForm.site = null
          this.jobAddForm.siteAddr = null
          this.jobAddForm.siteID = null
        } else {
          this.jobAddForm.siteAddr = site.address
          this.jobAddForm.siteID = siteID
        }
        console.log(item)
        console.log(this.jobAddForm.site)
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
