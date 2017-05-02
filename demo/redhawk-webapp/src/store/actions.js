//Axios is asnyc so all axios calls should really occur in this class
import axios from 'axios'

//Helper functions
function getUsedTuners(getters, deviceLabel){
  var deviceUsedTuners = getters.baseURI+'/devicemanagers/'+getters.deviceManager.label+'/devices/'+deviceLabel+'/tuners/USED'

  return axios.get(deviceUsedTuners)
}

function getUnusedTuners(getters, deviceLabel){
  var deviceUnusedTuners = getters.baseURI+'/devicemanagers/'+getters.deviceManager.label+'/devices/'+deviceLabel+'/tuners/UNUSED'

  return axios.get(deviceUnusedTuners)
}

/*
* Gets the response object for a devicemanager
*/
function getDeviceManager(getters, show){
  var deviceManagerName = getters.devicemanagers[show.index].label
  var deviceManagerURL = getters.baseURI+'/devicemanagers/'+deviceManagerName+'.json'

  return axios.get(deviceManagerURL)
}

function getAllocationJson(allocate){
  var actualAllocation = new Object()
  actualAllocation["FRONTEND::tuner_allocation::allocation_id"] = allocate.id
  actualAllocation["FRONTEND::tuner_allocation::tuner_type"] = allocate.tunerType
  actualAllocation["FRONTEND::tuner_allocation::center_frequency"] = allocate.centerFrequency * 1000000
  actualAllocation["FRONTEND::tuner_allocation::sample_rate"] = allocate.samplerate * 1000000
  actualAllocation["FRONTEND::tuner_allocation::bandwidth_tolerance"] = allocate.bandwidthTolerance
  actualAllocation["FRONTEND::tuner_allocation::sample_rate_tolerance"] = allocate.sampleRateTolerance

  return actualAllocation
}

function getWaveforms(waveformsURL){
  return axios.get(waveformsURL)
}

function getApplications(applicationsURL){
  return axios.get(applicationsURL)
}

function getDeviceManagers(deviceManagersURL){
  return axios.get(deviceManagersURL)
}
//End of Helper functions

//Actions for editting domain configuration info.
export const addDomainConfig = ({ commit }, domainConfig) => commit('addDomainConfig', domainConfig)
export const deleteDomainConfig = ( { commit }, index) => commit('deleteDomainConfig', index)
export const editDomainConfig = ({ commit }, index) => commit('editDomainConfig', index)
export const editDomainConfigName = ({ commit }, name) => commit('editDomainConfigName', name)
export const editDomainConfigNameServer = ({ commit }, nameServer) => commit('editDomainConfigNameServer', nameServer)
export const editDomainConfigDomainName = ({ commit }, domainName) => commit('editDomainConfigDomainName', domainName)
export const updateDomainConfig = ({ commit }) => commit('updateDomainConfig')

//Actions to be able to view a domain
export const viewDomainConfig = ({ commit, getters }, index) => {
  var config = getters.domainConfigs[index]
  var baseURI = getters.redhawkRESTRoot+config.nameServer+'/domains/'+config.domainName

  axios.all([getWaveforms(baseURI+'/waveforms.json'), getApplications(baseURI+'/applications.json'),
  getDeviceManagers(baseURI+'/devicemanagers.json')])
  .then(axios.spread(function(waveforms, applications, deviceManagers){
    console.log(deviceManagers)
    console.log(waveforms)
    console.log(applications)

    var domain = new Object()
    domain.config = config
    domain.baseURI = baseURI
    domain.waveforms = waveforms.data.domains //TODO: Umm that's a bad key
    domain.applications = applications.data.applications
    domain.deviceManagers = deviceManagers.data.deviceManagers

    commit('viewDomainConfig', domain)
  }))

  //TODO: Add in appropriate alert on error
}

export const showWaveformComponents = ({ commit, getters }, index) => {
  var application = getters.launchedWaveforms[index]
  var applicationName = application.name
  var applicationURL = getters.baseURI + '/applications/'+applicationName+'/components.json'

  axios.get(applicationURL)
  .then(function(response){
    var applicationInfo = new Object()

    applicationInfo.application = application
    applicationInfo.applicationName = applicationName
    applicationInfo.applicationComponents = response.data.components

    commit('showWaveformComponents', applicationInfo)
  })
}

export const showComponentPorts = ({ commit, getters }, index) => {
  var portsComponentName = getters.waveformComponents[index].name
  var componentPortsURL = getters.baseURI+'/applications/'+getters.applicationName+'/components/'+portsComponentName+'/ports.json'

  axios.get(componentPortsURL)
  .then(function(response){
    var componentPorts = new Object()
    componentPorts.name = portsComponentName
    componentPorts.ports = response.data.ports

    commit('showComponentPorts', componentPorts)
  })
}

export const showComponentProperties = ({ commit }, index) => {
  commit('showComponentProperties', index)
}

export const updateComponentProperty = ({ commit }, property) => commit('updateComponentProperty', property)

//Waveform Controller props
export const showWaveformController = ({ commit }, index) => commit('showWaveformController', index)
export const closeWaveformController = ({ commit }) => commit('closeWaveformController')
export const controlWaveform = ({ commit }, control) => commit('controlWaveform', control)
export const releaseWaveform = ({ commit }, name) => commit('releaseWaveform', name)
export const updateDomainStateAfterWaveformRelease = ({ commit }, name) => commit('updateDomainStateAfterWaveformRelease', name)

//Launch Controls
export const showLaunchWaveformModal = ({ commit }, waveform) => commit('showLaunchWaveformModal', waveform)
export const closeLaunchWaveformModal = ({ commit }) => commit('closeLaunchWaveformModal')
export const launchWaveform = ({ commit }, waveformToLaunch) => commit('launchWaveform', waveformToLaunch)
//export const updateLaunchedWaveforms = ({ commit }) => commit('updateLaunchedWaveforms')

export const plotPortData = ({ commit }, port) => commit('plotPortData', port)
export const closeEditDomainConfig = ({ commit }) => commit('closeEditDomainConfig')
export const closeEditPropsConfig = ({ commit }) => commit('closeEditPropsConfig')

//Reset initial state
export const resetDomain = ({ commit }) => commit('resetDomain')
export const resetWaveformDisplay = ({ commit }) => commit('resetWaveformDisplay')

export const showApplication = ({ commit }, show) => commit('showApplication', show)

export const showDeviceManager = ({ commit, getters }, show) => {
  //TODO: Shouldn't need to do this learn to deal with Promises or just do stufff in then
  if(show.show){
    axios.all([getDeviceManager(getters, show)])
    .then(axios.spread(function(deviceManager){
      console.log(deviceManager)
      show.deviceManager = deviceManager.data
      commit('showDeviceManager', show)
    }))
  }else{
    commit('showDeviceManager', show)
  }
}

export const showDevicePorts = ({ commit }, show) => commit('showDevicePorts', show)

export const showDeviceTuners = ({ commit, getters }, show) => {
  if(show.show){
    var deviceLabel = show.device.label
    axios.all([getUsedTuners(getters, deviceLabel), getUnusedTuners(getters, deviceLabel)])
    .then(axios.spread(function(usedTuners, unusedTuners){
      var tuners = new Object()
      tuners.usedTuners = usedTuners.data
      tuners.unusedTuners = unusedTuners.data
      show.tuners = tuners

      commit('showDeviceTuners', show)
    }))
  }else{
    commit('showDeviceTuners', show)
  }
}

export const showDeviceProperties = ({ commit }, show) => commit('showDeviceProperties', show)

export const deallocate = ({ commit, getters }, deallocate) => {
  console.log("Made it")
  var deallocateURL = getters.baseURI+'/devicemanagers/'+getters.deviceManager.label+'/devices/'+deallocate.deviceLabel+'/deallocate'
  console.log(deallocateURL)
  console.log(deallocate)
  var deviceLabel = getters.tuners.device.label
  axios.post(deallocateURL, deallocate.allocationId, {
    headers: {
      'Content-Type':'application/json'
    }
  })
  .then(function(response){
    axios.all([getUsedTuners(getters, deviceLabel), getUnusedTuners(getters, deviceLabel)])
    .then(axios.spread(function(usedTuners, unusedTuners){
      var tuners = new Object()
      tuners.device = getters.tuners.device
      tuners.usedTuners = usedTuners.data
      tuners.unusedTuners = unusedTuners.data

      commit('updateTunersData', tuners)
    }))
  })
  .catch(function(error){
    console.log(error)
  })
}

export const showAllocationModal = ({ commit }, show) => commit('showAllocationModal', show)

export const allocate = ({commit, getters}, allocate) => {
  //Get allocation JSON for post
  var allocationJSON = getAllocationJson(allocate)

  var allocateURL = getters.baseURI+'/devicemanagers/'+getters.deviceManager.label+'/devices/'+getters.tuners.device.label+'/allocate'

  var deviceLabel = getters.tuners.device.label

  axios.post(allocateURL, allocationJSON, {
    headers: {
      'Content-Type':'application/json'
    }
  })
  .then(function(response){
    axios.all([getUsedTuners(getters, deviceLabel), getUnusedTuners(getters, deviceLabel)])
    .then(axios.spread(function(usedTuners, unusedTuners){
      var tuners = new Object()
      tuners.device = getters.tuners.device
      tuners.usedTuners = usedTuners.data
      tuners.unusedTuners = unusedTuners.data

      commit('updateTunersData', tuners)
    }))
  })
  .catch(function(error){
    console.log(error)
  })
}

export const updateRedhawkRESTRoot = ({ commit }, updateURL ) => commit('updateRedhawkRESTRoot', updateURL)
