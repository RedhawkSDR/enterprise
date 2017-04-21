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
  state.baseURI = state.redhawkRESTRoot+config.nameServer+"/domains/"+config.domainName

  //Setting Waveforms based on config
  var myState = state

  axios.get(state.baseURI+'/waveforms.json')
  .then(function(response){
    var availableWaveforms = response.data.domains
    myState.availableWaveforms = availableWaveforms
  })
  .catch(function(error){
    console.log("Unable to get available waveforms")
    alert("Unable to get waveforms available")
  })

  axios.get(state.baseURI+'/applications.json')
  .then(function(response){
    var launchedWFJson = response.data.applications
    console.log('Waveforms Launched: '+launchedWFJson)
    myState.launchedWaveforms = launchedWFJson
  })
  .catch(function(error){
    console.log("Unable to connect to domain")
    alert("Unable to connect to domain.")
  })

  //Setting the devicemanagers based on config
  axios.get(state.baseURI+'/devicemanagers.json')
  .then(function(response){
    //TODO: Would like to Domain info in this object
    console.log("Querying Devicemanagers")
    var devicemanagers = new Object();
    myState.devicemanagers = response.data.deviceManagers
  })
  .catch(function(error){
    alert("Unable to find device managers")
  })

  state.showDomain = true
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
  state.applicationName = state.launchedWaveforms[index].name

  //TODO: Refactor to not care about applicationName just have vuex track the waveform
  state.application = state.launchedWaveforms[index]

  //If the application name changes ports show no longer show up
  if(state.componentPorts!=null){
    state.componentPorts = []
  }

  var myState = state
  axios.get(state.baseURI+'/applications/'+state.applicationName+'/components.json')
  .then(function(response){
      myState.waveformComponents = response.data.components
      myState.showWaveformComponents = true
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
  state.showComponentProperties = true

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
    myState.propertyUpdate++
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
  var myState = state
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
  var url = new URL(state.baseURI)

  console.log(port)
  if(port.portType=='component'){
    var wsURL = 'ws://'+url.hostname+':8181/redhawk/'+state.configToView.nameServer+'/domains/'+state.configToView.domainName
    +'/applications/'+state.applicationName+'/components/'+state.portsComponentName+'/ports/'+port.port.name

    //Update wsURL
    state.wsURL = wsURL
    state.portToDisplayName = port.port.name
  }else{
    console.log("Display Device port data")
    var wsURL = 'ws://'+url.hostname+':8181/redhawk/'+state.configToView.nameServer+'/domains/'+state.configToView.domainName
    +'/devicemanagers/'+state.deviceManager.label+'/devices/'+port.device.label+'/ports/'+port.port.name

    state.wsURL = wsURL
    state.portToDisplayName = port.port.name
  }
}

export const closeEditPropsConfig = state =>{
  state.showComponentProperties = false
}

export const resetDomain = state => {
  state.launchedWaveforms = []
  state.availableWaveforms = []
  state.waveformComponents = []
  state.componentPorts = []
  state.applicationName = null
  state.portsComponentName = null
  state.componentPropertiesToEdit = []
  state.propComponentName = null
  state.showWaveformController = null
  state.waveformToControl = null
  state.waveformToLaunch = null
  state.showDomain = false
  state.wsURL = null
  state.showWaveformComponents = false
  state.showComponentProperties = false
  state.configToView = {}
  state.portToDisplayName = null
}

export const resetWaveformDisplay = state => {
  state.waveformComponents = []
  state.componentPorts = []
  state.portsComponentName = null
  state.portToDisplayName = null
  state.showWaveformComponents = false
  state.showComponentProperties = false
  state.wsURL = null
  state.showApplication = false
}

export const showApplication = (state, show) => {
  console.log('Show application '+show)
  state.showApplication = show
}

export const showDeviceManager = (state, show) => {
  var myState = state

  //TODO: Think about this a little bit more
  state.showTuners = false
  state.showDeviceProperties = false

  if(show.show){
    var deviceManagerName = state.devicemanagers[show.index].label
    var deviceManagerURL = state.baseURI+'/devicemanagers/'+deviceManagerName+'.json'

    axios.get(deviceManagerURL)
    .then(function(response){
        myState.deviceManager = response.data
        myState.showDeviceManager = true
    })
    .catch(function(error){
      console.log("ERROR: "+error)
    })
  }else{
      myState.showDeviceManager = false
  }
}

export const showDevicePorts = (state, show) => {
  var myState = state
  var myShow = show

  if(show.show){
    var devicePortsURL = state.baseURI+'/devicemanagers/'+state.deviceManager.label+'/devices/'+show.device.label+'/ports.json'

    axios.get(devicePortsURL)
    .then(function(response){
      console.log("Do I have access to this")
      var devPorts = new Object()
      devPorts.ports = response.data.ports
      devPorts.device = myShow.device
      myState.devicePorts = devPorts
    })
    .catch(function(error){
      console.log("ERROR: "+error)
    })
  }else{
    console.log("Do stuff")
  }
}

function getUsedTuners(state, deviceLabel){
  var myState = state
  var deviceUsedTuners = state.baseURI+'/devicemanagers/'+state.deviceManager.label+'/devices/'+deviceLabel+'/tuners/USED'

  axios.get(deviceUsedTuners)
  .then(function(response){
    console.log("Do I have access to this")
    myState.tuners.usedTuners = response.data
  })
  .catch(function(error){
    console.log("ERROR: "+error)
  })
}

function getUnusedTuners(state, deviceLabel){
  var myState = state
  var deviceUnusedTuners = state.baseURI+'/devicemanagers/'+state.deviceManager.label+'/devices/'+deviceLabel+'/tuners/UNUSED'

  axios.get(deviceUnusedTuners)
  .then(function(response){
    console.log("Do I have access to this")
    myState.tuners.unusedTuners = response.data
  })
  .catch(function(error){
    console.log("ERROR: "+error)
  })
}

function fdGetUsedTuners(state, deviceLabel){
  var deviceUsedTuners = state.baseURI+'/devicemanagers/'+state.deviceManager.label+'/devices/'+deviceLabel+'/tuners/USED'

  return axios.get(deviceUsedTuners)
}

function fdGetUnusedTuners(state, deviceLabel){
  var deviceUnusedTuners = state.baseURI+'/devicemanagers/'+state.deviceManager.label+'/devices/'+deviceLabel+'/tuners/UNUSED'

  return axios.get(deviceUnusedTuners)
}

export const showDeviceTuners = (state, show) => {
  var myState = state
  var tuners = new Object()

  if(show.show){
    myState.tuners.device = show.device

    axios.all([fdGetUnusedTuners(myState, show.device.label), fdGetUsedTuners(myState, show.device.label)])
    .then(axios.spread(function(unused, used){
      console.log("Ran both requests")
      myState.tuners.unusedTuners = unused.data
      myState.tuners.usedTuners = used.data
      myState.showTuners = true
    }));
  }else{
    myState.showTuners = false
  }
}

export const deallocate = (state, deallocate) => {
  console.log("Well now what is it: "+ deallocate)
  var deallocateURL = state.baseURI+'/devicemanagers/'+state.deviceManager.label+'/devices/'+deallocate.deviceLabel+'/deallocate'
  var myState = state
  axios.post(deallocateURL, deallocate.allocationId,{
          headers:{
                  'Content-Type':'application/json'
          }
  })
  .then(function(response){
    //After dellocation see if you can update device tuners being showTuners
    axios.all([fdGetUnusedTuners(myState, deallocate.deviceLabel), fdGetUsedTuners(myState, deallocate.deviceLabel)])
    .then(axios.spread(function(unused, used){
      console.log("Updated on deallocate...")
      console.log(unused)
      console.log(used)
      myState.tuners.unusedTuners = unused.data
      myState.tuners.usedTuners = used.data
    }));
  })
  .catch(function(error){
          console.log(error)
  })
}

export const showAllocationModal = (state, show) =>  {
  state.showAllocationModal = show
}

export const allocate = (state, allocate) => {
  var allocateURL = state.baseURI+'/devicemanagers/'+state.deviceManager.label+'/devices/'+state.tuners.device.label+'/allocate'
  var myState = state
  var deviceLabel = state.tuners.device.label

  var actualAllocation = new Object()
  actualAllocation["FRONTEND::tuner_allocation::allocation_id"] = allocate.id
  actualAllocation["FRONTEND::tuner_allocation::tuner_type"] = allocate.tunerType
  actualAllocation["FRONTEND::tuner_allocation::center_frequency"] = allocate.centerFrequency * 1000000
  actualAllocation["FRONTEND::tuner_allocation::sample_rate"] = allocate.samplerate * 1000000
  actualAllocation["FRONTEND::tuner_allocation::bandwidth_tolerance"] = allocate.bandwidthTolerance
  actualAllocation["FRONTEND::tuner_allocation::sample_rate_tolerance"] = allocate.sampleRateTolerance

  console.log(actualAllocation)
  console.log("Allocation URL: "+allocateURL)

  axios.post(allocateURL, actualAllocation,{
          headers:{
                  'Content-Type':'application/json'
          }
  })
  .then(function(response){
    //After dellocation see if you can update device tuners being showTuners
    axios.all([fdGetUnusedTuners(myState, deviceLabel), fdGetUsedTuners(myState, deviceLabel)])
    .then(axios.spread(function(unused, used){
      console.log("Updated on allocate...")
      console.log(unused)
      console.log(used)
      myState.tuners.unusedTuners = unused.data
      myState.tuners.usedTuners = used.data
    }));
  })
  .catch(function(error){
          console.log(error)
  })
}

export const updateRedhawkRESTRoot = (state, updateURL) => {
  state.redhawkRESTRoot = updateURL
}

export const showDeviceProperties = (state, show) => {
  if(show.show){
    state.deviceForPropView = show.device
    state.showDeviceProperties = show.show
  }else{
    state.showDeviceProperties = show.show
  }
}
