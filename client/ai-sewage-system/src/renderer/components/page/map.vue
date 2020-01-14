<template>
    <baidu-map class="map" :center="center" :zoom="zoom" :scroll-wheel-zoom="true"
               @click="getPoint" @ready="handler" mapType="BMAP_NORMAL_MAP">
      <div v-for="pos in equipposarray" :key="pos.name">
        <bm-marker :position="{lng: pos.pos.longitude, lat: pos.pos.latitude}" @click="infoWindowCheck(pos)">
          <bm-info-window :show="pos.showflag" @close="infoWindowClose(pos)" @open="infoWindowOpen(pos)" style="font-size: 14px">
            <p>名称: {{pos.name}}</p>
            <p>地址: {{pos.address}}</p>
          </bm-info-window>
        </bm-marker>
      </div>
    </baidu-map>
</template>

<script>
  export default {
    name: 'vMap',
    data () {
      // 处理地图数据
      let index = this.$store.state.Treedata.chooseData
      window.equipmentposarray = getEquipmentPosArray()
      return {
        equipposarray: window.equipmentposarray,
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        center: {
          lng: window.equipmentposarray[index]['pos']['longitude'],
          lat: window.equipmentposarray[index]['pos']['longitude']
        },
        zoom: 9
      }
    },
    methods: {
      // 获取地图对象
      async handler ({BMap, map}) {
        console.log(BMap, map)
        // 如果vuex里面存有树节点数据则不需要重新计算节点坐标
        // 这样可以保证每次进入程序后只进行一次节点的计算
        if (this.$store.state.Treedata.treedata.length === 1 && this.$store.state.Treedata.treedata[0].label === '暂无数据') {
          let treeData = await getTreeData(this.equipposarray, BMap)
          this.getPermissionSiteTreeByCity(treeData, window.equipmentposarray)
        }
        window.BMap = BMap
        window.map = map
        // 在切换界面时返回记录坐标数据
        let posarray = window.equipmentposarray
        let index = this.$store.state.Treedata.chooseData
        console.log(posarray[index])
        let longitude = posarray[index]['pos']['longitude']
        let latitude = posarray[index]['pos']['latitude']
        let point = new BMap.Point(longitude, latitude)
        console.log(point)
        // 重新定位地图位置
        map.centerAndZoom(point, 9)
        // 关闭以前打开的地图窗口并开启新的地图窗口
        for (let j = 0; j < posarray.length; j++) {
          posarray[j]['showflag'] = false
        }
        posarray[index]['showflag'] = true
      },
      // 点击地图获取一些信息
      getPoint (e) {
        // this.show = true
      },
      infoWindowCheck (pos) {
        pos.showflag = !pos.showflag
      },
      infoWindowClose (pos) {
        pos.showflag = false
      },
      infoWindowOpen (pos) {
        pos.showflag = true
      },
      /**
       * @desc 做城市节点筛选
       */
      getPermissionSiteTreeByCity (originTree, equipArray) {
        let cityArray = this.$store.state.ShiroToken.area.split(';')
        cityArray.pop()
        console.log(originTree)
        console.log(cityArray)
        let ansTree = []
        for (let i = 0; i < originTree.length; i++) {
          for (let j = 0; j < originTree[i]['children'].length; j++) {
            for (let k = 0; k < originTree[i]['children'][j]['children'].length; k++) {
              if (checkCityInArray(originTree[i]['children'][j]['children'][k].label, cityArray)) {
                ansTree = insertCity(originTree[i]['label'], originTree[i]['children'][j]['label'],
                  originTree[i]['children'][j]['children'][k], ansTree)
              }
            }
          }
        }
        console.log(ansTree)
        // 更新树
        this.$store.dispatch('setTreedata', ansTree)
        // 更新选项节点
        let chooseIndex = 0
        for (let i = 0; i < equipArray.length; i++) {
          if (equipArray[i].name === ansTree[0]['children'][0]['children'][0].label) {
            chooseIndex = i
          }
        }
        this.$store.dispatch('setChooseData', chooseIndex)
      }
    }
  }
  // 解析全局设备信息中的位置数据
  function getEquipmentPosArray () {
    const equipmentobjarray = window.equipmentobjarray
    let equipmentposarray = []
    for (let i = 0; i < equipmentobjarray.length; i++) {
      let name = equipmentobjarray[i]['alias']
      let address = equipmentobjarray[i]['box']['address']
      let longitude = equipmentobjarray[i]['box']['longitude']
      let latitude = equipmentobjarray[i]['box']['latitude']
      let net = equipmentobjarray[i]['box']['net']
      let connectionState = equipmentobjarray[i]['box']['connectionState']
      let posobj = {
        'name': name,
        'address': address,
        'net': net,
        'showflag': false,
        'connectionState': connectionState,
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
      let connect = posarray[i]['connectionState']
      let matchaddr = await callbackAddrArray(longitude, latitude, BMap)
      console.log('province: ' + matchaddr[0] + 'city: ' + matchaddr[1])
      treedataarray = insertMatchAddr(treedataarray, matchaddr, name, net, connect)
    }
    // 将站点排序, 连接正常的站点优先排序
    treedataarray = sortTree(treedataarray)
    return treedataarray
  }

  /**
   * @desc 正常情况 matchaddr经过解析后应该长度为2代表省/市
           其他情况下可以视为错误地址
  */
  function insertMatchAddr (treedataarray, matchaddr, name, net, connect) {
    let province = matchaddr[0]
    let city = matchaddr[1]
    // 标记是否已经在循环时执行过插入
    let provinceflag = false
    let provinceindex = -1
    let cityflag = false
    let netclass = getNetIconClass(net)
    let connectClass = getConnectClass(connect)
    // 遍历查找是否存在相同的省
    for (let i = 0; i < treedataarray.length; i++) {
      if (treedataarray[i]['label'] === province) {
        provinceflag = true // 存在同省
        for (let j = 0; j < treedataarray[i]['children'].length; j++) {
          if (treedataarray[i]['children'][j]['label'] === city) {
            treedataarray[i]['children'][j]['children'].push({label: name, netClass: netclass, connectClass: connectClass})
            cityflag = true // 存在同市
          }
        }
        provinceindex = i // 记录同省在地址数据中的下标,创建新市的时候做指向
      }
    }
    // 表示新的省市
    if (!provinceflag && !cityflag) {
      treedataarray.push({label: province, children: [{label: city, children: [{label: name, netClass: netclass, connectClass: connectClass}]}]})
    }
    // 表示已经存在省但不存在市
    if (provinceflag && !cityflag) {
      treedataarray[provinceindex]['children'].push({label: city, children: [{label: name, netClass: netclass, connectClass: connectClass}]})
    }
    return treedataarray
  }

  // 网络信号图标类
  function getNetIconClass (id) {
    let classname = ''
    switch (id) {
      case 0: classname = 'iconfont icon-Network-Error'; break
      case 1: classname = 'iconfont icon-internet'; break
      case 2: classname = 'iconfont icon-G-Network'; break
      case 4: classname = 'iconfont icon-Wifi-'; break
      case 5: classname = 'iconfont icon-G-Network1'; break
      default: classname = 'iconfont icon-Network-Error'
    }
    return classname
  }

  // 连接图标类
  function getConnectClass (connect) {
    let classname = ''
    if (connect === 1) {
      classname = 'el-icon-success'
    } else {
      classname = 'el-icon-warning-outline'
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
  /**
   * @desc 检测城市是否在有权限的城市列表中
   */
  function checkCityInArray (city, cityArray) {
    if (cityArray[0] === 'all') {
      return true
    } else {
      for (let i = 0; i < cityArray.length; i++) {
        if (city === cityArray[i]) {
          return true
        }
      }
      return false
    }
  }

  /**
   * @desc 做权限城市的插值 细化到每个站点
   */
  function insertCity (province, city, siteInfo, ansTree) {
    // 检查省份是否存在
    let provinceIndex = -1
    let cityIndex = -1
    for (let i = 0; i < ansTree.length; i++) {
      if (province === ansTree[i]['label']) {
        provinceIndex = i
      }
    }
    if (provinceIndex === -1) {
      // 因为是第一次插入 直接插入整条地图路径
      ansTree.push({label: province, children: [{label: city, children: [siteInfo]}]})
    } else {
      // 存在已有省份的情况下
      // 检查所在城市是否存在
      for (let i = 0; i < ansTree[provinceIndex]['children'].length; i++) {
        if (city === ansTree[provinceIndex]['children'][i].label) {
          cityIndex = i
        }
      }
      if (cityIndex === -1) {
        // 如果该城市不存在
        ansTree[provinceIndex]['children'].push({label: city, children: [siteInfo]})
      } else {
        ansTree[provinceIndex]['children'][cityIndex]['children'].push(siteInfo)
      }
    }
    return ansTree
  }
  /**
   * @desc 对树节点按照是否连接优先级进行排序
   */
  function sortTree (tree) {
    for (let i = 0; i < tree.length; i++) {
      for (let j = 0; j < tree[i].children.length; j++) {
        let siteList = tree[i].children[j].children
        siteList.sort((a, b) => {
          if (a.connectClass === 'el-icon-success' && b.connectClass === 'el-icon-warning-outline') {
            return -1
          } else {
            return 1
          }
        })
      }
    }
    return tree
  }
</script>

<style>
  .map {
    width: 100%;
    height: 100%;
    overflow: hidden;
  }
</style>
