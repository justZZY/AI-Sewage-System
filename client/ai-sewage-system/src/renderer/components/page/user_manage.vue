<template>
  <div class="app-container">
    <div
    class="filter-container"
    style="{width: 100%; display: flex; justify-content: flex-end; margin-top: 10px;}">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" @click="showCreate">添加</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
              highlight-current-row stripe>
      <el-table-column align="center" label="用户名" prop="username"></el-table-column>
      <el-table-column align="center" label="角色"
                       :filters="[{text: '管理员', value: 'admin'}, {text: '普通用户', value: 'user'}]"
                       :filter-method="filterTag" filter-placement="bottom-end">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.identity === 'admin'" :type="scope.row.identity === 'admin' ? 'primary' : 'success'" disable-transitions>
            <span>管理员</span>
          </el-tag>
          <el-tag v-if="scope.row.identity === 'user'" :type="scope.row.identity === 'admin' ? 'primary' : 'success'" disable-transitions>
            <span>用户</span>
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="地区">
        <template slot-scope="scope">
          <el-button @click="showUserArea(scope)">查看</el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="是否冻结" prop="delete_status">
        <template slot-scope="scope">
          <el-button type="warning" v-if="scope.row.delete_status === 1" @click="frozenUser(scope)">已冻结</el-button>
          <el-button type="warning" v-if="scope.row.delete_status === 0" @click="frozenUser(scope)">未冻结</el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作">
        <template slot-scope="scope">
          <el-button type="warning" @click="showUpdate(scope)">编辑</el-button>
          <el-button type="danger" @click="removeUser(scope)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div
    class="paginationContainer"
    style="{width: 100%; display: flex; justify-content: center; margin-top: 10px;}">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="listQuery.pageNum"
        :page-size="listQuery.pageRow"
        :total="totalCount"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper">
      </el-pagination>
    </div>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-dialog title="地区选择" :visible.sync="dialogFormAreaVisible" append-to-body>
        <el-transfer v-model="chooseCity" :data="cityArray" filterable></el-transfer>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormAreaVisible = false">取 消</el-button>
          <el-button type="primary" @click="chooseCityList">确 定</el-button>
        </div>
      </el-dialog>
      <el-form class="small-space" :model="tempUser" ref="tempUser" label-position="left" label-width="80px"
               style='width: 300px; margin-left:50px;'>
        <el-form-item label="用户名" prop="username" :rules="nameRules" v-if="dialogStatus === 'create'">
          <el-input v-model="tempUser.username"></el-input>
        </el-form-item>
        <el-form-item label="用户名" required v-if="dialogStatus === 'update'">
          <span>{{tempUser.username}}</span>
        </el-form-item>
        <el-form-item label="密码" prop="password" :rules="passwordRules" v-if="dialogStatus === 'create'" required>
          <el-input type="password" v-model="tempUser.password"></el-input>
          <span style="color: red">初始密码默认123456</span>
        </el-form-item>
        <el-form-item label="新密码" v-if="dialogStatus === 'update'">
          <el-input type="password" v-model="tempUser.password" placeholder="不填则表示不修改"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" v-if="dialogStatus === 'update'">
          <el-input type="password" v-model="tempUser.confirmPassword" placeholder="不填则表示不修改"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone" :rules="phoneRules">
          <el-input v-model.number="tempUser.phone"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="mail" :rules="mailRules">
          <el-input v-model="tempUser.mail"></el-input>
        </el-form-item>
        <el-form-item label="角色" required>
          <el-select v-model="tempUser.identity" placeholder="请选择" :value="tempUser.identity">
            <el-option
                v-for="item in roles"
                :key="item.roleId"
                :label="item.roleName"
                :value="item.roleId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="地区" required>
          <el-button @click="showAreaChoose">查看/编辑</el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus==='create'" type="success" @click="createUser('tempUser')">创 建</el-button>
        <el-button type="primary" v-else @click="updateUser('tempUser')">修 改</el-button>
      </div>
    </el-dialog>
    <el-dialog title="区域列表" :visible.sync="dialogShowUserAreaVisible" width="20%">
      <el-table :data="showCityArray" border stripe height="300">
        <el-table-column prop="city" label="城市" align="center"></el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogShowUserAreaVisible = false" type="success">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    name: 'user_manage',
    data () {
      return {
        nameRules: [{required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 6, message: '长度在 3 到 6 个字符', trigger: 'blur'}],
        passwordRules: [{required: true, message: '请输入密码', trigger: 'blur'},
          {min: 6, max: 15, message: '长度在 6 到 18 个字符', trigger: 'blur'}],
        mailRules: [{required: true, message: '请输入邮箱', trigger: 'blur'}, {type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']}],
        phoneRules: [{required: true, message: '手机号不能为空'}, {type: 'number', message: '手机号必须为数字值'}],
        totalCount: 0, // 分页组件--数据总条数
        list: [], // 表格的数据
        listLoading: false, // 数据加载等待动画
        listQuery: {
          pageNum: 1, // 页码
          pageRow: 50 // 每页条数
        },
        roles: [{roleName: '用户', roleId: 'user'}, {roleName: '管理员', roleId: 'admin'}],
        dialogStatus: 'create',
        dialogFormVisible: false,
        dialogFormAreaVisible: false,
        dialogShowUserAreaVisible: false,
        textMap: {
          update: '编辑',
          create: '新建用户'
        },
        tempUser: {
          username: '',
          password: '',
          confirmPassword: '',
          identity: '',
          area: '',
          delete_status: 0,
          pid: 1,
          phone: '',
          mail: ''
        },
        compareCityArray: [], // 原始city列表
        cityArray: [], // Transfer所有city列表
        chooseCity: [], // Transfer所选city下标索引列表
        showCityArray: []
      }
    },
    created () {
      this.getList()
      this.getAreas()
    },
    methods: {
      getList () {
        // 查询列表
        this.listLoading = true
        this.$http.get('http://43.228.77.195:8082/user/list', {
          params: this.listQuery,
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(data => {
          console.log(data)
          this.listLoading = false
          this.list = data['data']['info']['list']
          this.totalCount = data['data']['info']['totalCount']
        })
      },
      filterTag (value, row) {
        return row['identity'] === value
      },
      handleSizeChange (val) {
        // 改变每页数量
        this.listQuery.pageRow = val
        this.handleFilter()
      },
      handleCurrentChange (val) {
        // 改变页码
        this.listQuery.pageNum = val
        this.getList()
      },
      handleFilter () {
        // 查询事件
        this.listQuery.pageNum = 1
        this.getList()
      },
      getIndex ($index) {
        // 表格序号
        return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1
      },
      showCreate () {
        // 显示新增对话框
        this.tempUser.username = ''
        this.tempUser.password = '123456'
        this.tempUser.confirmPassword = '123456'
        this.tempUser.identity = 'user'
        this.tempUser.area = ''
        this.tempUser.delete_status = 0
        this.tempUser.phone = ''
        this.tempUser.mail = ''
        this.dialogStatus = 'create'
        this.dialogFormVisible = true
      },
      showUpdate (scope) {
        console.log(scope)
        this.tempUser.username = scope.row.username
        this.tempUser.password = ''
        this.tempUser.confirmPassword = ''
        this.tempUser.identity = scope.row.identity
        this.tempUser.area = scope.row.area
        this.tempUser.delete_status = scope.row.deleteStatus
        this.tempUser.phone = Number(scope.row.phone)
        this.tempUser.mail = scope.row.mail
        this.dialogStatus = 'update'
        this.dialogFormVisible = true
      },
      createUser (formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.tempUser.pid = getPid(this.tempUser.identity)
            // 添加新用户
            this.$http.post('http://43.228.77.195:8082/user/addUser', {
              user: this.tempUser
            }, {
              headers: {
                'Authorization': this.$store.state.ShiroToken.token
              }
            }).then(res => {
              console.log(res)
              if (res['data']['code'] === '10009') {
                this.$message.error(res['data']['msg'])
              } else {
                this.getList()
                this.dialogFormVisible = false
              }
            })
          } else {
            console.log('error submit!')
            return false
          }
        })
      },
      // 修改用户信息
      updateUser (formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.tempUser.pid = getPid(this.tempUser.identity)
            console.log(this.tempUser.password)
            console.log(this.tempUser.confirmPassword)
            if (this.tempUser.password === this.tempUser.confirmPassword) {
              this.$http.post('http://43.228.77.195:8082/user/updateUser', {
                user: this.tempUser
              }, {
                headers: {
                  'Authorization': this.$store.state.ShiroToken.token
                }
              }).then(() => {
                let msg = '修改成功'
                this.dialogFormVisible = false
                if (this.$store.state.ShiroToken.username === this.tempUser.username) {
                  msg = '修改成功,部分信息重新登录后生效'
                }
                this.$message({
                  message: msg,
                  type: 'success',
                  onClose: () => {
                    this.getList()
                  }
                })
              })
            } else {
              this.$message.error('两次密码不一致')
            }
          } else {
            console.log('error submit!')
            return false
          }
        })
      },
      frozenUser (scope) {
        if (scope.row.delete_status === 0) {
          this.$confirm('此操作将冻结账号' + scope.row.username + ', 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.frozen(scope.row.username, 1)
          }).catch(() => {
            this.$message('已取消账号冻结操作')
          })
        } else {
          this.$confirm('此操作将解冻账号: ' + scope.row.username + ', 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.frozen(scope.row.username, 0)
          }).catch(() => {
            this.$message('已取消账号解冻操作')
          })
        }
      },
      frozen (username, deleteStatus) {
        this.$http.post('http://43.228.77.195:8082/user/frozenUser', {
          username: username,
          deleteStatus: deleteStatus
        }, {
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(res => {
          console.log(res)
          this.getList()
          this.$message({
            message: '操作成功',
            type: 'success'
          })
        }).catch(() => {
          this.$message.error('取消冻结操作')
        })
      },
      removeUser (scope) {
        this.$confirm('此操作将删除用户: ' + scope.row.username + ', 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http.post('http://43.228.77.195:8082/user/removeUser', {
            username: scope.row.username
          }, {
            headers: {
              'Authorization': this.$store.state.ShiroToken.token
            }
          }).then(res => {
            console.log(res)
            this.getList()
          }).catch(() => {
            this.$message.error('取消删除操作')
          })
        })
      },
      getAreas () {
        let treeData = this.$store.state.Treedata.treedata
        console.log(treeData)
        for (let i = 0; i < treeData.length; i++) {
          for (let j = 0; j < treeData[i]['children'].length; j++) {
            for (let k = 0; k < treeData[i]['children'][j]['children'].length; k++) {
              this.compareCityArray.push(treeData[i]['children'][j]['children'][k].label)
            }
          }
        }
        console.log(this.compareCityArray)
        for (let i = 0; i < this.compareCityArray.length; i++) {
          this.cityArray.push({key: i, label: this.compareCityArray[i]})
        }
      },
      chooseCityList () {
        this.dialogFormAreaVisible = false
        let cityIndexList = this.chooseCity
        let areaString = ''
        this.chooseCity = []
        for (let i = 0; i < cityIndexList.length; i++) {
          areaString += (this.compareCityArray[cityIndexList[i]] + ';')
        }
        this.tempUser.area = areaString
        console.log(this.tempUser)
      },
      transCityList () {
        let tempCityArray = this.tempUser.area.split(';')
        console.log(tempCityArray)
        let indexArray = []
        for (let i = 0; i < tempCityArray.length; i++) {
          for (let j = 0; j < this.cityArray.length; j++) {
            if (this.cityArray[j].label === tempCityArray[i]) {
              indexArray.push(j)
            }
          }
        }
        return indexArray
      },
      showAreaChoose () {
        this.dialogFormAreaVisible = true
        this.chooseCity = this.transCityList()
      },
      // 用于用户区域权限展示
      showUserArea (scope) {
        this.showCityArray = []
        let areaArray = scope.row.area.split(';')
        areaArray.pop()
        for (let i = 0; i < areaArray.length; i++) {
          this.showCityArray.push({city: areaArray[i]})
        }
        this.dialogShowUserAreaVisible = true
      }
    }
  }
  function getPid (identity) {
    if (identity === 'user') {
      return 2
    } else {
      return 1
    }
  }
</script>
