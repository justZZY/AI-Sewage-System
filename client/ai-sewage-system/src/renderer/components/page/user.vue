<template>
  <div class="app-container">
    <div class="filter-container">
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
          <el-button @click="userArea(scope)">查看</el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="是否冻结" prop="delete_status">
        <template slot-scope="scope">
          <el-button v-if="scope.row.delete_status === 1" @click="frozenUser(scope)">已冻结</el-button>
          <el-button v-if="scope.row.delete_status === 0" @click="frozenUser(scope)">未冻结</el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作">
        <template slot-scope="scope">
          <el-button @click="showUpdate(scope)">编辑</el-button>
          <el-button @click="deleteUser(scope)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="listQuery.pageNum"
        :page-size="listQuery.pageRow"
        :total="totalCount"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-dialog title="地区选择" :visible.sync="dialogFormAreaVisible" append-to-body>
        <el-transfer v-model="allAreas" :data="allAreas"></el-transfer>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormAreaVisible = false">取 消</el-button>
          <el-button type="primary" @click="dialogFormAreaVisible = false">确 定</el-button>
        </div>
      </el-dialog>
      <el-form class="small-space" :model="tempUser" label-position="left" label-width="80px"
               style='width: 300px; margin-left:50px;'>
        <el-form-item label="用户名" required v-if="dialogStatus === 'create'">
          <el-input type="text" v-model="tempUser.username">
          </el-input>
        </el-form-item>
        <el-form-item label="用户名" required v-if="dialogStatus === 'update'">
          <span>{{tempUser.username}}</span>
        </el-form-item>
        <el-form-item label="密码" v-if="dialogStatus === 'create'" required>
          <el-input type="password" v-model="tempUser.password">
          </el-input>
        </el-form-item>
        <el-form-item label="新密码" v-if="dialogStatus === 'update'">
          <el-input type="password" v-model="tempUser.password" placeholder="不填则表示不修改">
          </el-input>
        </el-form-item>
        <el-form-item label="确认密码" v-if="dialogStatus === 'update'">
          <el-input type="password" v-model="tempUser.confirmPassword" placeholder="不填则表示不修改">
          </el-input>
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
          <el-button @click="dialogFormAreaVisible = true">查看/编辑</el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus==='create'" type="success" @click="createUser">创 建</el-button>
        <el-button type="primary" v-else @click="updateUser">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    name: 'user',
    data () {
      return {
        allAreas: [], // 从外部获取的所有市级列表
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
          deleteStatus: 0
        }
      }
    },
    created () {
      this.getList()
    },
    methods: {
      getList () {
        // 查询列表
        this.listLoading = true
        this.$http.get('http://localhost:8081/user/list', {
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
        this.tempUser.deleteStatus = ''
        this.dialogStatus = 'create'
        this.dialogFormVisible = true
      },
      showUpdate (scope) {
        console.log(scope)
        this.tempUser.username = scope.row.username
        this.tempUser.password = scope.row.password
        this.tempUser.confirmPassword = ''
        this.tempUser.identity = scope.row.identity
        this.tempUser.area = scope.row.area
        this.tempUser.deleteStatus = scope.row.deleteStatus
        this.dialogStatus = 'update'
        this.dialogFormVisible = true
      },
      createUser () {
        // 添加新用户
        this.api({
          url: 'http://localhost:8081/user/addUser',
          method: 'post',
          data: this.tempUser
        }).then(() => {
          this.getList()
          this.dialogFormVisible = false
        })
      },
      // 修改用户信息
      updateUser () {
        let _vue = this
        this.api({
          url: 'http://localhost:8081/user/updateUser',
          method: 'post',
          data: this.tempUser
        }).then(() => {
          let msg = '修改成功'
          this.dialogFormVisible = false
          if (this.userId === this.tempUser.userId) {
            msg = '修改成功,部分信息重新登录后生效'
          }
          this.$message({
            message: msg,
            type: 'success',
            duration: 1 * 1000,
            onClose: () => {
              _vue.getList()
            }
          })
        })
      },
      removeUser ($index) {
        let _vue = this
        this.$confirm('确定删除此用户?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          let user = _vue.list[$index]
          user.deleteStatus = '2'
          _vue.api({
            url: 'http://localhost:8081/user/updateUser',
            method: 'post',
            data: user
          }).then(() => {
            _vue.getList()
          }).catch(() => {
            _vue.$message.error('删除失败')
          })
        })
      }
    }
  }
  function chooseAreas (scope) {

  }
</script>
