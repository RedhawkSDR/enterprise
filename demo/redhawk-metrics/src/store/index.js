import Vue from 'vue'
import Vuex from 'vuex'
import * as getters from './getters'
import * as actions from './actions'
import * as mutations from './mutations'

Vue.use(Vuex)

const state = {
  baseURL: 'http://localhost:8181/cxf/redhawk',
  nameServer: 'localhost:2809',
  domainName: 'REDHAWK_DEV',
  available : {},
  appMetrics: {},
  appMetricsURL: '',
  appMetricsKeys: [],
  appMetricsToView: {},
  appMetricsType: ''
}

const store = new Vuex.Store({
	state,
	getters,
	actions,
	mutations
})

if (module.hot) {
  module.hot.accept([
    './getters',
    './actions',
    './mutations'
  ], () => {
    store.hotUpdate({
      getters: require('./getters'),
      actions: require('./actions'),
      mutations: require('./mutations')
    })
  })
}

export default store
