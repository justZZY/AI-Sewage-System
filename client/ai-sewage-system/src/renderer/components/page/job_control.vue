<template>
  <el-container>
    <el-header>
      <el-row :gutter="20">
        <el-col :span="5"><div class="grid-content bg-purple">工单管理({{currentUser.identity}})</div></el-col>
        <el-col :span="19"><div class="grid-content bg-purple" >
            <el-form :inline="true" :model="formSearch" size="small" class="demo-form-inline">
              <el-form-item label="问题类型">
                <el-cascader
                    :show-all-levels="false"
                    v-model="formSearch.jobType"
                    :options="jobTypeOptions"
                    :loading="jobTypeLoading">
                </el-cascader>

              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="selectMenu('search')">查询</el-button>
                <router-link to="job_create"><el-button type="primary" icon="el-icon-plus">新建工单</el-button></router-link>
            </el-form-item>
            </el-form>
          </div>
        </el-col>
      </el-row>
    </el-header>
    <el-container>
      <el-aside width="auto">
        <el-menu default-active="-1">
            <el-menu-item  v-for="(item,index) in jobMenu"
                         :index="index.toString()"
                          @click="selectMenu(item.reqUrl)"
                          v-bind:key="index"
                          v-if="(currentUser.identity!='admin' && item.reqUrl!='waitingspect' && item.reqUrl!='waiting' && item.reqUrl!='all') || (currentUser.identity=='admin' && (item.reqUrl=='waitingspect' || item.reqUrl=='waiting' || item.reqUrl=='all'))">
              <!--              <i class="el-icon-menu"></i>-->
              <span slot="title">{{item.title}}<el-badge class="mark" :hidden="item.num==null||item.num==''||item.num<1" :value="item.num" :max='99'/> </span>
            </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main v-loading="tablelodingshadow"
               element-loading-text="拼命加载中"
               element-loading-spinner="el-icon-loading">
        <div class="page-list" style="height: 100%"  v-if="page=='list'" >
          <el-table
              element-loading-text="拼命加载中"
              element-loading-spinner="el-icon-loading"
              :data="jobList"
              fit
              stripe
              height="85%"
              @selection-change="handleSelectionChange"
              :cell-class-name="cellClass"
              :cell-style="cellStyle"
          >
            <el-table-column
                v-if="queryJobType=='waiting' || queryJobType=='processing' "
                type="selection"
                width="55">
            </el-table-column>
            <el-table-column
                fixed
                prop="number"
                label="序号"
                width="50">
              <template scope="scope"><span>{{scope.$index+(currentPage - 1) * pageSize + 1}} </span></template>
            </el-table-column>
            <el-table-column
                prop="jobTypeName"
                label="工单类型" >
            </el-table-column>
            <el-table-column
                prop="status"
                width="100"
                label="状态"
                :formatter="formatterStatus" >
            </el-table-column>
            <el-table-column
                prop="createTime"
                label="创建日期"
                min-width="150"
                :formatter="formatterDate">
            </el-table-column>
            <el-table-column
                prop="updateTime"
                label="更新日期"
                min-width="150"
                :formatter="formatterDate">
            </el-table-column>
            <el-table-column
                prop="creator"
                label="创建人" >
            </el-table-column>
            <el-table-column
                prop="processor"
                label="处理人" >
            </el-table-column>
            <el-table-column
                prop="inspector"
                label="审核人" >
            </el-table-column>
            <el-table-column
                fixed="right"
                label="操作" >
              <template slot-scope="scope">
                <el-button @click="showDetail(scope.row)" type="text" size="small">查看</el-button>
<!--                <el-button type="text" size="small" v-show="queryJobType=='processing'">编辑</el-button>-->
              </template>
            </el-table-column>
          </el-table>
          <el-row>
            <el-col :span="12"><div class="grid-content bg-purple-light" >
              <div class="block pag">
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page.sync="currentPage"
                    :page-sizes="[5, 10, 100, 200]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next"
                    background

                    :total="totalNum">
                </el-pagination>
              </div>
            </div></el-col>
            <el-col :span="12"><div class="grid-content bg-purple">
              <div class="block pag" style="text-align: right" v-show="selectJobsIds.length>0">
                <el-button type="primary" icon="el-icon-plus" v-show="queryJobType=='waiting'" @click="allocateFormVisible=true">派单</el-button>
                <el-button type="primary" icon="el-icon-s-promotion" v-show="queryJobType=='processing'" @click="forwardFormVisible=true">转发</el-button>
              </div>
            </div></el-col>
          </el-row>
        </div>

        <div class="page-info" align="center" v-if="page=='detail'" style="width:max-content;min-width:100%">
          <el-steps  align-center >
            <el-step  v-for="(process,i) in processList"
                      :title="statusMapper[process.type]"
                      :icon="switchJobTypeIcon(process.type)"
                      :status="process.type=='5'?'success':process.type=='6'?'error':'finish'"
                      :description="new Date(process['createTime']).toLocaleString()"
                      :key="i">
            </el-step>
            <el-step title="创建" :icon="switchJobTypeIcon(1)" v-if="processList[processList.length-1].type<1"  status="wait" description=""></el-step>
            <el-step title="受理" :icon="switchJobTypeIcon(2)" v-if="processList[processList.length-1].type<2"  status="wait" description=""></el-step>
            <el-step title="审核" :icon="switchJobTypeIcon(4)" v-if="processList[processList.length-1].type<4" status="wait" description=""></el-step>
            <el-step title="完成" :icon="switchJobTypeIcon(5)" v-if="processList[processList.length-1].type<=4" status="wait" description=""></el-step>

          </el-steps>

          <el-collapse v-model="collapseOpened" style="width: 90%;margin-top: 10em" >
            <el-collapse-item title="工单详情" name="1">
              <el-form :model="jobDetail" ref="jobDetailForm" label-width="100px" disabled class="jobDetail">
                <el-form-item label="问题分类" prop="jobTypeName" >
                  <el-input v-model="jobDetail.jobTypeName"></el-input>
                </el-form-item>
                <el-form-item label="内容" prop="content" >
                  <el-input type="textarea" v-model="jobDetail.content" :autosize="{ minRows: 5, maxRows: 20}" resize="false" ></el-input>
                </el-form-item>
                <el-form-item label="附件" prop="file">
                  <template>
                    <el-carousel :interval="4000" type="card" height="200px">
                      <el-carousel-item v-for="item in createProcess.file" :key="item" >
                        <img :src="'http://43.228.77.195:8082/file/download/'+item" preview="1" preview-text=" " style="width:100%">
                      </el-carousel-item>
                    </el-carousel>
                  </template>
                </el-form-item>
                <el-form-item  prop="email" label="联系邮箱">
                  <el-input v-model="jobDetail.email"></el-input>
                </el-form-item>
                <el-form-item  prop="telephone" label="联系电话">
                  <el-input v-model="jobDetail.telephone"></el-input>
                </el-form-item>
                <el-form-item label="创建人" prop="creator" v-if="jobDetail.creator">
                  <el-input v-model="jobDetail.creator"></el-input>
                </el-form-item>
                <el-form-item label="处理人" prop="processor" v-if="jobDetail.processor">
                  <el-input v-model="jobDetail.processor"></el-input>
                </el-form-item>
                <el-form-item label="审核人" prop="inspector" v-if="jobDetail.inspector" >
                  <el-input v-model="jobDetail.inspector"></el-input>
                </el-form-item>
              </el-form>
            </el-collapse-item>

            <el-collapse-item title="处理结果" name="2.1" v-if="jobDetail.status>=4">
              <el-form :model="resultProcessed"  label-width="100px" disabled  >
                <el-form-item label="处理人" prop="username" >
                  <el-input v-model="resultProcessed.username"></el-input>
                </el-form-item>
                <el-form-item label="结果说明" prop="content" >
                  <el-input type="textarea" v-model="resultProcessed.remark" :autosize="{ minRows: 5, maxRows: 20}" resize="false" ></el-input>
                </el-form-item>
                <el-form-item label="附件" prop="file">
                  <template>
                    <el-carousel :interval="4000" type="card" height="200px">
                      <el-carousel-item v-for="item in resultProcessed.file" :key="item" >
                        <img :src="'http://43.228.77.195:8082/file/download/'+item" preview="2.1" preview-text=" " style="width:100%">
                      </el-carousel-item>
                    </el-carousel>
                  </template>
                </el-form-item>
              </el-form>
            </el-collapse-item>

            <el-collapse-item title="审核结果" name="2.2" v-if="jobDetail.status>4">
              <el-form :model="resultInspected"  label-width="100px" disabled  >
                <el-form-item label="审核人" prop="username" >
                  <el-input v-model="resultInspected.username"></el-input>
                </el-form-item>
                <el-form-item label="结果说明" prop="content" >
                  <el-input type="textarea" v-model="resultInspected.remark" :autosize="{ minRows: 5, maxRows: 20}" resize="false" ></el-input>
                </el-form-item>
                <el-form-item label="附件" prop="file">
                  <template>
                    <el-carousel :interval="4000" type="card" height="200px">
                      <el-carousel-item v-for="item in resultInspected.file" :key="item" >
                        <img :src="'http://43.228.77.195:8082/file/download/'+item" preview="2.2" preview-text=" " style="width:100%">
                      </el-carousel-item>
                    </el-carousel>
                  </template>
                </el-form-item>
              </el-form>
            </el-collapse-item>

            <el-collapse-item title="派单" name="3" v-if="jobDetail.status==1 && currentUser.identity=='admin'" >
              <el-form :model="processForm" rel="processForm" label-width="100px" >
                <el-form-item align="left">
                  <el-button type="primary" @click="allocateFormVisible=true" >派单</el-button>
                  <el-button @click="returnToPageList">返回</el-button>
                </el-form-item>
              </el-form>
            </el-collapse-item>

            <el-collapse-item title="工单完成确认" name="3" v-if="jobDetail.status==2 && jobDetail.processor==currentUser.username" >  <!--工单属于当前登录用户-->
              <el-form :model="processForm" rel="processForm" label-width="100px" >
                <el-form-item label="结果说明" prop="content" >
                  <el-input type="textarea" v-model="processForm.content" :autosize="{ minRows: 5, maxRows: 20}" resize="false" ></el-input>
                </el-form-item>

                 <el-form-item label="上传附件" prop="fileList"  align="left">
                  <el-upload action="http://43.228.77.195:8082/file/singleupload"
                            list-type="picture-card"
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

                <el-form-item label="拍照" prop="photoList"  align="left">
                  <div>
                    <ul class="el-upload-list el-upload-list--picture-card">
                      <li tabindex="0" class="el-upload-list__item is-success" v-for="item in photoList" :key="item">
                        <img :src="'http://43.228.77.195:8082/file/download/' + item" alt="" class="el-upload-list__item-thumbnail"><a class="el-upload-list__item-name">
                        <i class="el-icon-document"></i>
                      </a>
                        <label class="el-upload-list__item-status-label">
                          <i class="el-icon-upload-success el-icon-check"></i>
                        </label>
                        <i class="el-icon-close"></i>
                        <i class="el-icon-close-tip"></i>
                        <span class="el-upload-list__item-actions">
                    <span class="el-upload-list__item-preview">
                      <i class="el-icon-zoom-in" @click="pictureCardPreview('http://43.228.77.195:8082/file/download/' + item)"></i>
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

                <el-form-item align="left">
                  <el-button type="primary" @click="submitForm(4)">确认完成</el-button>
                  <el-button @click="returnToPageList">返回</el-button>
                </el-form-item>
              </el-form>
            </el-collapse-item>

            <el-collapse-item title="工单审核确认" name="3" v-if="jobDetail.status==4 && currentUser.identity=='admin'" >  <!--工单属于当前登录用户-->
              <el-form :model="processForm" rel="processForm" label-width="100px" >
                <el-form-item label="结果说明" prop="content" >
                  <el-input type="textarea" v-model="processForm.content" :autosize="{ minRows: 5, maxRows: 20}" resize="false" ></el-input>
                </el-form-item>
                <el-form-item label="上传附件" prop="fileList"  align="left">
                  <el-upload action="http://43.228.77.195:8082/file/singleupload"
                            list-type="picture-card"
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

                <el-form-item label="拍照" prop="photoList" align="left">
                  <div>
                    <ul class="el-upload-list el-upload-list--picture-card">
                      <li tabindex="0" class="el-upload-list__item is-success" v-for="item in photoList" :key="item">
                        <img :src="'http://43.228.77.195:8082/file/download/' + item" alt="" class="el-upload-list__item-thumbnail"><a class="el-upload-list__item-name">
                        <i class="el-icon-document"></i>
                      </a>
                        <label class="el-upload-list__item-status-label">
                          <i class="el-icon-upload-success el-icon-check"></i>
                        </label>
                        <i class="el-icon-close"></i>
                        <i class="el-icon-close-tip"></i>
                        <span class="el-upload-list__item-actions">
                    <span class="el-upload-list__item-preview">
                      <i class="el-icon-zoom-in" @click="pictureCardPreview('http://43.228.77.195:8082/file/download/' + item)"></i>
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

                <el-form-item align="left">
                  <el-button type="warning"  @click="submitForm(7)">驳回</el-button>
                  <el-button type="primary" @click="submitForm(5)">审核通过</el-button>
                  <el-button type="danger"  @click="submitForm(6)">中断</el-button>
                  <el-button @click="returnToPageList">返回</el-button>
                </el-form-item>
              </el-form>
            </el-collapse-item>
          </el-collapse>
        </div>

      </el-main>
    </el-container>
    <el-dialog title="工单转发" :visible.sync="forwardFormVisible">
      <el-form :model="forwardForm"  :rules="rules" ref="forwardForm">
        <el-form-item label="备注" label-width="100px">
          <el-input v-model="forwardForm.remark" type="textarea"   :autosize="{ minRows: 5, maxRows: 20}" ></el-input>
        </el-form-item>
        <el-form-item label="选择转发人" label-width="100px" prop="username" required>
          <el-select v-model="forwardForm.username" placeholder="请选择工单接收人" :loading="userListLoading">
            <el-option
                v-for="username in userList"
                :key="username"
                :label="username"
                :value="username"
                v-show="username!=currentUser.username"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="forwardFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="forwardJobs">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="派单" :visible.sync="allocateFormVisible">
      <el-form :model="allocateForm"  :rules="rules" ref="allocateForm">
        <el-form-item label="备注" label-width="100px">
          <el-input v-model="allocateForm.remark" type="textarea"   :autosize="{ minRows: 5, maxRows: 20}" ></el-input>
        </el-form-item>
        <el-form-item label="选择维护人" label-width="100px" prop="username" required>
          <el-select v-model="allocateForm.username" placeholder="请选择维护人" :loading="userListLoading">
            <el-option
                v-for="username in userList"
                :key="username"
                :label="username"
                :value="username"
                v-show="username!=currentUser.username"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="allocateFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="allocateJobs">确 定</el-button>
      </div>
    </el-dialog>

    <camera v-on:listenToChildEvent="receiveDataFromCamera" ref="camera"></camera>

  </el-container>
</template>

<script>
  import camera from './../common/camera'
  export default {
    name: 'job_control',
    components: { camera },
    created () {
      this.showList()
      this.queryJobTypeList()
      this.queryUserList()
      this.queryLoginUser()
    },
    methods: {
      queryLoginUser () {
        this.$http.post('http://43.228.77.195:8082/job/user/curlogin', null, {
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(response => {
          let result = response.data
          // eslint-disable-next-line eqeqeq
          if (result.code == 1) {
            this.currentUser = result.data
            console.log(this.currentUser.identity)
          } else {
            this.$alert('登录失效，请重新登录', '提示', { callback: action => {
              location.href = '/'
            }
            })
          }
        }).catch(function (error) { // 请求失败处理
          this.$alert('服务器异常，请稍后再试！', '提示')
          console.log(error)
          this.currentUser = {}
        }.bind(this))
      },
      queryMenuJobsCount () {
        for (let i = 0, len = this.jobMenu.length; i < len; i++) {
          this.$http('http://43.228.77.195:8082/job/query/count', {
            params: {type: this.jobMenu[i].reqUrl},
            headers: {
              'Authorization': this.$store.state.ShiroToken.token
            },
            method: 'post'
          }).then(response => {
            let result = response.data
            // eslint-disable-next-line eqeqeq
            if (result.code == 1) {
              this.jobMenu[i].num = result.data
            } else {
              this.jobMenu[i].num = 0
            }
          }).catch(function (error) { // 请求失败处理
            // this.$alert('服务器异常，请稍后再试！', '提示')
            console.log(error)
            this.jobMenu[i].num = 0
          }.bind(this))
        }
      },
      showList () {
        this.page = 'list'
        this.jobDetail = {}
        this.selectJobsIds = []
        this.queryJobList()
        this.queryMenuJobsCount()
      },
      showDetail (row) {
        // 工单进程初始化
        this.processList = []
        this.jobDetail = {} // 当前显示的工单详情
        this.createProcess = {} // 创建进程
        this.resultProcessed = {} // 处理结果（进程已完成的数据）
        this.resultInspected = {} // 审核结果
        this.processForm = {} // 进程确认/完成
        this.photoList = []

        let jobId = row.id
        this.collapseOpened = ['3'] // 展开的面板默认只打开第三个
        this.queryJobProcessList(jobId) // 查询进程列表
        this.updateJobDetail(jobId) // 更新当前的工单详情
        this.page = 'detail' // 显示详情页

        console.log(row)
      },
      // 初始页currentPage、初始每页数据数pagesize和数据data
      handleSizeChange: function (size) {
        this.pageSize = size
        this.showList()
        console.log(this.pageSize) // 每页下拉显示数据
      },
      handleCurrentChange: function (currentPage) {
        this.currentPage = currentPage
        this.showList()
        console.log(this.currentPage) // 点击第几页
      },
      selectMenu: function (menu) {
        this.queryJobType = menu
        this.currentPage = 1
        this.showList()
      },
      queryJobList () {
        if (this.queryJobType === null) {
          return
        }
        let keyword_val = null
        if (this.queryJobType === 'search') {
          keyword_val = this.formSearch.jobType[this.formSearch.jobType.length - 1]
          if (keyword_val == null) {
            return
          }
        }
        this.tablelodingshadow = true
        this.$http({
          url: 'http://43.228.77.195:8082/job/query/' + this.queryJobType,
          params: {
            pageIndex: this.currentPage,
            pageSize: this.pageSize,
            keyword: keyword_val
          },
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          },
          method: 'post'
        }).then(response => {
          let result = response.data
          let pageInfo = result.data
          this.jobList = pageInfo.list
          this.totalNum = pageInfo.total
          this.tablelodingshadow = false
        }).catch(function (error) { // 请求失败处理
          this.$alert('服务器异常，请稍后再试！', '提示')
          console.log(error)
          this.jobList = null
          this.totalNum = 0
          this.tablelodingshadow = false
        }.bind(this))
      },
      queryJobTypeList () {
        this.jobTypeLoading = true
        this.$http.post('http://43.228.77.195:8082/job/type/queryall', null, {
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(response => {
          let result = response.data
          let list = result.data
          this.jobTypeList = list
          this.formatJobTypeOptions()
          this.jobTypeLoading = false
        }).catch(function (error) { // 请求失败处理
          // this.$alert('服务器异常，请稍后再试！', '提示')
          console.log(error)
          this.jobTypeList = null
          this.jobTypeLoading = false
        }.bind(this))
      },
      queryJobProcessList (jobId) {
        this.tablelodingshadow = true
        this.$http({
          url: 'http://43.228.77.195:8082/job/jobprocess/query/list/' + jobId,
          method: 'post',
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(response => {
          let result = response.data
          this.processList = result.data
          // 更新当前具体进程
          for (let i = 0, len = this.processList.length; i < len; i++) {
            if (this.processList[i].file !== undefined && this.processList[i].file !== '') {
              this.processList[i].file = JSON.parse(this.processList[i].file) // 将文件id列表转化为json，不然就字符串
            }
            // eslint-disable-next-line eqeqeq
            if (this.processList[i].type == '4') {
              this.resultProcessed = this.processList[i] // 完成进程
              // eslint-disable-next-line eqeqeq
            } else if (this.processList[i].type == '5' || this.processList[i].type == '6') {
              this.resultInspected = this.processList[i] // 审核进程
              // eslint-disable-next-line eqeqeq
            } else if (this.processList[i].type == '1') {
              this.createProcess = this.processList[i] // 创建程
            }
          }
          this.tablelodingshadow = false
        }).catch(function (error) { // 请求失败处理
          this.$alert('服务器异常，请稍后再试！', '提示')
          console.log(error)
          this.processList = null
          this.tablelodingshadow = false
        }.bind(this))
      },
      // 查询工单详情
      updateJobDetail (jobId) {
        const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })
        this.$http({
          url: 'http://43.228.77.195:8082/job/query/one',
          method: 'post',
          params: {
            'jobId': jobId
          },
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(response => {
          loading.close()
          let result = response.data
          let code = result.code
          let msg = result.msg
          this.jobDetail = {}
          if (code <= 0) this.$alert(msg, '提示')
          else this.jobDetail = result.data
        }).catch(function (error) { // 请求失败处理
          this.$alert('服务器异常，请稍后再试！', '提示')
          console.log(error)
          this.jobDetail = null
          loading.close()
        })
      },
      // 完成工单当前进程
      submitForm (type) {
        const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })
        const processUrlMapping = {
          '2': 'grab', // 不存在了，接单功能去除，换成派单
          '4': 'done',
          '5': 'inspect',
          '6': 'inspect',
          '7': 'reject'
        }
        this.processForm.jobId = this.jobDetail.id
        this.processForm.type = type
        let temp = this.processForm.fileList
        if (this.processForm.fileList !== null && this.processForm.fileList !== undefined) {
          this.processForm.fileList = this.processForm.fileList.concat(this.photoList) // 注意提交的是processForm.fileList (注意concat方法不修改原数组，只返回新数组)
        }
        this.$http({
          url: 'http://43.228.77.195:8082/job/' + processUrlMapping[type],
          method: 'post',
          data: this.processForm,
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(response => {
          let result = response.data
          loading.close()
          this.$alert(result.msg, '提示',
            { callback: action => {
              this.queryJobProcessList(this.jobDetail.id) // 更新进程列表
              this.queryMenuJobsCount()
              this.updateJobDetail(this.jobDetail.id) // 更新当前的工单详情
              this.processForm = {}
              this.photoList = []
            }
            })
        }).catch(function (error) { // 请求失败处理
          this.$alert('服务器异常，请稍后再试！', '提示')
          console.log(error)
          this.processForm.fileList = temp
          loading.close()
        })
      },
      grabJobs (jobId) {
        const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })
        this.$http({
          url: 'http://43.228.77.195:8082/job/grab',
          data: {
            jobsIds: this.selectJobsIds
          },
          method: 'post',
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(response => {
          let result = response.data
          loading.close()
          this.$alert(result.msg, '提示', this.showList())
        }).catch(function (error) { // 请求失败处理
          this.$alert('服务器异常，请稍后再试！', '提示')
          console.log(error)
          loading.close()
        }(this))
      },
      queryUserList () {
        this.userListLoading = true
        this.$http.post('http://43.228.77.195:8082/job/user/list', null, {
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(response => {
          let result = response.data
          let list = result.data
          if (list == null) return
          this.userList = list
          this.userListLoading = false
        }).catch(function (error) { // 请求失败处理
          this.$alert('服务器异常，请稍后再试！', '提示')
          console.log(error)
          this.userList = null
          this.userListLoading = false
        }.bind(this))
      },
      forwardJobs () {
        this.$refs['forwardForm'].validate((valid) => {
          if (valid) {
            const loading = this.$loading({
              lock: true,
              text: 'Loading',
              spinner: 'el-icon-loading',
              background: 'rgba(0, 0, 0, 0.7)'
            })
            this.$http({
              url: 'http://43.228.77.195:8082/job/forward',
              data: {
                jobsIds: this.selectJobsIds,
                receiverUsername: this.forwardForm.username,
                remark: this.forwardForm.remark
              },
              method: 'post',
              headers: {
                'Authorization': this.$store.state.ShiroToken.token
              }
            }).then(response => {
              let result = response.data
              loading.close()
              this.forwardFormVisible = false
              this.forwardForm = {}
              this.$alert(result.msg, '提示', this.showList())
            }).catch(function (error) { // 请求失败处理
              this.$alert('服务器异常，请稍后再试！', '提示')
              console.log(error)
              loading.close()
              this.forwardFormVisible = false
              this.forwardForm = {}
            }.bind(this))
          }
        })
      },
      allocateJobs () {
        this.$refs['allocateForm'].validate((valid) => {
          if (valid) {
            const loading = this.$loading({
              lock: true,
              text: 'Loading',
              spinner: 'el-icon-loading',
              background: 'rgba(0, 0, 0, 0.7)'
            })
            let allocateJobsIds = []
            if (this.jobDetail.id != null) {
              allocateJobsIds = [this.jobDetail.id]
            } else {
              allocateJobsIds = this.selectJobsIds
            }
            this.$http({
              url: 'http://43.228.77.195:8082/job/allocate',
              data: {
                jobsIds: allocateJobsIds,
                username: this.allocateForm.username,
                remark: this.allocateForm.remark
              },
              method: 'post',
              headers: {
                'Authorization': this.$store.state.ShiroToken.token
              }
            }).then(response => {
              let result = response.data
              loading.close()
              this.allocateFormVisible = false
              this.allocateForm = {}
              this.$alert(result.msg, '提示', this.showList())
            }).catch(function (error) { // 请求失败处理
              this.$alert('服务器异常，请稍后再试！', '提示')
              console.log(error)
              loading.close()
              this.allocateFormVisible = false
              this.allocateForm = {}
            }.bind(this))
          }
        })
      },
      searchJobsAboutMe () {
        const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })
        this.$http({
          url: 'http://43.228.77.195:8082/job/query/search',
          data: this.formsearch,
          params: {
            pageIndex: this.currentPage,
            pageSize: this.pageSize,
            keyword: this.formsearch.jobType
          },
          method: 'post',
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(response => {
          let result = response.data
          loading.close()
          this.forwardFormVisible = false
          this.$alert(result.msg, '提示', this.showList())
        }).catch(function (error) { // 请求失败处理
          this.$alert('服务器异常，请稍后再试！', '提示')
          console.log(error)
          loading.close()
          this.forwardFormVisible = false
        }.bind(this))
      },
      handleSelectionChange (selectObjs) {
        this.selectJobsIds = []
        for (let i = 0, len = selectObjs.length; i < len; i++) {
          this.selectJobsIds[i] = (selectObjs[i].id)
        }
      },
      returnToPageList () {
        this.page = 'list'
        this.selectJobsIds = []
        this.jobDetail = {}
      },
      // 确认工单完成上传附件
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
        console.log(file, fileList)
        this.processForm.fileList = this.processForm.fileList.filter(function (item) {
          return item !== file.response.data
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
        let fileId = result.data
        if (this.processForm.fileList === undefined || this.processForm.fileList === '') {
          this.processForm.fileList = []
        }
        this.processForm.fileList[this.processForm.fileList.length] = fileId
      },
      handleError (rr, file, fileList) {
        console.log(rr)
        console.log(file)
        console.log(fileList)
      },
      formatterDate (row, column, cellValue, index) {
        return new Date(cellValue).toLocaleString()
      },
      formatterStatus (row, column, cellValue, index) {
        return this.statusMapper[cellValue]
      },
      formatJobTypeOptions () {
        for (let key in this.jobTypeList) {
          let json = {}
          json.value = key
          json.label = key
          // eslint-disable-next-line no-array-constructor
          let childArray = new Array()
          for (let j = 0, len = this.jobTypeList[key].length; j < len; j++) {
            let secJson = this.jobTypeList[key][j]
            let json = {}
            json.value = secJson.jobTypeName
            json.label = secJson.jobTypeName
            childArray.push(json)
          }
          json.children = childArray
          this.jobTypeOptions.push(json)
        }
      },
      // 拍照
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
      cellClass ({row, column, rowIndex, columnIndex}) {
        // eslint-disable-next-line eqeqeq
        if (column.property == 'status') {
          return 'status_column'
        }
      },
      cellStyle ({row, column, rowIndex, columnIndex}) {
        // eslint-disable-next-line eqeqeq
        if (column.property == 'status') {
          switch (row.status) {
            case '1': return 'color:#232525'
            case '2': return 'color:#20CBC5'
            case '4': return 'color:#1E91B4'
            case '5': return 'color:#009C2C'
            case '6': return 'color:#AE0F24'
          }
        }
      },
      switchJobTypeIcon (type) {
        type = parseInt(type)
        switch (type) {
          case 1: return 'el-icon-document-add'
          case 2: return 'el-icon-edit-outline'
          case 3: return 'el-icon-position'
          case 4: return 'el-icon-search'
          case 5: return 'el-icon-success'
          case 6: return 'el-icon-error'
          case 7: return 'el-icon-refresh-left'
        }
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
    },
    data () {
      return {
        currentUser: {
          username: '',
          delete_status: '', // 禁用状态
          identity: '', // 用户身份
          area: '', // 用户可查看的地区
          phone: '',
          mail: ''
        }, // 当期登录用户
        page: 'list', // 当前显示页面(list：显示工单列表，detail：具体某条工单信息)
        // 工单进程
        processList: [],
        collapseOpened: ['3'], // 展开的面板
        jobDetail: {}, // 当前显示的工单
        createProcess: {}, // 创建进程
        resultProcessed: {}, // 处理结果（进程已完成的数据）
        resultInspected: {}, // 审核结果
        processForm: {}, // 进程确认/完成
        jobTypeList: [],
        jobTypeOptions: [],
        selectJobsIds: [], // 选中的工单
        // 上传图片
        dialogImageUrl: '',
        dialogVisible: false,
        photoList: [], // 拍照图片列表(数据库的图片id)
        // 工单左菜单栏
        jobMenu: [
          {title: '未指派工单', reqUrl: 'waiting', num: 0},
          {title: '待审核工单', reqUrl: 'waitingspect', num: 0},
          {title: '所有工单', reqUrl: 'all', num: 0},
          {title: '我创建的工单', reqUrl: 'create', num: 0},
          {title: '我的未完成的工单', reqUrl: 'processing', num: 0},
          {title: '我的待审核的工单', reqUrl: 'processed', num: 0},
          {title: '我的处理成功的工单', reqUrl: 'success', num: 0},
          {title: '我的处理中断的工单', reqUrl: 'fail', num: 0}
        ],
        queryJobType: null, // 当前选中的左菜单栏
        // 状态编码转换名称
        statusMapper: {
          '1': '创建/待受理',
          '2': '处理中',
          '3': '被转接',
          '4': '审核中',
          '5': '处理成功',
          '6': '处理中断',
          '7': '驳回'
        },
        // 分页配置&工单列表数据源
        currentPage: 1, // 当前页
        pageSize: 10, // 每页的数据
        totalNum: 0,
        jobList: [],
        tablelodingshadow: false, // 表格数据加载中
        jobTypeLoading: false, // 工单类型数据加载中（下拉框）
        // 按条件查询
        formSearch: {
          jobType: ''
        },
        // 转发工单弹窗
        forwardFormVisible: false,
        forwardForm: {
          remark: '',
          username: ''
        },
        // 派单
        allocateFormVisible: false,
        allocateForm: {
          remark: '',
          username: ''
        },
        userListLoading: false, // 选择用户
        userList: [ ],
        rules: {
          username: [
            {required: true, message: '请选择工单接收人', trigger: ['blur', 'change']}
          ]
        }

      }
    }

  }
</script>

<style scoped>
  .el-header{
    background-color: #F4F4F4;
    border-bottom: #ded8d8 1px solid;
    border-top: #ded8d8 1px solid;
    text-align: left;
    line-height: 60px;
  }
  .el-header .el-row .el-col:nth-child(2){
    line-height: 0px;
    margin-bottom: 0px;
    padding: 1em;
    text-align: right;
  }
  .el-aside{
    background-color: #D9E5E5;
  }
  .el-aside>.el-menu{
    background-color: inherit;
    width: auto;
  }
  div.pag{
    margin-top: 2em;
  }
  .el-steps{
    margin-top: 200px;
  }
  ul.file-list{
    list-style: none;
    text-align: left;
  }
  ul.file-list > li {
    line-height: 2em;
  }


</style>
<style>
  .el-table .status_column{
  }
</style>
