<template>
  <v-container grid-list-md text-xs-center>
      <v-layout row wrap>
        <devicetable :devices="devicemanager.devices" />
        <properties
          :id="devicemanager.label"
          :properties="devicemanager.properties"
          />
      </v-layout>
    </v-container>
</template>
<script>
import Properties from '../properties/Properties.vue'
import DeviceTable from './components/DeviceTable.vue'

export default {
  name: 'devicemanager',
  created(){
    this.$store.dispatch('selectDeviceManager', this.$route.params.devicemanagerLabel)
  },
  computed: {
    devicemanager(){
      return this.$store.getters.devicemanager
    }
  },
  components: {
    'properties' : Properties,
    'devicetable' : DeviceTable
  }
}
</script>
