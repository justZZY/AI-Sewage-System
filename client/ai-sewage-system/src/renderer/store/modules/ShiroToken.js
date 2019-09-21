const state = {
  token: ''
}

const mutations = {
  SET_SHIRO_TOKEN (state, newData) {
    state.token = newData
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
