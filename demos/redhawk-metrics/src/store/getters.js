export const available = state => state.available
export const nameServer = state => state.nameServer
export const domainName = state => state.domainName
export const baseURL = state => state.baseURL
export const interval = state => state.interval

//Configuration getters
export const showConfiguration = state => state.configuration.show

//App metrics getters
export const appName = state => state.application.name
export const appMetricsURL = state => state.application.url
export const appMetrics = state => state.application.metrics
export const appMetricsIndex = state => state.application.index

//GPP metric getters
export const gppName = state => state.gpp.name
export const gppMetrics = state => state.gpp.metrics
export const gppMetricsURL = state => state.gpp.url

//Port stats getters
export const portName = state => state.port.name
export const portComponentName = state => state.port.component
export const portApplicationName = state => state.port.application
export const portStatistics = state => state.port.statistics
export const portStatisticsURL = state => state.port.url
