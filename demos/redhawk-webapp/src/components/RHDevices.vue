<template>
<md-list>
    <md-subheader>Devices</md-subheader>
    <device v-for="(device, index) in devices" v-bind:device="device"></device>
</md-list>
</template>

<script>
import RHDevice from './RHDevice.vue'

export default {
  name: 'rhdevices',
  computed: {
    devices(){
      return this.$store.getters.deviceManager.devices
    }
  },
  components: {
    'device': RHDevice
  }
}
</script>
