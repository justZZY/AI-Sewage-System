import Vue from 'vue'
import axios from 'axios'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/zh-CN'
import BaiduMap from 'vue-baidu-map'
import App from './App'
import router from './router'
import store from './store'
import 'signalr'
import '@/icons' // icon
import './permission' // 权限
import {default as api} from './utils/api'
// 导入图标组件
import {hasPermission} from './utils/hasPermission'
import echarts from 'echarts'

Vue.prototype.hasPerm = hasPermission
Vue.prototype.api = api
Vue.use(ElementUI, {locale})

Vue.use(BaiduMap, {

  ak: 'fIaeDjiILX4C7VpyIkGed9BTjpbeaQ0w'

})

if (!process.env.IS_WEB) Vue.use(require('vue-electron'))

Vue.http = Vue.prototype.$http = axios

Vue.prototype.$echarts = echarts

Vue.config.productionTip = (process.env.NODE_ENV !== 'production')

/* eslint-disable no-new */

new Vue({

  components: { App },

  router,

  store,

  template: '<App/>',

  el: '#app',

  render: h => h(App)

})
