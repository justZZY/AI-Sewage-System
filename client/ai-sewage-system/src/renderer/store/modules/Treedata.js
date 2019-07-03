const state = {
  treedata: [{label: '暂无数据', children: [{label: '暂无数据', netclass: 'el-icon-loading'}]}]
}

const mutations = {
  SET_TREE_DATA (state, newdata) {
    state.treedata = newdata
  }
}

const actions = {
  setTreedata (context, newdata) {
    // do something async
    context.commit('SET_TREE_DATA', newdata)
  }
}

export default {
  state,
  mutations,
  actions
}
