//Axios is asnyc so all axios calls should really occur in this class
import axios from 'axios'
//var AUTH_TOKEN = "Basic "+btoa("redhawk:redhawk")
//axios.defaults.headers.common['Authorization'] = AUTH_TOKEN;
//console.log("Auth token "+AUTH_TOKEN)
//var config = {
//  headers : {'Authorization' : AUTH_TOKEN}
//}

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

function getEventChannels(eventChannelsURL){
  return axios.get(eventChannelsURL)
}

function getDomainBaseURL(getters){
  return getters.baseURL+'/'+getters.nameServer+'/domains/'+getters.domainName
}
//End of Helper functions

//Begining of latest actions
export const getAvailableWaveforms = ({commit, getters}) => {
  var waveformsURL = getDomainBaseURL(getters)+'/waveforms.json'
  axios.get(waveformsURL)
  .then(function(response){
    commit('setAvailableWaveforms', response.data.domains)
  })
  .catch(function(error){
    //TODO: Actually visually handle this
    console.log(error)
  })
}

//Launch Waveform
export const launchChoosenWaveform = ({ commit, getters }, waveformToLaunch) => {
  var launchWaveformURL = getDomainBaseURL(getters)+'/applications/'+waveformToLaunch.name

  axios.put(launchWaveformURL, JSON.stringify(waveformToLaunch),
  {
    headers: { 'Content-Type' : 'application/json'}
  })
  .then(function(response){
    console.log("Some indication that launch worked so you can redirect")
  })
  .catch(function(error){
    console.log("Error")
  })
}

export const getApplicationsInDomain = ({commit, getters}) => {
  var applicationsURL = getDomainBaseURL(getters)+'/applications.json'

  axios.get(applicationsURL)
  .then(function(response){
    commit('setApplications', response.data.applications)
  })
  .catch(function(error){
    console.log(error)
  })
}

//End of latest actions

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

  console.log(axios.defaults)
  axios.all([getWaveforms(baseURI+'/waveforms.json'), getApplications(baseURI+'/applications.json'),
  getDeviceManagers(baseURI+'/devicemanagers.json'), getEventChannels(baseURI+'/eventchannels.json')])
  .then(axios.spread(function(waveforms, applications, deviceManagers, eventchannels){
    var domain = new Object()
    domain.config = config
    domain.baseURI = baseURI
    domain.waveforms = waveforms.data.domains //TODO: Umm that's a bad key
    domain.applications = applications.data.applications
    domain.deviceManagers = deviceManagers.data.deviceManagers
    domain.eventchannels = eventchannels.data.eventChannels

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

export const showComponentProperties = ({ commit, getters }, index) => {
  var propComponentName = getters.waveformComponents[index].name
  var componentPropsURL = getters.baseURI+'/applications/'+getters.applicationName+'/components/'+propComponentName+'/properties.json'

  axios.get(componentPropsURL)
  .then(function(response){
    var obj = new Object()
    obj.propComponentName = propComponentName
    obj.componentPropertiesToEdit = response.data.properties

    commit('showComponentProperties', obj)
  })
  .catch(function(error){

  })
}

export const updateComponentProperty = ({ commit, getters }, property) => {
  var propertyUpdateURL = getters.baseURI+'/applications/'+getters.applicationName+'/components/'+getters.propComponentName+'/properties/'+property.id

  axios.put(propertyUpdateURL, JSON.stringify(property),
  {
    headers: {
      'Content-Type': 'application/json'
    }
  })
  .then(function(response){
    commit('updateComponentProperty', property)
  })
  .catch(function(error){

  })
}

//Waveform Controller props
export const showWaveformController = ({ commit, getters }, index) => {
  var applicationsURL = getters.baseURI+'/applications/'+getters.launchedWaveforms[index].name+'.json'

  axios.get(applicationsURL)
  .then(function(response){
      var obj = new Object()
      obj.waveformToControl = response.data
      commit('showWaveformController', obj)
  })
  .catch(function(error){

  })
}

export const closeWaveformController = ({ commit }) => commit('closeWaveformController')

export const controlWaveform = ({ commit, getters }, control) => {
  var applicationControlURI = getters.baseURI+'/applications/'+control.waveformName
  axios.post(applicationControlURI, control.action,{
    headers: {
      'Content-Type':'application/json'
    }
  })
  .then(function(response){
      //TODO: If you ever want to just do a play button this i where u could make updates
  })
  .catch(function(response){

  })
}

export const releaseWaveform = ({ commit, getters }, name) => {
  axios.delete(getters.baseURI+'/applications/'+name)
  .then(function(response){
    //Get new application list post release
    axios.get(getters.baseURI+'/applications.json')
    .then(function(response){
        var obj = new Object()
        obj.releasedAppName = name
        obj.applications = response.data.applications
        commit('releaseWaveform', obj)
    })
    .catch(function(error){

    })
  })
  .catch(function(error){

  })
}

export const releaseRegistrant = ({ commit, getters }, registrantId) => {
  axios.delete(getters.baseURI+'/eventchannels/'+getters.eventchannel.name+'/registrant/'+registrantId)
  .then(function(response){
    //Get latest registrants
    axios.get(getters.baseURI+'/eventchannels/'+getters.eventchannel.name)
    .then(function(response){
        commit('updateEventChannelRegistrants', response.data)
    })
  })
  .catch(function(error){
    console.log(error)
  })
}

export const updateEventChannelRegistrants = ({ commit, getters }, name) => {
  axios.get(getters.baseURI+'/eventchannels/'+name+".json")
  .then(function(response){
      commit('updateEventChannelRegistrants', response.data)
  })
}

export const deleteEventChannel = ({commit, getters}, name) => {
  axios.delete(getters.baseURI+'/eventchannels/'+name)
  .then(function(response){
    //Update Event Channels list and view
    getEventChannels(getters.baseURI+'/eventchannels.json')
    .then(function(response){
      var obj = new Object();
      obj.eventchannels = response.data.eventChannels
      obj.deletedName = name

      commit('updateEventChannels', obj)
    })
    .catch(function(error){
      console.log(error)
    })
  })

}

export const updateDomainStateAfterWaveformRelease = ({ commit }, name) => commit('updateDomainStateAfterWaveformRelease', name)

//Launch Controls
export const showLaunchWaveformModal = ({ commit }, waveform) => commit('showLaunchWaveformModal', waveform)
export const closeLaunchWaveformModal = ({ commit }) => commit('closeLaunchWaveformModal')

//Launch Waveform
export const launchWaveform = ({ commit, getters }, waveformToLaunch) => {
  var launchWaveformURL = getters.baseURI+'/applications/'+waveformToLaunch.name

  axios.put(launchWaveformURL, JSON.stringify(waveformToLaunch),
  {
    headers: { 'Content-Type' : 'application/json'}
  })
  .then(function(response){
    //Get new application list post release
    axios.get(getters.baseURI+'/applications.json')
    .then(function(response){
        var obj = new Object()
        //obj.releasedAppName = name
        obj.applications = response.data.applications
        commit('launchWaveform', obj)
    })
    .catch(function(error){

    })
  })
}

//Show Event Channel Controls
export const showEventChannelModal = ({ commit }, show) => commit('showEventChannelModal', show)

//Create Event Channel
export const createEventChannel = ({ commit, getters }, channelName) => {
    var createEventChannelURL = getters.baseURI+'/eventchannels/'+channelName

    axios.put(createEventChannelURL, channelName,
    {
      headers: { 'Content-Type' : 'application/json'}
    })
    .then(function(response){
      //At this point you need to update the eventchannels
      getEventChannels(getters.baseURI+'/eventchannels.json')
      .then(function(response){
        var obj = new Object();
        obj.eventchannels = response.data.eventChannels
        obj.deletedName = name

        commit('updateEventChannels', obj)
      })
      .catch(function(error){
        console.log(error)
      })
  })
  .catch(function(error){
    console.log(error)
  })
}

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
      show.deviceManager = deviceManager.data
      commit('showDeviceManager', show)
    }))
  }else{
    commit('showDeviceManager', show)
  }
}

export const showDevicePorts = ({ commit, getters }, show) => {
  if(show.show){
    var devicePortsURL = getters.baseURI+'/devicemanagers/'+getters.deviceManager.label+'/devices/'+show.device.label+'/ports.json'
    axios.get(devicePortsURL)
    .then(function(response){
      var devPorts = new Object()

      devPorts.ports = response.data.ports
      devPorts.device = show.device

      commit('showDevicePorts', devPorts)
    })
    .catch(function(error){
      console.log("ERROR: "+error)
    })
  }else{
    //commit('showDeviceProperties', show)
  }
}

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

export const showEventChannel = ({ commit }, show) => commit('showEventChannel', show)

export const subscribeToEventChannel = ({ commit }, sub) => commit('subscribeToEventChannel', sub)

export const showDeviceProperties = ({ commit, getters }, show) => commit('showDeviceProperties', show)

export const deallocate = ({ commit, getters }, deallocate) => {
  var deallocateURL = getters.baseURI+'/devicemanagers/'+getters.deviceManager.label+'/devices/'+deallocate.deviceLabel+'/deallocate'
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
