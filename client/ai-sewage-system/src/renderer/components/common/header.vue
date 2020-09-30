<template>
  <el-row>
    <el-col :span="5">
      <img :src="imgurl">
    </el-col>
    <el-col :span="16">
      <el-menu :router="true" :default-active="$route.path" class="el-menu" mode="horizontal" @select="handleSelect"
               background-color="black"
               text-color="#fff"
               active-text-color="#ffd04b">

        <el-menu-item index="/map">
          <i class="el-icon-map-location"></i>
          <span slot="title">站点地图</span>
        </el-menu-item>
<!--        <el-menu-item index="/site_watch">-->
<!--        <el-submenu index="1">-->
<!--          <template slot="title">-->
<!--            <i class="el-icon-view"></i>-->
<!--            <span>站点监控</span>-->
<!--          </template>-->
        <el-menu-item index="/site_info">
          <i class="el-icon-info"></i>
          <span slot="title">站点信息</span>
        </el-menu-item>
        <el-menu-item index="/remote_control" v-if="checkIdentity()">
          <!-- 加入promotion图标 -->
          <i class="el-icon-s-promotion"></i>
          <span slot="title">站点控制</span>
        </el-menu-item>
        <el-menu-item index="/site_realtime_data">
          <i class="el-icon-data-line"></i>
          <span slot="title">参数信息</span>
        </el-menu-item>
        <el-menu-item index="/site_data">
          <i class="el-icon-s-data"></i>
          <span slot="title">数据统计</span>
        </el-menu-item>
        <el-menu-item index="/site_monitor">
          <!-- 加入map图标 -->
          <i class="el-icon-video-camera"></i>
          <span slot="title">站点监控</span>
        </el-menu-item>
<!--        <el-menu-item index="/knowledge_graph">-->
<!--          &lt;!&ndash; 加入map图标 &ndash;&gt;-->
<!--          <i class="el-icon-coin"></i>-->
<!--          <span slot="title">站点图谱</span>-->
<!--        </el-menu-item>-->
        <el-menu-item index="/job_control">
          <i class="el-icon-document"></i>
          <span slot="title">运维管理</span>
        </el-menu-item>
        <el-menu-item index="/user_manage" v-if="checkIdentity()">
          <!-- 加入custom图标 -->
          <i class="el-icon-s-custom"></i>
          <span slot="title">用户管理</span>
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
        imgurl: require('@/assets/logo3.png'),
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
