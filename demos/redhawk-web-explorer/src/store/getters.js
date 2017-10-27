//
//Configuration Getters
export const baseURL = state => state.configuration.baseURL
export const wsBaseURL = state => state.configuration.wsBaseURL

//dialog
export const showDialog = state => state.dialog.show
export const dialogTitle = state => state.dialog.title
export const dialogMessage = state => state.dialog.message

//Waveform getters
export const waveformcatalog = state => state.waveforms.catalog
export const waveformToLaunch = state => state.waveforms.waveformToLaunch

//Application getters
export const applications = state => state.applications
export const application = state => state.application
export const applicationName = state => state.application.name

//Component getters
export const component = state => state.component

//domain
export const domainName = state => state.domain.domainName
export const nameServer = state => state.domain.nameServer
export const domainProperties = state => state.domain.properties

//device managers
export const devicemanagers = state => state.devicemanagers
export const devicemanager = state => state.devicemanager
export const device = state => state.device

//Port
export const port = state => state.port
export const portWSURL = state => state.port.wsURL
