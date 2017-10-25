<template>
  <v-container grid-list-md text-xs-center>
  		<v-layout row wrap>
  			<metadata :device="device"/>
        <ports :ports="device.ports"/>
        <properties
          :id="device.label"
          :properties="device.properties"
          />
  		</v-layout>
  	</v-container>
</template>

<script>
import DeviceMeta from './components/DeviceMetadata'
import Properties from '../properties/Properties.vue'
import DevicePorts from './components/PortTable.vue'

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
