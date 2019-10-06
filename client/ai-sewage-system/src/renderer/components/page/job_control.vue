<template>
  <el-container>
    <el-header>
      <el-row :gutter="20">
        <el-col :span="5"><div class="grid-content bg-purple">工单管理</div></el-col>
        <el-col :span="19"><div class="grid-content bg-purple" >
            <el-form :inline="true" :model="formsearch" size="small" class="demo-form-inline">
              <el-form-item label="标题">
                <el-input v-model="formsearch.title" placeholder="工单标题" prefix-icon="el-icon-search"></el-input>
              </el-form-item>
              <el-form-item label="问题类型">
                <el-select v-model="formsearch.jobType" placeholder="问题类型" :loading="jobTypeLoading">
                  <el-option
                      v-for="item in jobTypeList"
                      :key="item.id"
                      :label="item.jobTypeName"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="searchJobsAboutMe">查询</el-button>
                <router-link to="job_create"><el-button type="primary" icon="el-icon-plus">新建工单</el-button></router-link>
            </el-form-item>
            </el-form>
          </div>
        </el-col>
      </el-row>
    </el-header>
    <el-container>
      <el-aside width="200px">
        <el-menu default-active="2">
            <el-menu-item  v-for="(item,index) in jobMenu"
                         :index="index.toString()"
                          @click="selectMenu(item.reqUrl)">
              <!--              <i class="el-icon-menu"></i>-->
              <span slot="title">{{item.title}}</span>
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
                width="80">
              <template scope="scope"><span>{{scope.$index+(currentPage - 1) * pageSize + 1}} </span></template>
            </el-table-column>
            <el-table-column
                class-name=""
                prop="title"
                label="标题" >
            </el-table-column>
            <el-table-column
                prop="jobTypeName"
                label="工单类型" >
            </el-table-column>
            <el-table-column
                prop="status"
                width="150"
                label="状态"
                :formatter="formatterStatus" >
            </el-table-column>
            <el-table-column
                prop="createTime"
                label="创建日期"
                min-width="100"
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
                    :page-sizes="[3, 10, 100, 200]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next"
                    background

                    :total="totalNum">
                </el-pagination>
              </div>
            </div></el-col>
            <el-col :span="12"><div class="grid-content bg-purple">
              <div class="block pag" style="text-align: right" v-show="selectJobsIds.length>0">
                <el-button type="primary" icon="el-icon-plus" v-show="queryJobType=='waiting'" @click="grabJobs">领取工单</el-button>
                <el-button type="primary" icon="el-icon-s-promotion" v-show="queryJobType=='processing'" @click="forwardFormVisible=true">转发</el-button>
              </div>
            </div></el-col>
          </el-row>
        </div>

        <div class="page-info" align="center" v-if="page=='detail'">
          <el-steps  align-center >
            <el-step  v-for="(process,i) in processList" :title="statusMapper[process.type]" :status="process.type=='5'?'success':process.type=='6'?'error':'finish'" :description="new Date(process['createTime']).toLocaleString()"></el-step>
            <el-step title="创建" v-if="processList[processList.length-1].type<1"  status="wait" description=""></el-step>
            <el-step title="受理" v-if="processList[processList.length-1].type<2"  status="wait" description=""></el-step>
            <el-step title="审核" v-if="processList[processList.length-1].type<4" status="wait" description=""></el-step>
            <el-step title="完成" v-if="processList[processList.length-1].type<=4" status="wait" description=""></el-step>
<!--            <el-step title="结束" v-if="processList[processList.length-1].type>4" status="finish" description=""></el-step>-->
          </el-steps>

          <el-collapse v-model="collapseOpened" style="width: 90%;margin-top: 10em" >
            <el-collapse-item title="工单详情" name="1">
              <el-form :model="jobDetail" ref="jobDetailForm" label-width="100px" disabled class="jobDetail">
                <el-form-item label="工单标题" prop="title" >
                  <el-input v-model="jobDetail.title"></el-input>
                </el-form-item>
                <el-form-item label="问题分类" prop="jobTypeName" >
                  <el-input v-model="jobDetail.jobTypeName"></el-input>
                </el-form-item>
                <el-form-item label="内容" prop="content" >
                  <el-input type="textarea" v-model="jobDetail.content" :autosize="{ minRows: 10, maxRows: 20}" resize="false" ></el-input>
                </el-form-item>
                <el-form-item label="附件" prop="fileList">
                  <ul class="file-list">
                    <li>故障设备.jpg</li>
                    <li>描述文档.xml</li>
                  </ul>
                </el-form-item>
                <el-form-item  prop="email" label="联系邮箱">
                  <el-input v-model="jobDetail.email"></el-input>
                </el-form-item>
                <el-form-item  prop="telephone" label="联系电话">
                  <el-input v-model="jobDetail.telephone"></el-input>
                </el-form-item>
              </el-form>
            </el-collapse-item>

            <el-collapse-item title="处理结果" name="2.1" v-if="jobDetail.status>=4">
              <el-form :model="resultProcessed"  label-width="100px" disabled  >
                <el-form-item label="结果说明" prop="content" >
                  <el-input type="textarea" v-model="resultProcessed.remark==null?'无':resultProcessed.remark" :autosize="{ minRows: 10, maxRows: 20}" resize="false" ></el-input>
                </el-form-item>
                <el-form-item label="附件" prop="fileList">
                  <ul class="file-list">
                    <li>故障设备.jpg</li>
                    <li>描述文档.xml</li>
                  </ul>
                </el-form-item>
              </el-form>
            </el-collapse-item>

            <el-collapse-item title="审核结果" name="2.2" v-if="jobDetail.status>4">
              <el-form :model="resultInspected"  label-width="100px" disabled  >
                <el-form-item label="结果说明" prop="content" >
                  <el-input type="textarea" v-model="resultInspected.remark==null?'无':resultInspected.remark" :autosize="{ minRows: 10, maxRows: 20}" resize="false" ></el-input>
                </el-form-item>
                <el-form-item label="附件" prop="fileList">
                  <ul class="file-list">
                    <li>故障设备.jpg</li>
                    <li>描述文档.xml</li>
                  </ul>
                </el-form-item>
              </el-form>
            </el-collapse-item>

            <el-collapse-item title="接单" name="3" v-if="jobDetail.status==1" >
              <el-form :model="processForm" rel="processForm" label-width="100px" >
                <el-form-item align="left">
                  <el-button type="primary" @click="submitForm(2)" >接单</el-button>
                  <el-button @click="returnToPageList">返回</el-button>
                </el-form-item>
              </el-form>
            </el-collapse-item>

            <el-collapse-item title="工单完成确认" name="3" v-if="jobDetail.status==2 && jobDetail.processorUid==1" >  <!--工单属于当前登录用户-->
              <el-form :model="processForm" rel="processForm" label-width="100px" >
                <el-form-item label="结果说明" prop="content" >
                  <el-input type="textarea" v-model="processForm.content" :autosize="{ minRows: 10, maxRows: 20}" resize="false" ></el-input>
                </el-form-item>
                <el-form-item label="上传附件" prop="fileList" >
                  <el-upload
                      align="left"
                      class="upload-demo"
                      ref="upload"
                      action="https://jsonplaceholder.typicode.com/posts/"
                      :on-preview="handlePreview"
                      :on-remove="handleRemove"
                      :file-list="processForm.fileList"
                      :auto-upload="false">
                    <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                    <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
                    <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
                  </el-upload>
                </el-form-item>
                <el-form-item align="left">
                  <el-button type="primary" @click="submitForm(4)">确认完成</el-button>
                  <el-button @click="returnToPageList">返回</el-button>
                </el-form-item>
              </el-form>
            </el-collapse-item>

            <el-collapse-item title="工单审核确认" name="3" v-if="jobDetail.status==4 " >  <!--工单属于当前登录用户-->
              <el-form :model="processForm" rel="processForm" label-width="100px" >
                <el-form-item label="结果说明" prop="content" >
                  <el-input type="textarea" v-model="processForm.content" :autosize="{ minRows: 10, maxRows: 20}" resize="false" ></el-input>
                </el-form-item>
                <el-form-item label="上传附件" prop="fileList" >
                  <el-upload
                      align="left"
                      class="upload-demo"
                      ref="upload"
                      action="https://jsonplaceholder.typicode.com/posts/"
                      :on-preview="handlePreview"
                      :on-remove="handleRemove"
                      :file-list="processForm.fileList"
                      :auto-upload="false">
                    <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                    <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
                    <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
                  </el-upload>
                </el-form-item>
                <el-form-item align="left">
                  <el-button type="primary" @click="submitForm(5)">审核通过</el-button>
                  <el-button type="danger"  @click="submitForm(6)">驳回</el-button>
                  <el-button @click="returnToPageList">返回</el-button>
                </el-form-item>
              </el-form>
            </el-collapse-item>
          </el-collapse>
        </div>

      </el-main>
    </el-container>
    <el-dialog title="工单转发" :visible.sync="forwardFormVisible">
      <el-form :model="forwardForm" >
        <el-form-item label="备注" label-width="100px" >
          <el-input v-model="forwardForm.content" type="textarea"   :autosize="{ minRows: 10, maxRows: 20}" ></el-input>
        </el-form-item>
        <el-form-item label="选择转发人" label-width="100px">
          <el-select v-model="forwardForm.uid" placeholder="情选择工单接收人" :loading="userListLoading">
            <el-option
                v-for="item in userList"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="forwardFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="forwardJobs">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog
        title="选择问题类型"
        :visible.sync=jobtypeSelectVisible
        center
    >
      <el-tabs class="jobtype" tab-position="top" type="border-card" stretch="true" style="height: 500px">
        <el-tab-pane label="aa ">
        <el-checkbox-group v-model="jobTypeList">
            <el-checkbox label="复选框 A" border></el-checkbox>
            <el-checkbox label="复选框 B" border></el-checkbox>
            <el-checkbox label="复选框 C"></el-checkbox>
            <el-checkbox label="禁用" disabled></el-checkbox>
            <el-checkbox label="选中且禁用" disabled></el-checkbox>
          </el-checkbox-group>
        </el-tab-pane>

        <el-tab-pane label="投诉" >
          <el-checkbox-group v-model="jobTypeList"  >
            <el-checkbox label="客服态度恶劣"   border></el-checkbox>
            <el-checkbox  label="问题处理随意" border ></el-checkbox>
            <el-checkbox  label="问题处理过于缓慢"border  ></el-checkbox>
            <el-checkbox  label="就是想投诉" border ></el-checkbox>
            <el-checkbox  label="工作人员不好看" border ></el-checkbox>
            <el-checkbox  label="工作人员脾气不好" border ></el-checkbox>
          </el-checkbox-group>
        </el-tab-pane>

        <el-tab-pane label="建议">
            <el-checkbox  label="设备安全建议" border></el-checkbox>
            <el-checkbox  label="系统功能性建议" border></el-checkbox>
            <el-checkbox  label="吐槽建议" border></el-checkbox>
         </el-tab-pane>


        <el-tab-pane label="设备故障">
          <el-checkbox  label="电力故障" border></el-checkbox>
          <el-checkbox  label="水管问题" border></el-checkbox>
          <el-checkbox  label="水泵故障" border></el-checkbox>
          <el-checkbox  label="风机故障" border></el-checkbox>
          <el-checkbox  label="进水问题" border></el-checkbox>
          <el-checkbox  label="出水问题" border></el-checkbox>
          <el-checkbox  label="设备不工作" border></el-checkbox>
          <el-checkbox  label="设备损坏" border></el-checkbox>
          <el-checkbox  label="设备老化" border></el-checkbox>
        </el-tab-pane>

        <el-tab-pane label="软件BUG">
          <el-checkbox  label="登录异常" border></el-checkbox>
          <el-checkbox  label="功能不可用" border></el-checkbox>
          <el-checkbox  label="权限问题" border></el-checkbox>
          <el-checkbox  label="体验不适" border></el-checkbox>
        </el-tab-pane>
        <el-tab-pane label="其他">
          <el-checkbox  label="自定义描述" border></el-checkbox>
        </el-tab-pane>
      </el-tabs>
      <span slot="footer" class="dialog-footer">
        <el-button  @click="jobtypeSelectVisible=false">取 消</el-button>
        <el-button type="primary"  @click="jobtypeSelectVisible=false">确 定</el-button>
      </span>
    </el-dialog>


  </el-container>
</template>

<script>
  export default {
    name: 'job_control',
    created () {
      this.showList()
      this.queryJobTypeList()
      this.queryUserList()
    },
    methods: {
      showList () {
        this.currentPage = 1
        this.page = 'list'
        this.selectJobsIds = []
        this.queryJobList()
      },
      showDetail (row) {
        let jobId = row.id
        this.collapseOpened = ['3'] // 展开的面板默认只打开第三个
        this.queryJobProcessList(jobId) // 查询进程列表
        this.updateJobDetail(jobId) // 更新当前的工单详情
        this.page = 'detail' // 显示详情页
        this.processForm = {}
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
        this.showList()
      },
      queryJobList () {
        this.tablelodingshadow = true
        this.$http({
          url: 'http://localhost:8081/job/query/' + this.queryJobType,
          params: {
            pageIndex: this.currentPage,
            pageSize: this.pageSize
          },
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          },
          method: 'post'
        }).then(response => { // 这是从本地请求的数据接口，
          let result = response.data
          let pageInfo = result.data
          this.jobList = pageInfo.list
          this.totalNum = pageInfo.total
          this.tablelodingshadow = false
        }).catch(function (error) { // 请求失败处理
          console.log(error)
          this.jobList = null
          this.totalNum = 0
          this.tablelodingshadow = false
        }.bind(this))
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
        }.bind(this))
        this.jobTypeLoading = false
      },
      queryJobProcessList (jobId) {
        this.tablelodingshadow = true
        this.$http({
          url: 'http://localhost:8081/job/jobprocess/query/list/' + jobId,
          method: 'post',
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(response => { // 这是从本地请求的数据接口，
          let result = response.data
          this.processList = result.data
          // 更新当前具体进程
          for (let i = 0, len = this.processList.length; i < len; i++) {
            // eslint-disable-next-line eqeqeq
            if (this.processList[i].type == '4') {
              this.resultProcessed = this.processList[i] // 完成进程
              // eslint-disable-next-line eqeqeq
            } else if (this.processList[i].type == '5' || this.processList[i].type == '6') {
              this.resultInspected = this.processList[i] // 审核进程
            }
          }
          this.tablelodingshadow = false
        }).catch(function (error) { // 请求失败处理
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
          url: 'http://localhost:8081/job/query/one',
          method: 'post',
          params: {
            'jobId': jobId
          },
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(response => { // 这是从本地请求的数据接口，
          loading.close()
          let result = response.data
          let code = result.code
          let msg = result.msg
          if (code < 0) this.$alert(msg, '提示')
          else this.jobDetail = result.data
        }).catch(function (error) { // 请求失败处理
          console.log(error)
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
          '2': 'grab',
          '4': 'done',
          '5': 'inspect',
          '6': 'inspect'
        }
        this.processForm.jobId = this.jobDetail.id
        this.processForm.type = type
        this.$http({
          url: 'http://localhost:8081/job/' + processUrlMapping[type],
          method: 'post',
          data: this.processForm,
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(response => { // 这是从本地请求的数据接口，
          let result = response.data
          loading.close()
          this.$alert(result.msg, '提示',
            { callback: action => {
              this.queryJobProcessList(this.jobDetail.id) // 更新进程列表
              this.updateJobDetail(this.jobDetail.id) // 更新当前的工单详情
              this.processForm = {}
            }
            })
        }).catch(function (error) { // 请求失败处理
          console.log(error)
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
          url: 'http://localhost:8081/job/grab',
          data: {
            jobsIds: this.selectJobsIds
          },
          method: 'post',
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(response => { // 这是从本地请求的数据接口，
          let result = response.data
          loading.close()
          this.$alert(result.msg, '提示', this.showList())
        }).catch(function (error) { // 请求失败处理
          console.log(error)
          loading.close()
        }(this))
      },
      queryUserList () {
        this.userListLoading = true
        this.$http.post('http://localhost:8081/user/list', null, {
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(response => { // 这是从本地请求的数据接口，
          let result = response.data
          let list = result.data
          // this.userList = list
          this.userListLoading = false
        }).catch(function (error) { // 请求失败处理
          console.log(error)
          // this.userList = null
          this.userListLoading = false
        }.bind(this))
      },
      forwardJobs () {
        const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })
        this.$http({
          url: 'http://localhost:8081/job/forward',
          data: {
            jobsIds: this.selectJobsIds,
            receiverUid: this.forwardForm.uid,
            remark: this.forwardForm.content
          },
          method: 'post',
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(response => { // 这是从本地请求的数据接口，
          let result = response.data
          loading.close()
          this.forwardFormVisible = false
          this.$alert(result.msg, '提示', this.showList())
        }).catch(function (error) { // 请求失败处理
          console.log(error)
          loading.close()
          this.forwardFormVisible = false
        }.bind(this))
      },
      searchJobsAboutMe () {
        const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })
        this.$http({
          url: 'http://localhost:8081/job/query/search',
          data: this.formsearch,
          params: {
            pageIndex: this.currentPage,
            pageSize: this.pageSize
          },
          method: 'post',
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(response => { // 这是从本地请求的数据接口，
          let result = response.data
          loading.close()
          this.forwardFormVisible = false
          this.$alert(result.msg, '提示', this.showList())
        }).catch(function (error) { // 请求失败处理
          console.log(error)
          loading.close()
          this.forwardFormVisible = false
        }.bind(this))
        console.log('submit!')
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
      },
      formatterDate (row, column, cellValue, index) {
        return new Date(cellValue).toLocaleString()
      },
      formatterStatus (row, column, cellValue, index) {
        return this.statusMapper[cellValue]
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
      }
    },
    data () {
      return {
        page: 'detail', // 当前显示页面(list：显示工单列表，detail：具体某条工单信息)
        // 工单进程
        processList: [],
        collapseOpened: ['3'], // 展开的面板
        jobDetail: {}, // 当前显示的工单
        resultProcessed: {}, // 处理结果（进程已完成的数据）
        resultInspected: {}, // 审核结果
        processForm: {}, // 进程确认/完成
        jobTypeList: [],
        selectJobsIds: [], // 选中的工单
        jobtypeSelectVisible: true,
        // 工单左菜单栏
        jobMenu: [
          {title: '待领取的工单', reqUrl: 'waiting'},
          {title: '我创建的工单', reqUrl: 'create'},
          {title: '我的未完成的工单', reqUrl: 'processing'},
          {title: '我的待审核的工单', reqUrl: 'processed'},
          {title: '我的处理成功的工单', reqUrl: 'success'},
          {title: '我的处理失败的工单', reqUrl: 'fail'},
          {title: '所有待审核的工单', reqUrl: 'waitinspect'}
        ],
        queryJobType: 'processing', // 当前选中的左菜单栏
        // 状态编码转换名称
        statusMapper: {
          '1': '创建/待受理',
          '2': '处理中',
          '3': '转接',
          '4': '待审核',
          '5': '完成',
          '6': '驳回'
        },
        // 分页配置&工单列表数据源
        currentPage: 1, // 当前页
        pageSize: 3, // 每页的数据
        totalNum: 0,
        jobList: [],
        tablelodingshadow: false, // 表格数据加载中
        jobTypeLoading: false, // 工单类型数据加载中（下拉框）
        // 按条件查询
        formsearch: {
          user: '',
          region: ''
        },
        // 转发工单弹窗
        forwardFormVisible: false,
        forwardForm: {
          content: '转发备注',
          uid: ''
        },
        userListLoading: false, // 选择用户
        userList: [{
          id: 1, name: '孙悟空'
        }, {
          id: 2, name: '猪八戒'
        }, {
          id: 3, name: '沙悟净'
        }, {
          id: 4, name: '唐三藏'
        }, {
          id: 5, name: '白龙马'
        }]

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
  .el-tabs.jobtype .el-checkbox {
    margin-top: 10px;
    margin-bottom: 20px;
    margin-left: 10px;
    margin-right: 10px;
  }

</style>
<style>
  .el-table .status_column{
  }
</style>