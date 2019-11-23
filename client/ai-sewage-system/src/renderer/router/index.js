import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)
export default new Router({
  routes: [
    {
      path: '/',
      name: 'log',
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
          path: '/side_watch',
          name: 'side_watch',
          component: require('@/components/page/side_watch').default
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
        },
        {
          path: '/knowledge_graph',
          name: 'knowledge_graph',
          component: require('@/components/page/knowledge_graph').default
        },
        {
          path: '/user_manage',
          name: 'user_manage',
          component: require('@/components/page/user_manage').default,
          menu: 'user'
        },
        {
          path: '/job_control',
          name: 'job_control',
          component: require('@/components/page/job_control').default
        },
        {
          path: '/job_create',
          name: 'job_create',
          component: require('@/components/page/job_create').default
        },
        {
          path: '/404',
          name: 'page404',
          component: require('@/components/page/404').default
        }
      ]
    },
    {
      path: '*',
      redirect: '/404'
    }
  ]
})
