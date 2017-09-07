import axios from 'axios'

/*
* GET The available metrics
*/
export const getAvailableMetrics = ({ commit, getters }) => {
  //Generate baseURL
  var url = getters.baseURL+'/'+getters.nameServer+'/domains/'+getters.domainName+'/metrics/available'

  axios.get(url)
  .then(function(response){
    console.log("Updating available")
    console.log(response.data)
    var obj = new Object();
    obj.key = 'available'
    obj.value = response.data
    commit("updateIndex", obj)
  })
  .catch(function(error){
    console.log(error)
  })
}

/**
* Setup Metrics for application
*/
export const appMetricsView = ({ commit, getters }, appName) => {
  //Create object with application metadata
  var obj = new Object()
  obj.type = "application"
  obj.name = appName
  obj.url = getters.baseURL+'/'+getters.nameServer+'/domains/'+getters.domainName+'/metrics/application/'+appName

  //Perform initial REST Query
  //TODO: Rethink how this is setup
  axios.get(obj.url)
  .then(function(response){
    var obj = new Object()
    obj.key = "appMetrics"

    //Should only be one entry
    obj.value = response.data[0]

    commit('updateIndex', obj)
  })
  .catch(function(error){
    console.log(error)
  })

  commit("showMetricsView", obj)
}

/*
* Set the applicationMetric URL
*/
export const setAppMetricsURL = ({ commit }, url) => {
  var obj = new Object()
  obj.key = 'appMetricsURL'
  obj.value = url
  commit("updateIndex", obj)
}

/*
* GET the application metrics based on URL
*/
export const getAppMetrics = ({ commit, getters }) => {
  var url = getters.appMetricsURL
  console.log("App "+url)
  axios.get(url)
  .then(function(response){
    var obj = new Object()
    obj.key = "appMetrics"

    //Should only be one entry
    obj.value = response.data[0]

    commit('updateIndex', obj)
  })
  .catch(function(error){
    console.log(error)
  })
}

/*
* GET a specific application metric
*/
export const getAppMetricsByType = ({ commit, getters}, metricsName) => {
  var url = getters.appMetricsURL

  //Create JSON
  var obj = new Object()
  obj.components = [metricsName]
  obj.attributes = []

  axios.post(url, JSON.stringify(obj),
  {
    headers: {
      'Content-Type': 'application/json'
    }
  })
  .then(function(response){
      var obj = new Object()
      obj.key = "appMetricsToView"
      obj.metricsName = Object.keys(response.data)[0]

      //Don't need the key at this point
      obj.value = response.data[obj.metricsName]
      commit('updateIndex', obj)
  })
  .catch(function(error){
      console.log(error)
  })
}
