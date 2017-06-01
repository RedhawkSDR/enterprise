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

export const viewDomainConfig = (state, domain) => {
  //Set variables for view
  state.configToView = domain.config
  state.baseURI = domain.baseURI
  state.availableWaveforms = domain.waveforms
  state.launchedWaveforms = domain.applications //TODO: Fix index put appropriate name
  state.devicemanagers = domain.deviceManagers
  state.eventchannels = domain.eventchannels

  state.showDomain = true
}

export const showWaveformComponents = (state, appInfo) => {
  state.applicationName = appInfo.applicationName

  //TODO: Refactor to not care about applicationName just have vuex track the waveform
  state.application = appInfo.application

  //If the application name changes ports show no longer show up
  if(state.componentPorts!=null){
    state.componentPorts = []
  }

  state.waveformComponents = appInfo.applicationComponents
  state.showWaveformComponents = true
}

export const showComponentPorts = (state, componentPorts) => {
  state.portsComponentName = componentPorts.name
  state.componentPorts = componentPorts.ports
}

export const showComponentProperties = (state, obj) => {
  state.propComponentName = obj.propComponentName
  state.componentPropertiesToEdit = obj.componentPropertiesToEdit
  state.showComponentProperties = true
}

export const updateComponentProperty = (state, property) => {
  console.log('Update property'+ property)
}

export const showWaveformController = (state, obj) => {
  state.waveformToControl = obj.waveformToControl
  state.showWaveformController = true
}

export const closeWaveformController = state => {
  state.showWaveformController = false
}

export const controlWaveform = (state, control) => {
  //TODO: Update button
}

export const releaseWaveform = (state, obj) => {
  //If Components displayed are from the component released reset component and ports
  if(state.applicationName==obj.releasedAppName){
    console.log('Should reset component and ports')
    state.componentPorts = []
    state.waveformComponents = []
  }else{
    console.log("Don't delete component/ports state. ")
  }
  state.launchedWaveforms = obj.applications

}

export const showLaunchWaveformModal = (state, waveform) => {
  state.waveformToLaunch = waveform
  state.showLaunchWaveformModal = true
}

export const closeLaunchWaveformModal = state => {
  state.showLaunchWaveformModal = false
}

export const launchWaveform = (state, obj) => {
  //Update applications
  state.launchedWaveforms = obj.applications
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
  //TODO: Think about this a little bit more
  state.showTuners = false
  state.showDeviceProperties = false

  if(show.show){
    state.deviceManager = show.deviceManager
    state.showDeviceManager = true
  }else{
      state.showDeviceManager = false
  }
}

export const showDevicePorts = (state, devPorts) => {
      state.devicePorts = devPorts
}

export const showDeviceTuners = (state, show) => {
  if(show.show){
    state.tuners.device = show.device

    state.tuners.unusedTuners = show.tuners.unusedTuners
    state.tuners.usedTuners = show.tuners.usedTuners
    state.showTuners = true
  }else{
    state.showTuners = false
  }
}

export const updateTunersData = (state, tuners) => {
  state.tuners = tuners
}

export const showAllocationModal = (state, show) =>  {
  state.showAllocationModal = show
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
