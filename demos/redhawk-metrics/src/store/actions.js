import axios from 'axios'

/*
* GET The available metrics
*/
export const getAvailableMetrics = ({ commit, getters }, type) => {
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
export const chooseApplication = ({ commit, getters }, appName) => {
  //Create object with application metadata
  var obj = new Object()
  obj.name = appName
  obj.url = getters.baseURL+'/'+getters.nameServer+'/domains/'+getters.domainName+'/metrics/application/'+appName

  //Perform initial REST Query
  //TODO: Rethink how this is setup
  axios.get(obj.url)
  .then(function(response){
    //Should only be one entry
    obj.metrics = response.data[0]["metrics"]

    commit('updateAppState', obj)
  })
  .catch(function(error){
    console.log(error)
  })
}

/*
* Update Application Metrics
*/
export const updateApplicationMetrics = ({ commit, getters }) => {
  //You already know which port stats need to be updated
  var url = getters.appMetricsURL

  axios.get(url)
  .then(function(response){
    commit('updateApplicationMetrics', response.data[0]["metrics"])
  })
  .catch(function(error){
    console.log(error)
  })
}

/*
* Chose GPP metrics
*/
export const chooseGPP = ({ commit, getters }, gppName) => {
  //Create object with GPP metadata
  var obj = new Object()
  obj.name = gppName
  obj.url = getters.baseURL+'/'+getters.nameServer+'/domains/'+getters.domainName+'/metrics/gpp/'+gppName

  //Perform initial REST Query
  //TODO: Rethink how this is setup
  axios.get(obj.url)
  .then(function(response){
    //Should only be one entry
    obj.metrics = response.data[0]

    commit('updateGPPState', obj)
  })
  .catch(function(error){
    console.log(error)
  })
}

/*
* Update GPP Metrics
*/
export const updateGPPMetrics = ({ commit, getters }) => {
  //You already know which port stats need to be updated
  var url = getters.gppMetricsURL

  axios.get(url)
  .then(function(response){
    commit('updateGPPMetrics', response.data[0])
  })
  .catch(function(error){
    console.log(error)
  })
}

/*
* Choose Port Statistics
*/
export const choosePort = ({ commit, getters }, port) => {
  //Create object with Port metadata
  var obj = port
  obj.url = getters.baseURL+'/'+getters.nameServer+'/domains/'+getters.domainName+'/applications/'+port.application
    +'/components/'+port.component+'/ports/'+port.port+'/statistics.json'

  axios.get(obj.url)
  .then(function(response){
    console.log(response.data)
    obj.statistics = response.data.statistics[0]

    commit('updatePortState', obj)
  })
  .catch(function(error){

  })
}

/*
* Update Port Statistics
*/
export const updatePortStats = ({ commit, getters }) => {
  //You already know which port stats need to be updated
  var url = getters.portStatisticsURL

  axios.get(url)
  .then(function(response){
    commit('updatePortStats', response.data.statistics[0])
  })
  .catch(function(error){
    console.log(error)
  })
}

export const editDomainConfigNameServer = ({ commit }, nameServer) => commit('editDomainConfigNameServer', nameServer)
export const editDomainConfigName = ({ commit }, domainName) => commit('editDomainConfigName', domainName)
export const editBaseURL = ({ commit }, baseURL) => commit('editBaseURL', baseURL)

/**
* Setup Metrics for GPP
*/
export const gppMetricsView = ({ commit, getters }, device) => {
  var obj = new Object()
  obj.type ="gpp"
  obj.name = device
  obj.url = getters.baseURL+'/'+getters.nameServer+'/domains/'+getters.domainName+'/metrics/gpp/'+device

  //Perform initial REST Query
  axios.get(obj.url)
  .then(function(response){
    commit('updateGPPMetrics', response.data[0])
  })
  .catch(function(error){
    console.log(error)
  })

  commit('closeView', 'port')
  commit('closeView', 'application')

  //Show metrics view
  commit("showView", obj)
}

/**
* Setup Port Metrics View
*/
export const portMetricsView = ({ commit, getters }, port) => {
  var obj = new Object()
  obj.type ="port"
  obj.rep = port

  //TODO: Use metrics endpoint instead
  obj.url = getters.baseURL+'/'+getters.nameServer+'/domains/'+getters.domainName+'/applications/'+
    port.APP+'/components/'+port.COMPONENT+'/ports/'+port.PORT+'/statistics'

  axios.get(obj.url)
  .then(function(response){
    commit('updatePortStats', response.data.statistics[0])
  })
  .catch(function(error){
    console.log(error)
  })

  commit('closeMetricsView', 'application')
  commit('closeMetricsView', 'gpp')
  commit('showView', obj)
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
