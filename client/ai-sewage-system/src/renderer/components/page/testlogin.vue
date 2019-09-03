<template>
  <div class="login-container">
    <el-form autoComplete="on" :model="loginForm" :rules="loginRules" ref="loginForm" label-position="left"
             label-width="0px"
             class="card-box login-form">
      <h3 class="title">智慧水务</h3>
      <el-form-item prop="username">
        <span class="svg-container svg-container_login">
          <svg-icon icon-class="user"/>
        </span>
        <el-input v-model="loginForm.username" autoComplete="on"/>
      </el-form-item>
      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password"></svg-icon>
        </span>
        <el-input type="password" @keyup.enter.native="handleLogin" v-model="loginForm.password"
                  autoComplete="on"></el-input>
      </el-form-item>
<!--      <el-form-item prop="scope">-->
<!--        <span class="svg-container">-->
<!--          <svg-icon icon-class="scope"></svg-icon>-->
<!--        </span>-->
<!--        <el-input type="scope" @keyup.enter.native="handleLogin" v-model="loginForm.scope"-->
<!--                  autoComplete="on"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="client_id">-->
<!--        <span class="svg-container">-->
<!--          <svg-icon icon-class="client_id"></svg-icon>-->
<!--        </span>-->
<!--        <el-input type="client_id" @keyup.enter.native="handleLogin" v-model="loginForm.client_id"-->
<!--                  autoComplete="on"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="client_secret">-->
<!--        <span class="svg-container">-->
<!--          <svg-icon icon-class="client_secret"></svg-icon>-->
<!--        </span>-->
<!--        <el-input type="client_secret" @keyup.enter.native="handleLogin" v-model="loginForm.client_secret"-->
<!--                  autoComplete="on"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="grant-type">-->
<!--        <span class="svg-container">-->
<!--          <svg-icon icon-class="grant-type"></svg-icon>-->
<!--        </span>-->
<!--        <el-input type="grant-type" @keyup.enter.native="handleLogin" v-model="loginForm.grant_type"-->
<!--                  autoComplete="on"></el-input>-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" style="width:100%;" :loading="loading" @click.native.prevent="handleLogin">
          登录
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  export default {
    name: 'log',
    data () {
      console.log('in')
      return {
        loginForm: {
          username: 'ligaofa',
          password: 'gf229192',
          scope: 'openid offline_access fbox email profile',
          client_id: 'ynsk',
          client_secret: 'af6a37beddb28136eed65bda1f16547f',
          grant_type: 'password'
        },
        loginRules: {
          username: [{required: true, trigger: 'blur', message: '请输入用户名'}],
          password: [{required: true, trigger: 'blur', message: '请输入密码'}]
          // scope: [{required: true, trigger: 'blur', message: '请输入合法信息'}],
          // client_id: [{required: true, trigger: 'blur', message: '请输入合法信息'}],
          // client_secret: [{required: true, trigger: 'blur', message: '请输入合法信息'}],
          // grant_type: [{required: true, trigger: 'blur', message: '请输入合法信息'}]
        },
        loading: false
      }
    },
    methods: {
      handleLogin () {
        this.$refs.loginForm.validate(valid => {
          if (valid) {
            this.loading = true
            this.$store.dispatch('Login', this.loginForm).then(data => {
              this.loading = false
              window.userobj = JSON.parse(JSON.stringify(data))['data']
              console.log(window.userobj)
              if (window.userobj === 'success') {
                // this.$router.push('/login')
                this.runTestFun()
              } else {
                this.$message.error('账号/密码错误')
              }
            }).catch(() => {
              this.loading = false
            })
          } else {
            return false
          }
        })
      },
      runTestFun () {
        console.log('1')
        this.$http.post('http://localhost:8081/test/testLogin').then(response => {
          // 将获取到的登录消息数据设为全局,在后面的设备请求中会用到相关的token
          // alert(response)
          // console.log(response)
          window.jsonobj = JSON.parse(JSON.stringify(response))['data']
          console.log('access_token: ' + window.jsonobj['access_token'])
          console.log('refresh_token: ' + window.jsonobj['refresh_token'])
          // console.log(window.jsonobj['expires_in'])
          // console.log(window.jsonobj['token_type'])
          this.runTestEquipment()
        }).catch(function (error) {
          console.log(error)
        })
      },
      runTestEquipment () {
        this.$http.get('http://localhost:8081/test/testEquipments', {
          params: {
            Authorization: 'Bearer ' + window.jsonobj['access_token'],
            XFBoxClientId: 'zzy_test'
          }
        }).then(response => {
          // 解析请求到的设备数据
          window.equipmentobj = JSON.parse(JSON.stringify(response))['data']
          window.equipmentobjarray = getArray(window.equipmentobj)
          console.log(window.equipmentobj)
          // console.log('正式设备: ' + window.equipmentobj[0]['name'])
          // console.log('正式设备: ' + window.equipmentobj[0]['boxRegs'])
          this.gotoIndex()
        }).catch(function (error) {
          console.log(error)
        })
      },
      gotoIndex () {
        this.$router.push('/main')
      }
    }
  }
  function getArray (arrayObj) {
    let array = []
    for (let i = 0; i < arrayObj.length; i++) {
      array = array.concat(arrayObj[i]['boxRegs'])
    }
    return array
  }
</script>
<style rel="stylesheet/scss" lang="scss">
  // import "../../styles/mixin.scss";
  $bg: #2d3a4b;
  $dark_gray: #889aa4;
  $light_gray: #eee;

  .login-container {
    include :relative;
    height: 100vh;
    background-color: $bg;
    input:-webkit-autofill {
      -webkit-box-shadow: 0 0 0px 1000px #293444 inset !important;
      -webkit-text-fill-color: #fff !important;
    }
    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
    }
    .el-input {
      display: inline-block;
      height: 47px;
      width: 85%;
    }
    .tips {
      font-size: 14px;
      color: #fff;
      margin-bottom: 10px;
    }
    .svg-container {
      padding: 6px 5px 6px 15px;
      color: $dark_gray;
      vertical-align: middle;
      width: 30px;
      display: inline-block;
      &_login {
        font-size: 20px;
      }
    }
    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
    .login-form {
      position: absolute;
      left: 0;
      right: 0;
      width: 400px;
      padding: 35px 35px 15px 35px;
      margin: 120px auto;
    }
    .el-form-item {
      border: 1px solid rgba(255, 255, 255, 0.1);
      background: rgba(0, 0, 0, 0.1);
      border-radius: 5px;
      color: #454545;
    }
    .show-pwd {
      position: absolute;
      right: 10px;
      top: 7px;
      font-size: 16px;
      color: $dark_gray;
      cursor: pointer;
    }
    .thirdparty-button {
      position: absolute;
      right: 35px;
      bottom: 28px;
    }
  }
</style>
