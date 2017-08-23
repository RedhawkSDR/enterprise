/*
* Use this method to update data in the index
*/
export const updateIndex = (state, obj) => {
  //TODO: Should be able to do below
  //console.log(state[key])
  console.log(obj)
  if(obj.key=='available'){
    state.available = obj.value
  }else if(obj.key=='appMetricsURL'){
    state.appMetricsURL = obj.value
  }else if(obj.key=='appMetrics'){
    //Update appMetrics and Keys
    state.appMetrics = obj.value
    state.appMetricsKeys = Object.keys(obj.value.metrics)
  }else{
    console.log("Unknown key "+obj.key)
  }
}
