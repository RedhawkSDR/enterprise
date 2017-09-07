/*
* Use this method to update data in the index
*/
export const updateIndex = (state, obj) => {
  //TODO: Should be able to do below
  //console.log(state[key])
  if(obj.key=='available'){
    state.available = obj.value
  }else if(obj.key=='appMetricsURL'){
    state.appMetricsURL = obj.value
  }else if(obj.key=='appMetrics'){
    //Update appMetrics and Keys
    state.appMetrics = obj.value
    state.appMetricsKeys = Object.keys(obj.value.metrics)
  }else if(obj.key=='appMetricsToView'){
    state.appMetricsToView = obj.value
    state.appMetricsType = obj.metricsName
  }else{
    console.log("Unknown key "+obj.key)
  }
}

/*
* Command and control which metrics are being shown
*/
export const showView = (state, obj) => {
  if(obj=='application'){
    //TODO: Add logic for turning off other metrics
    state.application.show=true
    //state.application.name=obj.name
    //state.application.url=obj.url
  }else if(obj=='gpp'){
    state.gpp.show = true
    //state.gpp.name = obj.name
    //state.gpp.url = obj.url
  }else if(obj=='port'){
    state.port.show = true
    //state.port.name = obj.rep.PORT
    //state.port.applicationName = obj.rep.APP
    //state.port.componentName = obj.rep.COMPONENT
    //state.port.url = obj.url
  }else{
    state.configuration.show = true
  }
}

export const closeView = (state, obj) => {
  if(obj=='application'){
    state.application.show = false
  }else if(obj=='gpp'){
    state.gpp.show = false
  }else if(obj=='port'){
    state.port.show = false
  }else if(obj=='configuration'){
    state.configuration.show = false
  }
}

export const updateGPPMetrics = (state, metrics) => {
  state.gpp.metrics = metrics
}

export const updatePortStats = (state, stats) => {
  state.port.statistics = stats
}

export const editDomainConfigName = (state, name) => {
  state.domainName = name
}

export const editDomainConfigNameServer = (state, nameServer) => {
  state.nameServer = nameServer
}

export const editBaseURL = (state, baseURL) => {
  state.baseURL = baseURL
}
