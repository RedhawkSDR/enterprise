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

function getDomainBaseURL(getters){
  return getters.baseURL+'/'+getters.nameServer+'/domains/'+getters.domainName
}
//End of Helper functions
export const setDomainName = ({commit}, value) =>  commit('setDomainName', value)
export const setNameServer = ({commit}, value) =>  commit('setNameServer', value)
export const selectWaveform = ({commit}, value) => commit('selectWaveform', value)

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
  var applicationsURL = getDomainBaseURL(getters)+'/applications.json?fetch=LAZY'

  axios.get(applicationsURL)
  .then(function(response){
    commit('setApplications', response.data.applications)
  })
  .catch(function(error){
    console.log(error)
  })
}

export const controlApplication = ({commit, getters, store}, controlInfo) => {
  var applicationControlURL = getDomainBaseURL(getters)+'/applications/'+controlInfo.applicationName

  console.log("Application URL "+applicationControlURL)
  axios.post(applicationControlURL, controlInfo.action,{
    headers: {
      'Content-Type':'application/json'
    }
  })
  .then(function(response){
    //Update applications in domain
    //TODO: Should be able to just call store.dispatch
    //store.dispatch(types.UPDATE_SHOW, 'getApplicationsInDomain')
    var applicationsURL = getDomainBaseURL(getters)+'/applications.json?fetch=LAZY'

    axios.get(applicationsURL)
    .then(function(response){
      commit('setApplications', response.data.applications)
    })
    .catch(function(error){
      console.log(error)
    })
  })
  .catch(function(error){

  })
}

export const releaseApplication = ({getters, commit}, appName) => {
  var releaseApplicationURL = getDomainBaseURL(getters)+'/applications/'+appName
  axios.delete(releaseApplicationURL)
  .then(function(response){
    //TODO: Should be able to just call store.dispatch
    //store.dispatch(types.UPDATE_SHOW, 'getApplicationsInDomain')
    var applicationsURL = getDomainBaseURL(getters)+'/applications.json?fetch=LAZY'

    axios.get(applicationsURL)
    .then(function(response){
      commit('setApplications', response.data.applications)
    })
    .catch(function(error){
      console.log(error)
    })
  })
  .catch(function(error){

  })
}

export const selectApplication = ({getters, commit}, applicationName) => {
  var applicationURL = getDomainBaseURL(getters)+'/applications/'+applicationName

  axios.get(applicationURL)
  .then(function(response){
    commit('selectApplication', response.data)
  })
  .catch(function(error){
    //TODO: Handle error
    console.log(error)
  })
}

export const selectComponent = ({getters, commit}, component) => {
  var componentURL = getDomainBaseURL(getters)+'/applications/'+component.applicationName+
    '/components/'+component.name+'.json'

  axios.get(componentURL)
  .then(function(response){
    console.log("Successfully got component")
    console.log(response.data)
    commit('selectComponent', response.data)
  })
  .catch(function(error){
    console.log("ERROR "+error);
  })
}

export const selectDomainProperties = ({getters, commit}, domainName) => {
  var domainPropsURL = getDomainBaseURL(getters)+'/properties.json'
  axios.get(domainPropsURL)
  .then(function(response){
    commit('selectDomainProperties', response.data.properties)
  })
  .catch(function(error){
    console.log("ERROR "+error)
  })
}

export const getDeviceManagersInDomain = ({commit, getters}) => {
  var devicemanagersURL = getDomainBaseURL(getters)+'/devicemanagers.json?fetch=LAZY'
  console.log("Device Manager URL: "+devicemanagersURL)
  axios.get(devicemanagersURL)
  .then(function(response){
    console.log(response.data)
    commit('setDeviceManagers', response.data.deviceManagers)
  })
  .catch(function(error){
    console.log(error)
  })
}

export const selectDeviceManager = ({commit, getters}, deviceManagerName) => {
  console.log("DeviceManager "+deviceManagerName)
  var deviceManagerURL = getDomainBaseURL(getters)+'/devicemanagers/'+deviceManagerName+'.json'

  axios.get(deviceManagerURL)
  .then(function(response){
    commit('selectDeviceManager', response.data)
  })
  .catch(function(error){
    console.log(error)
  })
}

export const selectDevice = ({getters, commit}, device) => {
  console.log(device)
  var componentURL = getDomainBaseURL(getters)+'/devicemanagers/'+device.devicemanagerLabel+
    '/devices/'+device.label+'.json'

  axios.get(componentURL)
  .then(function(response){
    commit('selectDevice', response.data)
  })
  .catch(function(error){
    console.log("ERROR "+error);
  })
}

export const selectPort = ({getters, commit}, port) => {
  var portURL;
  if(port.type=="Component"){
    portURL = getDomainBaseURL(getters)+'/applications/'+port.applicationName+
      '/components/'+port.componentName+'/ports/'+port.name+'.json'
  }else{
    portURL = getDomainBaseURL(getters)+'/devicemanagers/'+port.devicemanagerLabel+
      '/devices/'+port.deviceLabel+'/ports/'+port.name+'.json'
  }

  axios.get(portURL)
  .then(function(response){
    console.log("Queried!!!!")
    commit('selectPort', response.data)
  })
  .catch(function(error){
    console.log("ERROR "+error);
  })
}

export const setPortWSURL = ({commit}, value) => commit('setPortWSURL', value)
