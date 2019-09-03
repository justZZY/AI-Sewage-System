import Vue from 'vue'
import axios from 'axios'
/* 添加ElementUI组件 */
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
/* 添加百度地图组件 */
import BaiduMap from 'vue-baidu-map'
import App from './App'
import router from './router'
import store from './store'
import '../icons' // icon
import '../permission' // 权限
import {default as api} from '../utils/api'
import {hasPermission} from '../utils/hasPermission'
// 导入图标组件
import echarts from 'echarts'

Vue.prototype.api = api
Vue.prototype.hasPerm = hasPermission

Vue.use(ElementUI)
Vue.use(BaiduMap, {
  ak: 'fIaeDjiILX4C7VpyIkGed9BTjpbeaQ0w'
})

if (!process.env.IS_WEB) Vue.use(require('vue-electron'))
Vue.http = Vue.prototype.$http = axios
Vue.prototype.$echarts = echarts
Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  components: { App },
  router,
  store,
  template: '<App/>',
  el: '#app',
  render: h => h(App)
})
