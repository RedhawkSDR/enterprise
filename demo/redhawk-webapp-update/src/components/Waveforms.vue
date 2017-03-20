<template>
<div>
<h1>Waveforms</h1>
<md-list class="md-dense">
  <md-list-item
    v-for="(waveform, index) in waveforms"
    v-bind:key="waveform"
    v-bind:index="index">
    <waveform v-bind:index="index" v-bind:waveform="waveform"></waveform>
    <md-divider></md-divider>
  </md-list-item>
</md-list>
</div>
</template>

<script>
import Waveform from './waveform.vue'
import {EventBus} from '../event-bus/event-bus.js'
import axios from 'axios'

var baseURI = "http://127.0.0.1:8181/cxf/redhawk/127.0.0.1:2809/domains/REDHAWK_DEV"

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
  }
}
</script>
