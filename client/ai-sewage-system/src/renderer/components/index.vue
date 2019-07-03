<template>
  <el-container style="border: 1px solid #eee">
    <el-aside width="255px">
      <div class="aside-icon"> icon_test</div>
      <el-tree :data="equiptreedata" :props="defaultProps" @node-click="handleNodeClick" :default-expand-all="true">
          <span slot-scope="{ node, data }">
            <i :class="data.netclass"></i>
            <span style="padding-left: 4px;">{{node.label}}</span>
          </span>
      </el-tree>
    </el-aside>
    <el-container>
      <el-header>
        <el-menu :default-active="activeIndex" class="el-menu" mode="horizontal" @select="handleSelect"
                 background-color="#545c64"
                 text-color="#fff"
                 active-text-color="#ffd04b">
          <el-menu-item index="1">站点地图</el-menu-item>
          <el-submenu index="2">
            <template slot="title">站点监控</template>
            <el-menu-item index="2-1">选项1</el-menu-item>
            <el-menu-item index="2-2">选项2</el-menu-item>
            <el-menu-item index="2-3">选项3</el-menu-item>
            <el-submenu index="2-4">
              <template slot="title">选项4</template>
              <el-menu-item index="2-4-1">选项1</el-menu-item>
              <el-menu-item index="2-4-2">选项2</el-menu-item>
              <el-menu-item index="2-4-3">选项3</el-menu-item>
            </el-submenu>
          </el-submenu>
          <el-submenu index="3">
            <template slot="title">站点控制</template>
            <el-menu-item index="3-1">远程控制</el-menu-item>
            <el-menu-item index="3-2">定时控制</el-menu-item>
          </el-submenu>
          <el-menu-item index="4">运营维护</el-menu-item>
          <el-menu-item index="5">系统管理</el-menu-item>
          <!--<el-menu-item index="4"><a href="https://www.ele.me" target="_blank">订单管理</a></el-menu-item>-->
        </el-menu>
      </el-header>
      <el-main>
        <baidu-map class="map" :center="center" :zoom="zoom" :scroll-wheel-zoom="true" @click="getPoint" @ready="handler">
          <div v-for="pos in equipposarray">
            <bm-marker :position="{lng: pos.pos.longitude, lat: pos.pos.latitude}" @click="infoWindowCheck(pos)">
              <bm-info-window :show="pos.showflag" @close="infoWindowClose(pos)" @open="infoWindowOpen(pos)" style="font-size: 14px">
                <p>名称: {{pos.name}}</p>
                <p>地址: {{pos.address}}</p>
              </bm-info-window>
            </bm-marker>
          </div>
        </baidu-map>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
  export default {
    name: 'index',
    data () {
      // 处理地图数据
      let equipmentposarray = getEquipmentPosArray()
      // 处理地图树形数据,分割出省市信息-迁移到地图初始化后返回对象初始化
      // let treedata = getTreeData(equipmentposarray)
      let treedata = [{label: '暂无数据', children: [{label: '暂无数据', netclass: 'el-icon-loading'}]}]
      console.log(equipmentposarray)
      console.log(treedata)
      return {
        activeIndex: '1',
        center: '昆明市',
        zoom: 8,
        equipposarray: equipmentposarray,
        equiptreedata: treedata,
        defaultProps: {
          children: 'children',
          label: 'label'
        }
      }
    },
    methods: {
      handleSelect (key, keyPath) {
        console.log(key, keyPath)
      },
      // 获取地图对象
      async handler ({BMap, map}) {
        console.log(BMap, map)
        this.equiptreedata = await getTreeData(this.equipposarray, BMap)
        window.BMap = BMap
        window.map = map
      },
      // 点击地图获取一些信息
      getPoint (e) {
        // this.show = true
      },
      infoWindowCheck (pos) {
        if (pos.showflag) {
          pos.showflag = false
        } else {
          pos.showflag = true
        }
      },
      infoWindowClose (pos) {
        pos.showflag = false
      },
      infoWindowOpen (pos) {
        pos.showflag = true
      },
      // 树形节点点击事件
      handleNodeClick (data) {
        if (data['children'] === undefined) {
          let posarray = this.equipposarray
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

  // 解析全局设备信息中的位置数据
  function getEquipmentPosArray () {
    const equipmentobjarray = window.equipmentobj[0]['boxRegs']
    let equipmentposarray = []
    for (let i = 0; i < equipmentobjarray.length; i++) {
      let name = equipmentobjarray[i]['alias']
      let address = equipmentobjarray[i]['box']['address']
      let longitude = equipmentobjarray[i]['box']['longitude']
      let latitude = equipmentobjarray[i]['box']['latitude']
      let net = equipmentobjarray[i]['box']['net']
      let posobj = {
        'name': name,
        'address': address,
        'net': net,
        'showflag': false,
        'pos': {
          'longitude': longitude,
          'latitude': latitude
        }
      }
      equipmentposarray.push(posobj)
    }
    return equipmentposarray
  }

  // 从位置信息中获取到相关省市信息的树形目录
  async function getTreeData (posarray, BMap) {
    let treedataarray = []
    for (let i = 0; i < posarray.length; i++) {
      let name = posarray[i]['name']
      let net = posarray[i]['net']
      let longitude = posarray[i]['pos']['longitude']
      let latitude = posarray[i]['pos']['latitude']
      let matchaddr = await callbackAddrArray(longitude, latitude, BMap)
      console.log('province: ' + matchaddr[0] + 'city: ' + matchaddr[1])
      treedataarray = insertMatchAddr(treedataarray, matchaddr, name, net)
    }
    return treedataarray
  }

  /*
  正常情况 matchaddr经过正则解析后应该长度为2代表省/市
  其他情况下可以视为错误地址
  */
  function insertMatchAddr (treedataarray, matchaddr, name, net) {
    let province = matchaddr[0]
    let city = matchaddr[1]
    // 标记是否已经在循环时执行过插入
    let provinceflag = false
    let provinceindex = -1
    let cityflag = false
    let netclass = getNetIconClass(net)
    // 遍历查找是否存在相同的省
    for (let i = 0; i < treedataarray.length; i++) {
      if (treedataarray[i]['label'] === province) {
        provinceflag = true // 存在同省
        for (let j = 0; j < treedataarray[i]['children'].length; j++) {
          if (treedataarray[i]['children'][j]['label'] === city) {
            treedataarray[i]['children'][j]['children'].push({label: name, netclass: netclass})
            cityflag = true // 存在同市
          }
        }
        provinceindex = i // 记录同省在地址数据中的下标,创建新市的时候做指向
      }
    }
    // 表示新的省市
    if (!provinceflag && !cityflag) {
      treedataarray.push({label: province, children: [{label: city, children: [{label: name, netclass: netclass}]}]})
    }
    // 表示已经存在省但不存在市
    if (provinceflag && !cityflag) {
      treedataarray[provinceindex]['children'].push({label: city, children: [{label: name, netclass: netclass}]})
    }
    return treedataarray
  }

  /*
  这个等找到图标之后再做 先用elementui图标代替
  function getNetIconClass (id) {
    let classname = ''
    switch (id) {
      case 0: classname = 'unknown_icon'; break
      case 1: classname = 'ethernet_icon'; break
      case 2: classname = 'gprs_icon'; break
      case 3: classname = 'wifi_icon'; break
      case 4: classname = '4g_icon'; break
      default: classname = 'unknown_icon'
    }
    return classname
  }
   */
  function getNetIconClass (id) {
    let classname = ''
    switch (id) {
      case 0:
        classname = 'el-icon-apple'
        break
      case 1:
        classname = 'el-icon-pear'
        break
      case 2:
        classname = 'el-icon-orange'
        break
      case 4:
        classname = 'el-icon-grape'
        break
      case 5:
        classname = 'el-icon-sugar'
        break
      default:
        classname = 'el-icon-apple'
    }
    return classname
  }

  function getAddress (longitude, latitude, BMap) {
    let geocoder = new BMap.Geocoder()
    let point = new BMap.Point(longitude, latitude)
    console.log('longitude: ' + longitude + ' latitude: ' + latitude)
    return new Promise((resolve) => {
      geocoder.getLocation(point, (rs) => {
        let addcomp = rs.addressComponents
        let addr = []
        if (longitude === 0 && latitude === 0) {
          addr[0] = '未标记地区'
          addr[1] = '未标记城市'
        } else {
          addr[0] = addcomp.province
          addr[1] = addcomp.city
        }
        resolve(addr)
      })
    })
  }

  async function callbackAddrArray (longitude, latitude, BMap) {
    let array = await getAddress(longitude, latitude, BMap)
    return array
  }
</script>

<style type="text/css">
  html, body, #app, .el-container {
    padding: 0px;
    margin: 0px;
    height: 100%;
  }

  .el-header {
    background-color: #545c64;
    color: #333;
    float: left;
  }

  .aside-icon {
    line-height: 60px;
    height: 60px;
    color: #ffffff;
    padding-left: 20px;
    background-color: #545c64;
  }

  .header-menu, .el-menu {
    width: 90%;
    height: 60px;
  }

  .header-account {
    width: 30%;
    line-height: 60px;
    float: left;
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
  .el-main {
    padding: 0px;
    margin: 0px;
  }

  .map {
    width: 100%;
    height: 100%;
    overflow: hidden;
  }
</style>
