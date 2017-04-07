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
  configToView: {}, //Which config to views domain
  baseURI: null, //Base URI for endpoint
  availableWaveforms: [], //Availables waveforms for a Domain
  waveformComponents: [], //Components for a selected waveform
  componentPorts: [], //Compoent Ports
  applicationName: null, //Name of application you're viewing
  application: null,
  portsComponentName: null,
  portToDisplayName: null,
  componentPropertiesToEdit: [],
  propComponentName: null,
  showWaveformController: false,
  waveformToControl: null,
  showLaunchWaveformModal : false,
  waveformToLaunch: null,
  showDomain: false,
  wsURL: null,
  showEditDomainConfig: false,
  showWaveformComponents: false,
  showComponentProperties: false,
  propertyUpdate: 0,
  redhawkRESTRoot: 'http://127.0.0.1:8181/cxf/redhawk/',
  showEditRESTModal: false,
  showApplication: false,
  showDeviceManager: false,
  devicemanagers: [],
  deviceManager: {}, //Device Manager to show
  devicePorts : {},
  tuners : {},
  showTuners: false
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
