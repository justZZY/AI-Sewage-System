const state = {
  treedata: [{label: '暂无数据', children: [{label: '暂无数据', netclass: 'null'}]}]
}

const mutations = {
  SET_TREE_DATA (state, newdata) {
    state.treedata = null
    state.treedata = newdata
  }
}

const actions = {
  setTreedata ({ commit }, newdata) {
    // do something async
    commit('SET_TREE_DATA', newdata)
  }
}

export default {
  state,
  mutations,
  actions
}
