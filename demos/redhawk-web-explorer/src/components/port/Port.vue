<template>
  <v-container grid-list-md text-xs-center>
  		<v-layout row wrap>
        <sigplot
          v-if="port.type=='uses'"
          :port="port"
          :meta="portURIMeta" />
  			<metadata
          :port="port"
          :meta="portURIMeta" />
  		</v-layout>
  	</v-container>
</template>

<script>
import PortMetadata from './components/PortMetadata'
import SigPlot from './components/SigPlot'

export default{
  name: 'port',
  data(){
    return {
      portURIMeta : null
    }
  },
  mounted(){
    this.portURIMeta = new Object();
    if(this.$route.params.applicationName){
      console.log("Dealing with Component")
      this.portURIMeta.applicationName = this.$route.params.applicationName
      this.portURIMeta.componentName = this.$route.params.componentName
      this.portURIMeta.type = "Component"
    }else{
      console.log("Dealing with device")
      this.portURIMeta.deviceLabel = this.$route.params.deviceLabel
      this.portURIMeta.devicemanagerLabel = this.$route.params.devicemanagerLabel
      this.portURIMeta.type = "Device"
    }

    this.portURIMeta.name = this.$route.params.portName

    this.$store.dispatch('selectPort', this.portURIMeta)
    this.$store.dispatch('setPortWSURL', this.portURIMeta)
  },
  components: {
    'metadata' : PortMetadata,
    'sigplot' : SigPlot
  },
  computed: {
    port(){
      return this.$store.getters.port
    }
  }
}
</script>
