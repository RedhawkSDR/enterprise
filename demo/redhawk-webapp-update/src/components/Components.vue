<template>
<div>
  <h1>Components</h1>
  <md-list class="md-dense">
      <md-list-item v-for="component in components">
        <rhcomponent v-bind:component="component"></rhcomponent>
        <md-divider></md-divider>
      </md-list-item>
  </md-list>
</div>
</template>

<script>
import {EventBus} from '../event-bus/event-bus.js'
import axios from 'axios'
import Component from './component.vue'

var baseURI = "http://127.0.0.1:8181/cxf/redhawk/127.0.0.1:2809/domains/REDHAWK_DEV"
var applicationURI = baseURI+'/applications'

export default {
  name: 'waveformcomponents',
  data(){
    return{
      components: []
    }
  },
  components:{
    'rhcomponent' : Component
  },
  methods: {
    updateComponents(applicationName){
          var self = this
          axios.get(applicationURI+'/'+applicationName+'/components.json')
          .then(function(response){
              self.components = response.data.components
          })
          .catch(function(error){
            console.log("ERROR: "+error)
          })
    }
  },
  created(){
    var self = this
    EventBus.$on("updateWaveformComponents", function(data){
        self.updateComponents(data.name)
    });
  }
}
</script>
