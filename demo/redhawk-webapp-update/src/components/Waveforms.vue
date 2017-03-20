<template>
<div>
<h1>Waveforms</h1>
<md-list class="md-dense">
  <md-list-item
    v-for="(waveform, index) in waveforms"
    v-bind:key="waveform"
    v-bind:index="index">
    {{waveform.name}}
    <md-menu md-direction="bottom left">
      <md-button md-menu-trigger>
        <md-icon>menu</md-icon>
      </md-button>
      <md-menu-content>
        <md-menu-item>Control</md-menu-item>
        <md-menu-item @click.native="showComponents(index)">Components</md-menu-item>
      </md-menu-content>
    </md-menu>
    <!--<waveform v-bind:index="index" v-bind:waveform="waveform"></waveform>-->
    <md-divider></md-divider>
  </md-list-item>
</md-list>
</div>
</template>

<script>
import Waveform from './waveform.vue'
import {EventBus} from '../event-bus/event-bus.js'
import axios from 'axios'

export default {
  name: 'rhwaveforms',
  computed: {
    waveforms(){
      console.log('Computing waveforms')
      return this.$store.getters.waveforms
    }
  },
  components: {
    'waveform': Waveform
  },
  methods: {
    showComponents(data){
      console.log("Showing components for waveforms at index "+data)
      this.$store.dispatch('showWaveformComponents', data)
    }
  }
}
</script>
