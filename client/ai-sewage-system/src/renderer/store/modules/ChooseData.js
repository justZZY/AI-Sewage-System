const state = {
  chooseData: 0
}

const mutations = {
  SET_CHOOSE_DATA (state, newData) {
    state.chooseData = null
    state.chooseData = newData
  }
}

const actions = {
  setChooseData ({ commit }, newData) {
    // do something async
    commit('SET_CHOOSE_DATA', newData)
  }
}

export default {
  state,
  mutations,
  actions
}
