//Actions for editting domain configuration info.
export const addDomainConfig = ({ commit }, domainConfig) => commit('addDomainConfig', domainConfig)
export const deleteDomainConfig = ( { commit }, index) => commit('deleteDomainConfig', index)
export const editDomainConfig = ({ commit }, index) => commit('editDomainConfig', index)
export const editDomainConfigName = ({ commit }, name) => commit('editDomainConfigName', name)
export const editDomainConfigNameServer = ({ commit }, nameServer) => commit('editDomainConfigNameServer', nameServer)
export const editDomainConfigDomainName = ({ commit }, domainName) => commit('editDomainConfigDomainName', domainName)
export const updateDomainConfig = ({ commit }) => commit('updateDomainConfig')

//Actions to be able to view a domain
export const viewDomainConfig = ({ commit }, index) => commit('viewDomainConfig', index)
export const getWaveformsAvailable = ({ commit }, index) => commit('getWaveformsAvailable', index)

export const showWaveformComponents = ({ commit }, index) => commit('showWaveformComponents', index)
export const showComponentPorts = ({ commit }, index) => commit('showComponentPorts', index)
export const showComponentProperties = ({ commit }, index) => commit('showComponentProperties', index)
export const updateComponentProperty = ({ commit }, property) => commit('updateComponentProperty', property)

//Waveform Controller props
export const showWaveformController = ({ commit }, index) => commit('showWaveformController', index)
export const closeWaveformController = ({ commit }) => commit('closeWaveformController')
export const controlWaveform = ({ commit }, control) => commit('controlWaveform', control)
export const releaseWaveform = ({ commit }, name) => commit('releaseWaveform', name)
export const updateDomainStateAfterWaveformRelease = ({ commit }, name) => commit('updateDomainStateAfterWaveformRelease', name)

//Launch Controls
export const showLaunchWaveformModal = ({ commit }, waveform) => commit('showLaunchWaveformModal', waveform)
export const closeLaunchWaveformModal = ({ commit }) => commit('closeLaunchWaveformModal')
export const launchWaveform = ({ commit }, waveformToLaunch) => commit('launchWaveform', waveformToLaunch)
