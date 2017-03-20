<template>
<div>
  <h1>Components</h1>
  <md-list class="md-dense">
      <md-list-item
      v-for="(component, index) in components"
      v-bind:key="component"
      v-bind:index="index">
      {{ component.name}}
      <md-menu md-direction="bottom left">
        <md-button md-menu-trigger>
          <md-icon>menu</md-icon>
        </md-button>
        <md-menu-content>
          <md-menu-item>Edit</md-menu-item>
          <md-menu-item @click.native="showPorts(index)">Ports</md-menu-item>
        </md-menu-content>
      </md-menu>
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
  computed: {
    components(){
      return this.$store.getters.waveformComponents
    }
  },
  components:{
    'rhcomponent' : Component
  },
  methods: {
    showPorts(index){
      console.log("Show ports for this index "+index)
      this.$store.dispatch('showComponentPorts', index)
    }
  }
}
</script>
