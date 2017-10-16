import Vue from 'vue'
import Vuex from 'vuex'
import * as getters from './getters'
import * as actions from './actions'
import * as mutations from './mutations'

Vue.use(Vuex)

const state = {
  baseURL: 'http://localhost:8181/rest/redhawk',
  nameServer: 'localhost:2809',
  domainName: 'REDHAWK_DEV',
  configuration: {
    show: false
  },
  available : {},
  application: {
    metrics: [],
    name: '',
    url: '',
    index: []
  },
  gpp: {
    metrics: {},
    name: '',
    url: ''
  },
  port: {
    statistics: {},
    name: '',
    component : '',
    application : '',
    url : ''
  },
  interval: null
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
