const state = {
  rateNums: []
}

const mutations = {
  SET_RATE_NUMS (state, newData) {
    state.rateNums = []
    state.rateNums = newData
  }
}

const actions = {
  setRateNums ({ commit }, newData) {
    // do something async
    commit('SET_RATE_NUMS', newData)
  }
}

export default {
  state,
  mutations,
  actions
}
