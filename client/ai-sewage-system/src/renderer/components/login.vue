<template>
  <div class="login">
    <img class="welcome" src="../assets/welcome.png">
    <div class="login_parent">
      <div class="login_fields__user">
        <!--                <img class="icon-user" alt src="../assets/user_icon_copy.png">-->
        <input class="input-user" placeholder="用户名" maxlength="16" type="text" autocomplete="off">
      </div>
      <div class="login_fields__password">
        <img v-bind:src="eyeUrl" class="eye" v-on:click="changeInputState()">
        <!--                <img class="icon-password" alt src="../assets/lock_icon_copy.png">-->
        <input class="input-password" v-bind:type="inputType" placeholder="密码" maxlength="16">
      </div>
      <div class="login_fields__submit">
        <input type="button" value="登录">
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'login-page',
    data () {
      return {
        inputType: 'password'
        // 暂时没有图片资源
        // eyeUrl: require('../assets/eye-close.png')
      }
    },
    methods: {
      changeInputState () {
        if (this.inputType === 'password') {
          this.inputType = 'Text'
          // this.eyeUrl = require('../assets/eye-open.png')
        } else {
          this.inputType = 'password'
          // this.eyeUrl = require('../assets/eye-close.png')
        }
      }
    }
  }
</script>

<style scoped>
  body {
    font-family: "Source Sans Pro", sans-serif;
    background: #ea5c54;
  }

  .welcome {
    position: absolute;
    width: 200px;
    left: 0;
    right: 0;
    margin: auto;
  }

  .login_parent {
    background: #35394a;
    position: absolute;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    margin: auto;
    width: 240px;
    height: 400px;
  }

  /* 通过display: inline-block属性让子元素横向排列 */
  .login_fields__user {
    display: inline-block;
  }

  .icon-user {
    position: relative;
    top: 120px;
    left: 40px;
  }

  /* 在input中通过color属性控制光标的颜色 */
  .input-user {
    position: relative;
    top: 120px;
    left: 50px;
    width: 140px;
    color: #ea5c54;
    border: none;
    outline: none;
    background: rgba(57, 61, 82, 0);
  }

  /* 有焦点的时候输入框有蓝色阴影 */
  .input-user:focus {
    box-shadow: 0 0 5px rgba(81, 203, 238, 1);
    -webkit-box-shadow: 0 0 5px rgba(81, 203, 238, 1);
    -moz-box-shadow: 0 0 5px rgba(81, 203, 238, 1);
  }

  /* 设置placeholder颜色和字体 */
  ::-webkit-input-placeholder {
    color: #4e546d;
  }

  .icon-password {
    position: relative;
    top: 150px;
    left: 40px;
  }

  .eye {
    width: 25px;
    height: 20px;
    vertical-align: center;
    position: absolute;
    background-position: center;
    z-index: 1;
    background-repeat: no-repeat;
    background-attachment: fixed;
    margin-left: 170px;
    margin-top: 153px;
    margin-bottom: 25px;
  }

  .input-password {
    position: relative;
    top: 150px;
    left: 50px;
    width: 140px;
    color: #ea5c54;
    border: none;
    outline: none;
    background: rgba(57, 61, 82, 0);
  }

  .input-password:focus {
    box-shadow: 0 0 5px rgba(81, 203, 238, 1);
    -webkit-box-shadow: 0 0 5px rgba(81, 203, 238, 1);
    -moz-box-shadow: 0 0 5px rgba(81, 203, 238, 1);
  }

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
