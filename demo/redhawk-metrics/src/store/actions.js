import axios from 'axios'

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
