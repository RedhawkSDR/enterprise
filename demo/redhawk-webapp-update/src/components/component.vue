<template>
<div v-on:click="fillPorts">
  <md-ink-ripple />  
  {{rhcomponent.name}}
</div>
</template>

<script>
import {EventBus} from '../event-bus/event-bus.js'

export default{
  name: 'component',
  props: ['component'],
  computed: {
    rhcomponent: function(){
      return this.component
    }
  },
  methods: {
    fillPorts(){
      EventBus.$emit('updateComponentPorts', this.rhcomponent)
    }
  }
}
</script>
