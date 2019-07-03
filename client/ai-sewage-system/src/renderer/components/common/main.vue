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
