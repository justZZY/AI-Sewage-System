import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'login-page',
      component: require('@/components/page/login').default
    },
    {
      path: '/main',
      name: 'vMain',
      redirect: '/map',
      component: require('@/components/common/main').default,
      children: [
        {
          path: '/map',
          name: 'vMap',
          component: require('@/components/page/map').default
        },
        {
          path: '/test',
          name: 'vTest',
          component: require('@/components/page/test').default
        },
        {
          path: '/404',
          name: 'page404',
          component: require('@/components/page/404').default
        },
        {
          path: '/remote_control',
          name: 'remote_control',
          component: require('@/components/page/remote_control').default
        },
        {
          path: '/time_control',
          name: 'time_control',
          component: require('@/components/page/time_control').default
        }
      ]
    },
    {
      path: '*',
      redirect: '/404'
    }
  ]
})
