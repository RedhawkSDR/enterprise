<template>
  <v-container grid-list-md text-xs-center>
  		<v-layout row wrap>
  			<metadata :device="device"/>
        <tuners v-if="deviceKind=='FRONTEND::TUNER'":device="device" />
        <ports :ports="device.ports"/>
        <properties
          :id="device.label"
          :properties="device.properties"
          type="Device"
          />
  		</v-layout>
  	</v-container>
</template>

<script>
import DeviceMeta from './components/DeviceMetadata'
import Properties from '../properties/Properties.vue'
import DevicePorts from './components/PortTable.vue'
import Tuners from './components/Tuners'

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
    'ports' : DevicePorts,
    'tuners' : Tuners
  },
  computed: {
    device(){
      return this.$store.getters.device
    },
    deviceKind(){
      var devProperties = this.$store.getters.device.properties

      for(var i = 0; devProperties.length; i++){
        console.log(devProperties[i].name)
        if(devProperties[i].name=='device_kind'){
          return devProperties[i].value
        }
      }

      return null
    }
  },
  methods: {
    isTunerDevice(properties){
      console.log(properties)
      for(var i = 0; properties.length; i++){
        console.log(properties[i].name)
        if(properties[i].name=='device_kind' && properties[i].value=='FRONTEND::TUNER'){
          this.isTuner = true;
        }else if(properties[i].name=='device_kind'){
          console.log("Found device kind")
        }
      }
    }
  }
}
</script>
