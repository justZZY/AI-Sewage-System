<template>
    <baidu-map class="map" :center="center" :zoom="zoom" :scroll-wheel-zoom="true"
               @click="getPoint" @ready="handler" mapType="BMAP_HYBRID_MAP">
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
      window.equipmentposarray = getEquipmentPosArray()
      return {
        activeIndex: '1',
        center: '云南大学呈贡校区',
        zoom: 12,
        equipposarray: window.equipmentposarray,
        defaultProps: {
          children: 'children',
          label: 'label'
        }
      }
    },
    methods: {
      // 获取地图对象
      async handler ({BMap, map}) {
        console.log(BMap, map)
        let treedata = await getTreeData(this.equipposarray, BMap)
        await this.$store.dispatch('setTreedata', treedata)
        window.BMap = BMap
        window.map = map
        // 在切换界面时返回记录坐标数据
        let posarray = window.equipmentposarray
        let index = this.$store.state.Treedata.chooseData
        let longitude = posarray[index]['pos']['longitude']
        let latitude = posarray[index]['pos']['latitude']
        let point = new window.BMap.Point(longitude, latitude)
        // 重新定位地图位置
        window.map.centerAndZoom(point, 12)
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
    return treedataarray
  }

  /*
  正常情况 matchaddr经过解析后应该长度为2代表省/市
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
</script>

<style>
  .map {
    width: 100%;
    height: 100%;
    overflow: hidden;
  }
</style>
