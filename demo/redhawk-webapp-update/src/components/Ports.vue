<template>
  <div>
    <h1>Ports</h1>
    <md-list class="md-dense">
        <md-list-item v-for="port in ports">
          <md-ink-ripple />
          {{port.name}}
        </md-list-item>
    </md-list>
  </div>
</template>

<script>
import {EventBus} from '../event-bus/event-bus.js'
import axios from 'axios'

var baseURI = "http://127.0.0.1:8181/cxf/redhawk/127.0.0.1:2809/domains/REDHAWK_DEV"
var applicationURI = baseURI+'/applications'

export default{
  name: 'componentports',
  data(){
    return {
      ports: [],
    }
  },
  methods:{
    updatePorts(componentName){
      //Do a split for now come back and clean this up
      var nameArray = componentName.split(':')

      var portsURI = applicationURI+'/'+nameArray[1].substring(0,nameArray[1].length-2)+'/components/'+componentName+'/ports.json'
      var self = this
      axios.get(portsURI)
      .then(function(response){
        self.ports = response.data.ports
      })
      .catch(function(error){
        console.log("ERROR "+error)
      })
    }
  },
  created(){
    var self = this
    EventBus.$on("updateComponentPorts", function(data){
        console.log("Populate ports "+data.name)
        self.updatePorts(data.name)
    });
  }
}
</script>
