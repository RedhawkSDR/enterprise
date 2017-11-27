export const setAvailableWaveforms = (state, availableWaveforms) => {
  state.waveforms.catalog = availableWaveforms
}

export const setApplications = (state, applications) => {
  state.applications = applications
}

export const selectApplication = (state, application) => {
  state.application = application
}

export const selectComponent = (state, component) => {
  state.component = component
}

export const selectDomainProperties = (state, properties) => {
  state.domain.properties = properties
}

export const setDeviceManagers = (state, devicemanagers) => {
  state.devicemanagers = devicemanagers
}

export const selectDeviceManager = (state, devicemanager) =>{
  state.devicemanager = devicemanager
}

export const selectDevice = (state, device) =>{
  state.device = device
}

export const setDomainName = (state, value) => {
  state.domain.domainName = value
}

export const setNameServer = (state, value) => {
  state.domain.nameServer = value
}

export const showDialog = (state, value) => {
  state.dialog.show = value
}

export const setDialog = (state, value) => {
  state.dialog.title = value.title
  state.dialog.message = value.message
}

export const setBaseURL = (state, value) => {
  state.configuration.baseURL = value
}

export const setWsBaseURL = (state, value) => {
  state.configuration.wsBaseURL = value
}

export const selectWaveform = (state, value) => {
  state.waveforms.waveformToLaunch = value
}

export const selectPort = (state, value) => {
  state.port.name = value.name
  state.port.type = value.type
  state.port.repId = value.repId

  if(value.connectionIds)
    state.port.connectionIds = value.connectionIds
}

export const setPortWSURL = (state, value) => {
  //Update WS Base URL
  var temp = state.configuration.wsBaseURL+state.domain.nameServer+'/domains/'+state.domain.domainName

  if(value.type=='Component'){
    state.port.wsURL=temp+'/applications/'+value.applicationName+'/components/'+value.componentName+'/ports/'+value.name
  }else{
    state.port.wsURL=temp+'/devicemanagers/'+value.devicemanagerLabel+'/devices/'+value.deviceLabel+'/ports/'+value.name
  }
}

export const setDeviceTuners = (state, value) => {
  state.deviceTuners.used = value.used
  state.deviceTuners.unused = value.unused
}

export const setEventChannels = (state, value) => {
  state.eventchannels = value
}

export const setEventChannel = (state, value) => {
  state.eventchannel.name = value.name
  state.eventchannel.registrantIds = value.registrantIds

  //Set ws url
  var wsURL = state.configuration.wsBaseURL+state.domain.nameServer+'/domains/'+state.domain.domainName+'/eventchannels/'+value.name
  console.log(wsURL)
  state.eventchannel.wsURL = wsURL
}
