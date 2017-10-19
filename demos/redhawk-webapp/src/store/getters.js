//
//Configuration Getters
export const baseURL = state => state.configuration.baseURL
export const domainName = state => state.configuration.domainName
export const nameServer = state => state.configuration.nameServer

//Waveform getters
export const availableWaveforms = state => state.availableWaveforms

//Application getters
export const applications = state => state.applications
export const application = state => state.application

//Previous Exports
export const domainConfigs = state => state.domainConfigs
export const launchedWaveforms = state => state.launchedWaveforms
export const configToEdit = state => state.configToEdit
export const waveformComponents = state => state.waveformComponents
export const componentPorts = state => state.componentPorts
export const componentPropertiesToEdit = state => state.componentPropertiesToEdit
export const propComponentName = state => state.propComponentName
export const showWaveformController = state => state.showWaveformController
export const waveformToControl = state => state.waveformToControl
export const baseURI = state => state.baseURI
export const showLaunchWaveformModal = state => state.showLaunchWaveformModal
export const showEventChannelModal = state => state.showEventChannelModal
export const waveformToLaunch = state => state.waveformToLaunch
export const showDomain = state => state.showDomain
export const wsURL = state => state.wsURL
export const showEditDomainConfig = state => state.showEditDomainConfig
export const configToView = state => state.configToView
export const showWaveformComponents = state => state.showWaveformComponents
export const applicationName = state => state.applicationName
export const portsComponentName = state => state.portsComponentName
export const showComponentProperties = state => state.showComponentProperties
export const portToDisplayName = state => state.portToDisplayName
export const propertyUpdate = state => state.propertyUpdate
export const redhawkRESTRoot = state => state.redhawkRESTRoot
export const showEditRESTModal = state => state.showEditRESTModal
export const showApplication = state => state.showApplication
export const devicemanagers = state => state.devicemanagers
export const showDeviceManager = state => state.showDeviceManager
export const deviceManager = state => state.deviceManager
export const devicePorts = state => state.devicePorts
export const showTuners = state => state.showTuners
export const tuners = state => state.tuners
export const showAllocationModal = state => state.showAllocationModal
export const deviceForPropView = state => state.deviceForPropView
export const showDeviceProperties = state => state.showDeviceProperties
export const eventchannels = state => state.eventchannels
export const eventchannel = state => state.eventchannel
export const showEventChannel = state => state.showEventChannel
export const userName = state => state.userName
export const password = state => state.password
