<template>
<div class="content">
  <div class="container-fluid">
    <div class="row">
      <metadata :device="device"/>
    </div>
    <div class="row">
      <ports :device="device"/>
    </div>
    <div class="row">
      <properties
        :bgColor="green"
        :id="device.label"
        :properties="device.properties" />
    </div>
  </div>
</div>
</template>

<script>
import DeviceMeta from './components/DeviceMetadata'
import Properties from '../properties/Properties.vue'
import DevicePorts from './components/PortList.vue'

export default {
  name: 'device',
  mounted(){
    var dev = new Object()
    dev.label = this.$route.params.deviceLabel
    dev.devicemanagerLabel = this.$route.params.devicemanagerLabel

    this.$store.dispatch('selectDevice', dev)
  },
  components: {
    'metadata' : DeviceMeta,
    'properties' : Properties,
    'ports' : DevicePorts
  },
  computed: {
    device(){
      return this.$store.getters.device
    }
  }
}
</script>
