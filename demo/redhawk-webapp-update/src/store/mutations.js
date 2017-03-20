import axios from 'axios'

//Add a domain configuation to the state.
export const addDomainConfig = (state, domainConfig) => {
  state.domainConfigs.push(domainConfig)
}

export const deleteDomainConfig = (state, index) => {
  state.domainConfigs.splice(index, 1)
}

export const editDomainConfig = (state, index) => {
  state.domainToEdit = index
  var config = state.domainConfigs[index]
  state.configToEdit = config
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
    console.log(launchedWFJson)
    myState.waveforms = launchedWFJson
  })
}
