<template>
  <div class="aside">
    <el-tree :data="equiptreedata" :props="defaultProps" @node-click="handleNodeClick" :default-expand-all="true">
          <span slot-scope="{node, data}">
            <i :class="data.netclass"></i>
            <span style="padding-left: 4px;">{{node.label}}</span>
          </span>
    </el-tree>
  </div>
</template>

<script>
  export default {
    name: 'vAside',
    data () {
      // 处理地图树形数据,分割出省市信息-迁移到地图初始化后返回对象初始化
      let treedata = this.$store.state.treedata
      return {
        equiptreedata: treedata,
        defaultProps: {
          children: 'children',
          label: 'label'
        }
      }
    },
    methods: {
      // 树形节点点击事件
      handleNodeClick (data) {
        if (data['children'] === undefined) {
          let posarray = window.equipmentposarray
          for (let i = 0; i < posarray.length; i++) {
            if (posarray[i].name === data['label']) {
              let longitude = posarray[i]['pos']['longitude']
              let latitude = posarray[i]['pos']['latitude']
              let point = new window.BMap.Point(longitude, latitude)
              // 重新定位地图位置
              window.map.centerAndZoom(point, 12)
              // 关闭以前打开的地图窗口并开启新的地图窗口
              for (let j = 0; j < posarray.length; j++) {
                posarray[j]['showflag'] = false
              }
              posarray[i]['showflag'] = true
            }
          }
        }
        console.log(data)
      }
    }
  }
</script>

<style>
  .aside {
    padding-left: 12px;
  }
</style>
