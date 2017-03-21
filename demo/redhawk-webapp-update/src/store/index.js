import Vue from 'vue'
import Vuex from 'vuex'
import * as getters from './getters'
import * as actions from './actions'
import * as mutations from './mutations'

Vue.use(Vuex)

const state = {
  domainConfigs : [], // List of domain configs
  launchedWaveforms: [], // List of launched waveforms
  domainToEdit: null, //Domain being edited
  configToEdit: {}, //Config being editted
  configToView: {},
  baseURI: null,
  availableWaveforms: [],
  waveformComponents: [],
  componentPorts: [],
  applicationName: null,
  portsComponentName: null,
  componentPropertiesToEdit: [],
  propComponentName: null,
  showWaveformController: false,
  waveformToControl: null,
  showLaunchWaveformModal : false,
  waveformToLaunch: null
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
