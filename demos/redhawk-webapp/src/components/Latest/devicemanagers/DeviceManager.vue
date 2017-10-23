<template>
<div class="content">
  <div class="container-fluid">
    <div class="row">
      <devicetable :devices="devicemanager.devices" />
    </div>
    <div class="row">
      <properties
        :bgColor="green"
        :id="devicemanager.label"
        :properties="devicemanager.properties" />
    </div>
  </div>
</div>
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
