const state = {
  username: '', // 用户名
  token: '', // axios请求附带的shiro验证Authorization
  identity: '', // shiro用户身份
  area: '' // 用户可见地区
}

const mutations = {
  SET_SHIRO_TOKEN (state, newData) {
    state.token = newData['shiroToken']
    state.identity = newData['identity']
    state.area = newData['area']
    state.username = newData['username']
  }
}

const actions = {
  setShiroToken ({ commit }, newData) {
    // do something async
    commit('SET_SHIRO_TOKEN', newData)
  }
}

export default {
  state,
  mutations,
  actions
}
