const state = {
  treedata: [{label: '暂无数据', children: [{label: '暂无数据', netclass: 'null'}]}],
  chooseData: 0
}

const mutations = {
  SET_TREE_DATA (state, newdata) {
    state.treedata = null
    state.treedata = newdata
  },
  SET_CHOOSE_DATA (state, newData) {
    state.chooseData = null
    state.chooseData = newData
  }
}

const actions = {
  setTreedata ({ commit }, newdata) {
    // do something async
    commit('SET_TREE_DATA', newdata)
  },
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
