<template>
  <div class="login">
      <div class="login_fields__submit">
        <input type="button" value="测试" v-on:click="runTestFun()">
        <input type="button" value="设备测试" v-on:click="runTestEquipment()">
        <input type="button" value="跳转" v-on:click="gotoIndex()">
      </div>
  </div>
</template>

<script>
  export default {
    name: 'login-page',
    methods: {
      runTestFun () {
        this.$http.post('http://localhost:8081/test/testLogin').then(function (response) {
          // 将获取到的登录消息数据设为全局,在后面的设备请求中会用到相关的token
          window.jsonobj = JSON.parse(JSON.stringify(response))['data']
          console.log('access_token: ' + window.jsonobj['access_token'])
          console.log('refresh_token: ' + window.jsonobj['refresh_token'])
          console.log(window.jsonobj['expires_in'])
          console.log(window.jsonobj['token_type'])
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
        }).then(function (response) {
          // 解析请求到的设备数据
          window.equipmentobj = JSON.parse(JSON.stringify(response))['data']
          window.equipmentobjarray = window.equipmentobj[0]['boxRegs']// .concat(window.equipmentobj[1]['boxRegs'])
          console.log(window.equipmentobj)
          console.log(window.equipmentobjarray)
          console.log('正式设备: ' + window.equipmentobj[0]['name'])
          console.log('正式设备: ' + window.equipmentobj[0]['boxRegs'])
        }).catch(function (error) {
          console.log(error)
        })
      },
      gotoIndex () {
        this.$router.push('/main')
      }
    }
  }
</script>

<style scoped>
  .login_fields__submit {
    position: relative;
    top: 190px;
    left: 50px;
  }

  .login_fields__submit input {
    border-radius: 50px;
    background: transparent;
    padding: 10px 50px;
    border: 2px solid #4fa1d9;
    color: #4fa1d9;
    font-size: 11px;
    -webkit-transition-property: background, color;
    transition-property: background, color;
    -webkit-transition-duration: 0.2s;
    transition-duration: 0.2s;
  }

  .login_fields__submit input:focus {
    box-shadow: none;
    outline: none;
  }

  .login_fields__submit input:hover {
    color: white;
    background: #4fa1d9;
    -webkit-transition-property: background, color;
    transition-property: background, color;
    -webkit-transition-duration: 0.2s;
    transition-duration: 0.2s;
  }
</style>
