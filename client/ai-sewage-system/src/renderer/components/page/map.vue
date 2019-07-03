<template>
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
</template>

<script>
  export default {
    name: 'vMap',
    data () {
      // 处理地图数据
      window.equipmentposarray = getEquipmentPosArray()
      return {
        activeIndex: '1',
        center: '昆明市',
        zoom: 8,
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
        this.$store.dispatch('setTreedata', treedata)
        window.BMap = BMap
        window.map = map
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
        classname = 'el-icon-apple'; break
      case 1:
        classname = 'el-icon-pear'; break
      case 2:
        classname = 'el-icon-orange'; break
      case 4:
        classname = 'el-icon-grape'; break
      case 5:
        classname = 'el-icon-sugar'; break
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

<style>
  .map {
    width: 100%;
    height: 100%;
    overflow: hidden;
  }

</style>
