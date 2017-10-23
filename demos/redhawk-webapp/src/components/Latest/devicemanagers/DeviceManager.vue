<template>
<div class="content">
  <div class="container-fluid">
    <div class="row">

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

export default {
  name: 'devicemanager',
  mounted(){
    this.$store.dispatch('selectDeviceManager', this.$route.params.devicemanagerName)
  },
  computed: {
    devicemanager(){
      return this.$store.getters.devicemanager
    }
  },
  components: {
    'properties' : Properties
  }
}
</script>
