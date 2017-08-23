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
