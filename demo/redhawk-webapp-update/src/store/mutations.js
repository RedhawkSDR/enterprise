import axios from 'axios'

//Add a domain configuation to the state.
export const addDomainConfig = (state, domainConfig) => {
  state.domainConfigs.push(domainConfig)
}

export const deleteDomainConfig = (state, index) => {
  var configToDelete = state.domainConfigs[index]
  if(configToDelete==state.configToView){
    console.log('Deleting configuration')
    state.showDomain = false
  }
  state.domainConfigs.splice(index, 1)
}

export const editDomainConfig = (state, index) => {
  state.domainToEdit = index
  var config = state.domainConfigs[index]
  state.configToEdit = config
  state.showEditDomainConfig = true
}

export const closeEditDomainConfig = state => {
  state.showEditDomainConfig = false
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
    myState.showDomain = true
  })
}

export const showWaveformComponents = (state, index) => {
  state.applicationName = state.launchedWaveforms[index].name

  //If the application name changes ports show no longer show up
  if(state.componentPorts!=null){
    state.componentPorts = []
  }

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
  var applicationName = state.launchedWaveforms[index].name

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
  var myState = state
  axios.delete(state.baseURI+'/applications/'+name)
  .then(function(response){
    console.log(response)
    //Update State of REST of Application
    updateLaunchedWaveforms(myState)

    //If Components displayed are from the component released reset component and ports
    if(state.applicationName==name){
      console.log('Should reset component and ports')
      state.componentPorts = []
      state.waveformComponents = []
    }else{
      console.log("Don't delete component/ports state. ")
    }
  })
  .catch(function(error){
    console.log(error)
  })
}

export const showLaunchWaveformModal = (state, waveform) => {
  state.waveformToLaunch = waveform
  state.showLaunchWaveformModal = true
}

export const closeLaunchWaveformModal = state => {
  state.showLaunchWaveformModal = false
}

function updateLaunchedWaveforms(state){
  console.log("Updating launched waveforms")
  //Retrieve all waveforms. Note may be cleaner to just delete waveform from array
  var myState = state

  axios.get(state.baseURI+'/applications.json')
  .then(function(response){
    var launchedWFJson = response.data.applications
    console.log("Updated launchedWaveforms")
    myState.launchedWaveforms = launchedWFJson
  })
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
  var myState = state
  myPut.put(launchWaveformURL+"/"+waveformToLaunch.name, JSON.stringify(waveformToLaunch))
  .then(function(response){
    console.log(response)
    updateLaunchedWaveforms(myState)
  })
  .catch(function(error){
    console.log(error)
  })
}

export const plotPortData = (state, port) => {
  console.log('Plot port data '+port)
  var wsURL = 'ws://localhost:8181/redhawk/'+state.configToView.nameServer+'/domains/'+state.configToView.domainName
  +'/applications/'+state.applicationName+'/components/'+state.portsComponentName+'/ports/'+port.name
  console.log(wsURL)
  //Update wsURL
  state.wsURL = wsURL
}
