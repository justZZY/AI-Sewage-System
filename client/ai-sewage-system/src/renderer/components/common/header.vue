<template>
  <el-row>
    <el-col :span="7">
      <img :src="imgurl">
    </el-col>
    <el-col :span="12">
      <el-menu :router="true" :default-active="$route.path" class="el-menu" mode="horizontal" @select="handleSelect"
               background-color="#545c64"
               text-color="#fff"
               active-text-color="#ffd04b">
        <el-menu-item index="/map">站点地图</el-menu-item>
        <el-menu-item index="/side_watch">站点监控</el-menu-item>
        <el-menu-item index="/remote_control">远程控制</el-menu-item>
        <el-menu-item index="/time_control">定时控制</el-menu-item>
        <el-menu-item index="/system_manage">系统管理</el-menu-item>
      </el-menu>
    </el-col>
  </el-row>
</template>

<script>
  export default {
    name: 'vHeader',
    data () {
      return {
        imgurl: require('@/assets/logo.png')
      }
    },
    methods: {
      handleSelect (key, keyPath) {
        console.log(key, keyPath)
      }
    }
  }
</script>

<style scoped>
  .el-menu {
    z-index: 1000;
  }
</style>
