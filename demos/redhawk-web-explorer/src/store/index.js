import Vue from 'vue'
import Vuex from 'vuex'
import * as getters from './getters'
import * as actions from './actions'
import * as mutations from './mutations'

Vue.use(Vuex)

const state = {
  configuration: {
    baseURL: 'http://127.0.0.1:8181/rest/redhawk/',
    wsBaseURL: 'ws://127.0.0.1:8181/ws/redhawk/',
  },
  dialog : {
    show: false,
    title: null,
    message : null
  },
  domain: {
    domainName: 'REDHAWK_DEV',
    nameServer: '127.0.0.1:2809',
    properties : []
  },
  waveforms: {
    catalog: [],
    waveformToLaunch: {}
  },
  devicemanagers: [],
  devicemanager: {
    label : null,
    identifier: '',
    services: [],
    devices: [],
    properties: []
  },
  device: {
    identifier: null,
    label: null,
    started: false,
    adminState: null,
    usageState: null,
    operationState : null,
    implementation: '',
    properties: [],
    ports: [],
  },
  deviceTuners: {
    used: [],
    unused: []
  },
  applications: [],
  application: {
    identifier: null,
    name: null,
    started: false,
    aware: false,
    components: [],
    externalPorts: [],
    properties: []
  },
  component: {
    started: false,
    name: '',
    processId: '',
    deviceIdentifier: '',
    implementation: '',
    properties: [],
    ports: [],
    configuration: null,
    softwareComponent: {}
  },
  port: {
    name: '',
    type: '',
    repId: '',
    connectionIds: [],
    wsURL : ''
  }
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
