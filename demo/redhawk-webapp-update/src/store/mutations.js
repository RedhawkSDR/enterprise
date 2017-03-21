import axios from 'axios'

//Add a domain configuation to the state.
export const addDomainConfig = (state, domainConfig) => {
  state.domainConfigs.push(domainConfig)
}

export const deleteDomainConfig = (state, index) => {
  state.domainConfigs.splice(index, 1)
}

export const editDomainConfig = (state, index) => {
  state.domainToEdit = index
  var config = state.domainConfigs[index]
  state.configToEdit = config
}

export const editDomainConfigName = (state, name) => {
  state.configToEdit.name = name
}

export const editDomainConfigNameServer = (state, nameServer) => {
  state.configToEdit.nameServer = nameServer
}

export const editDomainConfigDomainName = (state, domainName) => {
  state.configToEdit.domainName = domainName
}

export const updateDomainConfig = state => {
  //Edit the config to edit that's marked
  state.domainConfigs[state.domainToEdit] = state.configToEdit
}

export const viewDomainConfig = (state, index) => {
  var config = state.domainConfigs[index]
  state.configToView = config
  state.baseURI = "http://127.0.0.1:8181/cxf/redhawk/"+config.nameServer+"/domains/"+config.domainName

  //Setting Waveforms based on config
  var myState = state

  axios.get(state.baseURI+'/applications.json')
  .then(function(response){
    var launchedWFJson = response.data.applications
    console.log('Waveforms Launched: '+launchedWFJson)
    myState.launchedWaveforms = launchedWFJson
  })
}

//TODO: Merge this with viewDomain config and execute in parralel
export const getWaveformsAvailable = (state, index) => {
  //Setting Waveforms based on config
  var myState = state

  axios.get(state.baseURI+'/waveforms.json')
  .then(function(response){
    var availableWaveforms = response.data.domains
    myState.availableWaveforms = availableWaveforms
  })
}

export const showWaveformComponents = (state, index) => {
  state.applicationName = state.waveforms[index].name

  var myState = state
  axios.get(state.baseURI+'/applications/'+state.applicationName+'/components.json')
  .then(function(response){
      myState.waveformComponents = response.data.components
  })
  .catch(function(error){
    console.log("ERROR: "+error)
  })
}

export const showComponentPorts = (state, index) => {
  console.log("Makeing it to show component ports")
  state.portsComponentName = state.waveformComponents[index].name
  console.log('Component Name: '+state.componentName)
  var myState = state
  axios.get(state.baseURI+'/applications/'+state.applicationName+'/components/'+state.portsComponentName+'/ports.json')
  .then(function(response){
    myState.componentPorts = response.data.ports
  })
  .catch(function(error){
    console.log("ERROR "+error)
  })
}

export const showComponentProperties = (state, index) => {
  console.log('Time to show some props')
  state.propComponentName = state.waveformComponents[index].name

  var myState = state
  axios.get(state.baseURI+'/applications/'+state.applicationName+'/components/'+state.propComponentName+'/properties.json')
  .then(function(response){
    myState.componentPropertiesToEdit = response.data.properties
  })
  .catch(function(error){
    console.log("ERROR "+error)
  })
}

export const updateComponentProperty = (state, property) => {
  console.log('Update property'+ property)
  var myState = state

  /*
  * Run the put with the updated property
  */
  var myPut = axios.create({headers: {
    'Content-Type': 'application/json'
    }
  })
  var url = state.baseURI+'/applications/'+state.applicationName+'/components/'+state.propComponentName+'/properties/'+property.id
  console.log("URL: "+url)
  myPut.put(url, JSON.stringify(property))
  .then(function(response){
    console.log(response)
  })
  .catch(function(error){
    console.log(error)
  })
}

export const showWaveformController = (state, index) => {
  console.log('Show waveform controller')
  var applicationName = state.waveforms[index].name

  //Retrieve current waveform state
  var myState = state
  axios.get(state.baseURI+'/applications/'+applicationName+'.json')
  .then(function(response){
      myState.waveformToControl = response.data
      myState.showWaveformController = true
  })
  .catch(function(error){
    console.log("ERROR: "+error)
  })
}

export const closeWaveformController = state => {
  state.showWaveformController = false
}

export const controlWaveform = (state, control) => {
  console.log("Control obj: "+control)
  axios.post(state.baseURI+'/applications/'+control.waveformName, control.action,{
          headers:{
                  'Content-Type':'application/json'
          }
  })
  .then(function(response){
          console.log(response)
  })
  .catch(function(error){
          console.log(error)
  })
}

export const releaseWaveform = (state, name) => {
  axios.delete(state.baseURI+'/applications/'+name)
  .then(function(response){
    console.log(response)
  })
  .catch(function(error){
    console.log(error)
  })
}

export const updateDomainStateAfterWaveformRelease = (state, name) => {
  //Retrieve all waveforms. Note may be cleaner to just delete waveform from array
  var myState = state

  axios.get(state.baseURI+'/applications.json')
  .then(function(response){
    var launchedWFJson = response.data.applications
    myState.waveforms = launchedWFJson

    //Remove
  })
}

export const showLaunchWaveformModal = (state, waveform) => {
  state.waveformToLaunch = waveform
  state.showLaunchWaveformModal = true
}

export const closeLaunchWaveformModal = state => {
  state.showLaunchWaveformModal = false
}

export const launchWaveform = (state, waveformToLaunch) => {
  console.log("Waveform To Launch "+JSON.stringify(waveformToLaunch))
  var launchWaveformURL = state.baseURI+"/applications"
  var myPut = axios.create({
    headers: {
      'Content-Type': 'application/json',
      'mimeType':'text/html'
    }
  })
  myPut.put(launchWaveformURL+"/"+waveformToLaunch.name, JSON.stringify(waveformToLaunch))
  .then(function(response){
    console.log(response)
  })
  .catch(function(error){
    console.log(error)
  })
}
