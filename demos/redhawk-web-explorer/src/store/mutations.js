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
  state.configuration.domainName = value
}

export const setNameServer = (state, value) => {
  state.configuration.nameServer = value
}

export const selectWaveform = (state, value) => {
  state.waveforms.waveformToLaunch = value
}
