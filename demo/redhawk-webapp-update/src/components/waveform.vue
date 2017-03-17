<template>
<div v-on:click="fillComponents">
  <md-ink-ripple />
{{ rhwaveform.name}}
</div>
</template>

<script>
import {EventBus} from '../event-bus/event-bus.js'

export default {
  name: 'waveform',
  props: ['waveform'],
  computed: {
    rhwaveform: function(){
      return this.waveform
    }
  },
  methods:{
    fillComponents(){
      EventBus.$emit('updateWaveformComponents', this.rhwaveform)
    }
  }
}
</script>
