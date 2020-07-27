<template>
  <el-row>
    <el-col :span="7">
      <img :src="imgurl">
    </el-col>
    <el-col :span="14">
      <el-menu :router="true" :default-active="$route.path" class="el-menu" mode="horizontal" @select="handleSelect"
               background-color="black"
               text-color="#fff"
               active-text-color="#ffd04b">

        <el-menu-item index="/map">
          <!-- 加入map图标 -->
          <i class="el-icon-map-location"></i>
          站点地图
        </el-menu-item>
<!--        <el-menu-item index="/site_watch">-->
<!--        <el-submenu index="1">-->
<!--          <template slot="title">-->
<!--            <i class="el-icon-view"></i>-->
<!--            <span>站点监控</span>-->
<!--          </template>-->
        <el-menu-item index="/site_info">
          <i class="el-icon-info"></i>
          <span>站点信息</span>
        </el-menu-item>
        <el-menu-item index="/site_realtime_data">
          <i class="el-icon-data-line"></i>
          <span>实时数据</span>
        </el-menu-item>
        <el-menu-item index="/site_history_data">
          <i class="el-icon-s-data"></i>
          <span>历史数据</span>
        </el-menu-item>
<!--          &lt;!&ndash; 加入view图标 &ndash;&gt;-->
<!--          <i class="el-icon-view"></i>-->
<!--          站点监控-->
<!--        </el-submenu>-->
        <el-menu-item index="/remote_control" v-if="checkIdentity()">
          <!-- 加入promotion图标 -->
          <i class="el-icon-s-promotion"></i>
          远程控制
        </el-menu-item>
<!--        <el-menu-item index="/time_control" v-if="checkIdentity()">-->
<!--          &lt;!&ndash; 加入promotion图标 &ndash;&gt;-->
<!--          <i class="el-icon-time"></i>-->
<!--          定时控制-->
<!--        </el-menu-item>-->
        <el-menu-item index="/knowledge_graph">
          <!-- 加入map图标 -->
          <i class="el-icon-coin"></i>
          站点图谱
        </el-menu-item>
        <el-menu-item index="/job_control">
          <i class="el-icon-document"></i>
          工单管理
        </el-menu-item>
        <el-menu-item index="/user_manage" v-if="checkIdentity()">
          <!-- 加入custom图标 -->
          <i class="el-icon-s-custom"></i>
          用户管理
        </el-menu-item>
      </el-menu>
    </el-col>
    <el-col :span="3">
      <el-popover placement="bottom" trigger="click" width="150">
        <ul style="list-style-type: none; padding-left: 10px">
          <li v-for="info in getUserInform()">{{info}}</li>
        </ul>
        <el-button slot="reference" type="info" style="height: 60px; float: right">{{userName}}</el-button>
      </el-popover>
    </el-col>
  </el-row>
</template>

<script>
  export default {
    name: 'vHeader',
    data () {
      return {
        imgurl: require('@/assets/logo.png'),
        userName: '当前用户: ' + this.$store.state.ShiroToken.username
      }
    },
    methods: {
      handleSelect (key, keyPath) {
        console.log(key, keyPath)
      },
      checkIdentity () {
        let identity = this.$store.state.ShiroToken.identity
        return identity === 'admin'
      },
      getUserInform () {
        let identity = this.$store.state.ShiroToken.identity
        let phone = this.$store.state.ShiroToken.phone
        let mail = this.$store.state.ShiroToken.mail
        let informArray = []
        identity = this.checkIdentity() ? '管理员' : '普通用户'
        informArray.push('身份: ' + identity)
        informArray.push('手机: ' + phone)
        informArray.push('邮箱: ' + mail)
        return informArray
      }
    }
  }
</script>

<style scoped>
  .el-menu {
    z-index: 1000;
  }
</style>
