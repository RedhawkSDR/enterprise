<template>
  <md-layout md-flex="75" md-column>
    <md-toolbar class="md-warn">
      <h1 class="md-title">DeviceManager: {{ deviceManager.label }}</h1>
    </md-toolbar>
    <md-layout md-align="center" class="rowHeight">
      <plot></plot>
    </md-layout>
    <md-layout md-align="center" class="rowHeight">
      <md-layout>
        <md-layout>
          <!--Devices -->
          <devices></devices>
        </md-layout>
        <md-layout>
          <!-- Ports -->
          <h1>Ports</h1>
        </md-layout>
      </md-layout>
    </md-layout>
  </md-layout>
</template>

<script>
import Plot from './Plot.vue'
import Devices from './RHDevices.vue'

export default{
  name: 'rhdevicemanager',
  components: {
    'plot' : Plot,
    'devices' : Devices
  },
  computed: {
    deviceManager(){
      return this.$store.getters.deviceManager
    }
  }
}
</script>
