<template>
  <el-container style="border: 1px solid #eee">
    <el-header>
      <vHead></vHead>
    </el-header>
    <el-container>
      <el-aside width="300px">
        <vAside></vAside>
      </el-aside>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
  import vHead from './header'
  import vAside from './aside'
  export default {
    name: 'vMain',
    components: {
      vHead, vAside
    },
    data () {
      return {
        tokenTimer: null
      }
    },
    methods: {
      /**
       * @desc 定时刷新用于请求验证的token
       */
      refresh_token () {
        let refreshToken = window.jsonobj['refresh_token']
        let loginInfo = window.loginInfo
        loginInfo['refreshToken'] = refreshToken
        this.$http.post('http://localhost:8081/login/refreshToken', {
          loginInfo: loginInfo
        }, {
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(response => {
          console.log(response)
        }).catch(error => {
          console.log(error)
        })
      },
      /**
       * @desc 建立实时监控连接
       */
      createSignalRConnect () {
        let index = this.$store.state.Treedata.chooseData
        let signalrUrl = window.equipmentobjarray[index]['box']['cs']['signalrUrl']
        let token = window.jsonobj['access_token']
        this.$http.post('http://localhost:8081/equip/createSignalRConnect', {
          url: signalrUrl,
          token: token
        }, {
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(response => {
          console.log(response)
        }).catch(error => {
          console.log(error)
        })
      },
      closeSignalRConnect () {
        this.$http.post('http://localhost:8081/equip/closeSignalRConnect', null, {
          headers: {
            'Authorization': this.$store.state.ShiroToken.token
          }
        }).then(res => {
          console.log(res)
        }).catch(res => {
          console.log(res)
        })
      },
      /**
       * @desc 退出程序时清理数据的方法_(:з」∠)_
       */
      clearData () {
        this.closeSignalRConnect()
        this.$store.commit('TREEDATA_CLEAR')
        this.$store.commit('SHIRO_TOKEN_CLEAR')
      },
      /**
       * @desc 设置token刷新的timer定时器
       */
      setTokenTimer () {
        if (this.tokenTimer === null) {
          this.tokenTimer = setInterval(this.refresh_token, 600000)
        }
      }
    },
    created () {
      // 设置token刷新定时
      clearInterval(this.tokenTimer)
      this.tokenTimer = null
      this.setTokenTimer()
      // 启动实时数据连接
      this.createSignalRConnect()
    },
    mounted () {
      window.addEventListener('beforeunload', () => this.clearData())
    },
    destroyed () {
      clearInterval(this.tokenTimer)
      this.tokenTimer = null
      window.removeEventListener('beforeunload', () => this.clearData())
    }
  }
</script>

<style>
  html, body, #app, .el-container {
    padding: 0px;
    margin: 0px;
    height: 100%;
  }
  body {
    min-width: 1200px;
  }
  .el-header {
    background-color: #545c64;
    color: #333;
    padding-right: 0px;
  }
  .el-aside {
    color: #333;
    background-color: #545c64;
  }
  .el-tree {
    color: #ffffff;
    background-color: #545c64;
  }
  /* 修改树形节点hover样式 */
  .el-tree-node__content:hover {
    background-color: #454A50;
  }
  /* 修改树形节点点击样式 */
  .el-tree-node:focus>.el-tree-node__content{
    background-color: #454A50;
  }
</style>
