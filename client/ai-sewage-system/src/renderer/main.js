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

Vue.use(ElementUI)
Vue.use(BaiduMap, {
  ak: 'fIaeDjiILX4C7VpyIkGed9BTjpbeaQ0w'
})

if (!process.env.IS_WEB) Vue.use(require('vue-electron'))
Vue.http = Vue.prototype.$http = axios
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
