<template>
  <md-layout>
  <md-layout md-flex='75' md-column>
    <md-toolbar class="md-warn">
      <h1 class="md-title">DeviceManager: {{ deviceManager.label }}</h1>
    </md-toolbar>
    <md-layout md-align="center" class="rowHeight" v-if="!gppDeviceFound">
      <plot></plot>
    </md-layout>
    <md-layout md-align="center" class="rowHeight">
        <md-layout>
          <!--Devices -->
          <devices></devices>
        </md-layout>
        <md-layout v-if="!gppDeviceFound">
          <!-- Ports -->
          <deviceports></deviceports>
        </md-layout>
    </md-layout>
  </md-layout>
  <md-layout md-flex='25' v-if="showTuners">
    <tuners></tuners>
  </md-layout>
</md-layout>
</template>

<script>
import Plot from './Plot.vue'
import Devices from './RHDevices.vue'
import DevicePorts from './RHDevicePorts.vue'
import Tuners from './Tuners.vue'

export default{
  name: 'rhdevicemanager',
  components: {
    'plot' : Plot,
    'devices' : Devices,
    'deviceports' : DevicePorts,
    'tuners' : Tuners
  },
  computed: {
    deviceManager(){
      return this.$store.getters.deviceManager
    },
    gppDeviceFound(){
      var deviceLabel = this.deviceManager.devices[0].label
      if(deviceLabel.startsWith("GPP")){
        return true
      }else {
        return false
      }
    },
    showTuners(){
      if(!this.gppDeviceFound && this.$store.getters.showTuners){
        return true
      }else{
        return false
      }
    }
  }
}
</script>
