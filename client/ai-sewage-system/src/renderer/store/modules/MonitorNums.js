const state = {
  monitorNums: []
}

const mutations = {
  SET_MONITOR_NUMS (state, newData) {
    state.monitorNums = []
    state.monitorNums = newData
  }
}

const actions = {
  setMonitorNums ({ commit }, newData) {
    // do something async
    commit('SET_MONITOR_NUMS', newData)
  }
}

export default {
  state,
  mutations,
  actions
}
