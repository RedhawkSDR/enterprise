<template>
<md-list>
    <md-subheader>Devices</md-subheader>
    <md-list-item
    v-for="(device, index) in devices"
    >
    {{ device.label }}
  </md-list-item>
</md-list>
</template>

<script>
export default {
  name: 'rhdevices',
  computed: {
    devices(){
      return this.$store.getters.deviceManager.devices
    }
  }
}
</script>
