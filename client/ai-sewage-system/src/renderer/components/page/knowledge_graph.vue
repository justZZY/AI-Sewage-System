<template>
  <div id="main" style="width: 100%; height: 100%" ref="main">
  </div>
</template>

<script>
  import {relationOption} from '../../js/chart'
  let echarts = require('echarts')
  const size = 80
  let nodeList = []
  let linkList = []
  export default {
    name: 'knowledge_graph',
    mounted () {
      this.initGraph()
    },
    methods: {
      async initGraph () {
        const myGraph = echarts.init(this.$refs.main)
        const option = await this.initOption()
        console.log(option)
        myGraph.setOption(option)
      },
      async initOption () {
        let option = JSON.parse(JSON.stringify(relationOption))
        let categoryList = this.getCategory()
        await this.getNodeData(categoryList)
        console.log('====after init')
        nodeList = unique(nodeList)
        console.log(nodeList)
        console.log(linkList)
        option.series[0].data = nodeList
        option.series[0].links = linkList
        option.series[0].categories = categoryList
        return option
      },
      getCategory () {
        console.log('getCategory')
        let sideList = this.$store.state.ShiroToken.area
        let categoryList = sideList.split(';')
        categoryList.pop()
        console.log(categoryList)
        return categoryList
      },
      /*
       * @des 用于格式化图谱数据
       */
      async getNodeData (categoryList) {
        console.log('getNodeData')
        let allSiteList = window.equipmentobjarray
        for (let i = 0; i < categoryList.length; i++) {
          console.log(categoryList[i])
          // 获取账户拥有的站点
          let temp = allSiteList.find(item => item.alias === categoryList[i])
          console.log(temp)
          nodeList.push({
            name: temp.alias,
            des: temp.box.address,
            category: i,
            symbolSize: size,
            draggable: 'true',
            label: {normal: {show: true}}
          })
          let equipList = await this.getEquipMonitor(temp)
          console.log('====after axios')
          equipList = equipList.data
          console.log(equipList)
          for (let j = 0; j < equipList.length; j++) {
            // 这个lastEquipName用来保证站点与设备只创建一次链接
            let lastEquipName = ''
            let linkName = equipList[j].name
            let linkSource = temp.alias
            for (let k = 0; k < equipList[j].items.length; k++) {
              let equipID = equipList[j].items[k].id
              let equipTemp = equipList[j].items[k].name.split('_')
              let equipName = equipTemp[1] + '_' + linkSource
              let equipSubName = equipTemp[2] + '_' + equipID
              nodeList.push({
                name: equipName,
                category: i,
                symbolSize: size / 1.5,
                draggable: true,
                label: {normal: {show: true}}
              })
              nodeList.push({
                name: equipSubName,
                category: i,
                symbolSize: size / 2,
                draggable: true,
                label: {normal: {show: false}}
              })
              if (lastEquipName !== equipName) {
                setLinkData(linkName, linkSource, equipName)
                lastEquipName = equipName
              }
              setLinkData('功能', equipName, equipSubName)
            }
          }
        }
        console.log(nodeList)
      },
      getEquipMonitor (sideData) {
        return new Promise((resolve) => {
          let authorization = 'Bearer ' + window.jsonobj['access_token']
          let apiBaseUrl = sideData['box']['cs']['apiBaseUrl']
          let boxNo = sideData['box']['boxNo']
          this.$http.post('http://116.55.241.28:8082/equip/getEquipMonitor', {
            authorization: authorization,
            apiBaseUrl: apiBaseUrl,
            boxNo: boxNo
          }, {
            headers: {
              'Authorization': this.$store.state.ShiroToken.token
            }
          }).then(res => {
            resolve(res)
          })
        })
      }
    }
  }
  function setLinkData (linkName, linkSource, linkTarget) {
    linkList.push({
      source: linkSource,
      target: linkTarget,
      name: linkName,
      lineStyle: {
        normal: {
          color: 'source'
        }
      }
    })
  }
  function unique (arr) {
    const res = new Set()
    return arr.filter((a) => !res.has(a.name) && res.add(a.name))
  }
</script>

<style scoped>

</style>
