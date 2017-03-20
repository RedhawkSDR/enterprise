import Vue from 'vue'
import Vuex from 'vuex'
import * as getters from './getters'
import * as actions from './actions'
import * as mutations from './mutations'

Vue.use(Vuex)

const state = {
  domainConfigs : [],
  launchedWaveforms: [],
  domainToEdit: null,
  configToEdit: {},
  configToView: {},
  baseURI: null,
  waveforms: [],
  waveformComponents: [],
  componentPorts: [],
  applicationName: null,
  portsComponentName: null,
  componentPropertiesToEdit: []
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
