// import {getInfo, login, logout} from '../../../untils/api/login'
import {removeToken, setToken} from '../../../utils/auth'
// import {default as api} from '../../../utils/api'
import store from '../../store'
import router from '../../router'
import axios from 'axios'
import qs from 'qs'

const user = {
  state: {
    userId: '',
    avatar: 'https://www.gravatar.com/avatar/6560ed55e62396e40b34aac1e5041028',
    role: '',
    menus: [],
    permissions: []
  },
  mutations: {
    SET_USER: (state, userInfo) => {
      console.log(userInfo['info']['userInfo']['userId'])
      state.userId = userInfo['info']['userInfo']['userId']
      state.role = userInfo['info']['userPermission']['roleName']
      state.menus = userInfo['info']['userPermission']['menuList']
      state.permissions = userInfo['info']['userPermission']['permissionList']
    },
    RESET_USER: (state) => {
      state.userId = ''
      state.role = ''
      state.menus = []
      state.permissions = []
    }
  },
  actions: {
    // 登录
    Login ({commit, state}, loginForm) {
      return new Promise((resolve, reject) => {
        console.log('====send')
        axios({
          url: 'http://localhost:8081/login/auth',
          method: 'post',
          data: qs.stringify(loginForm)
        }).then(data => {
          window.obj = JSON.parse(JSON.stringify(data))['data']
          // console.log(window.obj)
          if (window.obj === 'success') {
            // cookie中保存前端登录状态
            console.log('====remember')
            setToken()
            console.log(setToken())
          }
          resolve(data)
        }).catch(err => {
          reject(err)
        })
      })
      //   api({
      //     url: 'http://localhost:8081/login/auth',
      //     method: 'post',
      //     data: loginForm
      //   }).then(data => {
      //     if (data.result === 'success') {
      //       // cookie中保存前端登录状态
      //       setToken()
      //     }
      //     resolve(data)
      //   }).catch(err => {
      //     reject(err)
      //   })
      // })
    },
    // 获取用户信息
    GetInfo ({commit, state}) {
      return new Promise((resolve, reject) => {
        axios({
          url: 'http://localhost:8081/login/getInfo',
          method: 'post'
        }).then(data => {
          console.log('=====get')
          // 储存用户信息
          console.log(data)
          commit('SET_USER', data.data)
          // cookie保存登录状态,仅靠vuex保存的话,页面刷新就会丢失登录状态
          setToken()
          // 生成路由
          let userPermission = data.data.info.userPermission
          store.dispatch('GenerateRoutes', userPermission).then(() => {
            // 生成该用户的新路由json操作完毕之后,调用vue-router的动态新增路由方法,将新路由添加
            router.addRoutes(store.getters.addRouters)
          })
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 登出
    LogOut ({commit}) {
      return new Promise((resolve) => {
        axios({
          url: 'http://localhost:8081/login/logout',
          method: 'post'
        }).then(data => {
          commit('RESET_USER')
          removeToken()
          resolve(data)
        }).catch(() => {
          commit('RESET_USER')
          removeToken()
        })
      })
    },
    // 前端 登出
    FedLogOut ({commit}) {
      return new Promise(resolve => {
        commit('RESET_USER')
        removeToken()
        resolve()
      })
    }
  }
}
export default user
