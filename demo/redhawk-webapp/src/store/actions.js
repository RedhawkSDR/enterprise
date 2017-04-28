//Axios is asnyc so all axios calls should really occur in this class
import axios from 'axios'

//Helper functions
function getUsedTuners(deviceLabel){
  var deviceUsedTuners = state.baseURI+'/devicemanagers/'+state.deviceManager.label+'/devices/'+deviceLabel+'/tuners/USED'

  return axios.get(deviceUsedTuners)
}

function getUnusedTuners(deviceLabel){
  var deviceUnusedTuners = state.baseURI+'/devicemanagers/'+state.deviceManager.label+'/devices/'+deviceLabel+'/tuners/UNUSED'

  return axios.get(deviceUnusedTuners)
}

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
//export const updateLaunchedWaveforms = ({ commit }) => commit('updateLaunchedWaveforms')

export const plotPortData = ({ commit }, port) => commit('plotPortData', port)
export const closeEditDomainConfig = ({ commit }) => commit('closeEditDomainConfig')
export const closeEditPropsConfig = ({ commit }) => commit('closeEditPropsConfig')

//Reset initial state
export const resetDomain = ({ commit }) => commit('resetDomain')
export const resetWaveformDisplay = ({ commit }) => commit('resetWaveformDisplay')

export const showApplication = ({ commit }, show) => commit('showApplication', show)
export const showDeviceManager = ({ commit }, show) => commit('showDeviceManager', show)

export const showDevicePorts = ({ commit }, show) => commit('showDevicePorts', show)
export const showDeviceTuners = ({ commit }, show) => commit('showDeviceTuners', show)
export const showDeviceProperties = ({ commit }, show) => commit('showDeviceProperties', show)

export const deallocate = ({ commit }, deallocate) => commit('deallocate', deallocate)
export const showAllocationModal = ({ commit }, show) => commit('showAllocationModal', show)
export const allocate = (context, allocate) => {
  //context
  console.log(context)
  //commit('allocate', allocate)
}

export const updateRedhawkRESTRoot = ({ commit }, updateURL ) => commit('updateRedhawkRESTRoot', updateURL)
