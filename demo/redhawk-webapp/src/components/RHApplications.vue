<template>
<md-list>
  <md-list-item>
    <span>Applications</span>
    <md-list-expand>
      <md-list-item
        class="md-inset"
        v-for="(waveform, index) in waveforms"
        v-bind:key="waveform"
        v-bind:index="index"
        v-bind:waveform="waveform">
        <!--
        TODO: Nice To have make play and stop show up next to waveform indicating state
        <md-button v-if="waveform.started" class="md-icon-button" @click.native="stop(waveform)">
          <md-icon>stop</md-icon>
        </md-button>
        <md-button v-else class="md-icon-button"@click.native="start(waveform)" >
          <md-icon>play_arrow</md-icon>
        </md-button>
        -->
        <span>{{waveform.name}}</span>
        <md-menu md-direction="bottom left">
          <md-button md-menu-trigger>
            <md-icon>menu</md-icon>
          </md-button>
          <md-menu-content>
            <md-menu-item @click.native="showController(index)">Control</md-menu-item>
            <md-menu-item @click.native="showComponents(index)">View</md-menu-item>
          </md-menu-content>
        </md-menu>
        <!--<waveform v-bind:index="index" v-bind:waveform="waveform"></waveform>-->
        <md-divider></md-divider>
      </md-list-item>
    </md-list-expand>
  </md-list-item>
</md-list>
</template>

<script>
import Waveform from './waveform.vue'
import axios from 'axios'

export default {
  name: 'rhwaveforms',
  computed: {
    waveforms(){
      console.log('Computing waveforms')
      return this.$store.getters.launchedWaveforms
    },
    showWaveformComponents(){
      return this.$store.getters.showWaveformComponents
    }
  },
  components: {
    'waveform': Waveform
  },
  methods: {
    showComponents(data){
      console.log("Showing components for waveforms at index "+data)
      this.$store.dispatch('resetWaveformDisplay')
      this.$store.dispatch('showWaveformComponents', data)

      //Make sure device manager is no longer being shown
      var showDevManager = new Object()
      showDevManager.show = false
      this.$store.dispatch('showDeviceManager', showDevManager) 
      this.$store.dispatch('showApplication', true)
    },
    showController(data){
      console.log('Show Controller for component at index '+data)
      this.$store.dispatch('showWaveformController', data)
    }
  }
}
</script>
