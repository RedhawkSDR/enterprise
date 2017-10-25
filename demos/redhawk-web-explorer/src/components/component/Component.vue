<template>
  <v-container grid-list-md text-xs-center>
  		<v-layout row wrap>
  			<metadata :component="component"/>
        <ports :ports="component.ports" />
        <properties
          :id="component.name"
          :properties="component.properties"
          />
  		</v-layout>
  	</v-container>
</template>

<script>
import ComponentMeta from './components/ComponentMetadata.vue'
import Properties from '../properties/Properties.vue'
import PortsTable from './components/PortTable'

export default {
  name: 'components',
  components: {
    'metadata' : ComponentMeta,
    'properties' : Properties,
    'ports' : PortsTable
  },
  mounted(){
    var comp = new Object();
    comp.name = this.$route.params.componentName
    comp.applicationName = this.$route.params.applicationName
    this.$store.dispatch('selectComponent', comp)
  },
  computed:{
    component(){
      return this.$store.getters.component
    }
  }
}
</script>
