<template>
  <div>
    <h1>Ports</h1>
    <md-list class="md-dense">
        <md-list-item v-for="port in ports">
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
  computed: {
    ports(){
      return this.$store.getters.componentPorts
    }
  }
}
</script>
