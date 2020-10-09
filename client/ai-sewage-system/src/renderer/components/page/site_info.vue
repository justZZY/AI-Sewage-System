<template>
  <div>
    <!-- 站点详情 -->
    <el-row>
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
                  <el-carousel-item v-for="item in siteForm.uploadImgIDArray" :key="item">
                    <img :src="'http://43.228.77.195:8082/file/download/'+item" style="width:100%">
                  </el-carousel-item>
                </el-carousel>
              </el-col>
            </el-row>
            <div style="width: 560px; margin: 0 auto">
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
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 用于编辑站点信息 -->
    <el-dialog title="编辑信息" :visible.sync="dialogSiteFormVisible">
      <el-row :gutter="10">
        <el-col :span="24">
          <el-col :span="10">
            <el-upload action="http://43.228.77.195:8082/file/singleupload"
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
  </div>
</template>

<script>
  export default {
    name: 'site_info',
    data () {
      return {
        siteForm: {},
        table: [],
        dialogSiteFormVisible: false,
        dialogPreVisible: false,
        dialogPreImageUrl: '',
        waitMergeImgArray: []
      }
    },
    created () {
      this.siteGet()
    },
    watch: {
      refreshPage: function () {
        this.$router.replace({
          path: '@/components/page/skip/info_skip',
          name: 'info_skip'
        })
      }
    },
    computed: {
      refreshPage () {
        return this.$store.state.Treedata.chooseData
      }
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
        this.$http.post('http://43.228.77.195:8082/equip/getSiteDetail', {
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
      onSiteClick () {
        this.dialogSiteFormVisible = true
        this.waitMergeImgArray = []
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
        this.$http.post('http://43.228.77.195:8082/equip/setSiteDetail', this.siteForm, {
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(res => {
          console.log(res)
          if (res.data.code === 1) {
            this.dialogSiteFormVisible = false
            // 刷新一下页面
            this.$router.replace({
              path: '@/components/page/skip/info_skip',
              name: 'info_skip'
            })
          } else {
            this.$message.error('服务器异常, 修改失败')
          }
        })
      },
      onSiteCancel () {
        this.dialogSiteFormVisible = false
      }
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
</script>

<style scoped>

</style>
