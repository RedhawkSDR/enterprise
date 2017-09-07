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
export const showMetricsView = (state, obj) => {
  if(obj.type=='application'){
    //TODO: Add logic for turning off other metrics
    state.application.show=true
    state.application.name=obj.name
    state.application.url=obj.url
  }
}
