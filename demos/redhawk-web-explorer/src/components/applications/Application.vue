<template>
  <v-container grid-list-md text-xs-center>
  		<v-layout row wrap>
  			<metadata :application="application"/>
        <componenttable :components="application.components"/>
        <properties
          :id="application.name"
          :properties="application.properties"
          />
  		</v-layout>
  	</v-container>
</template>

<script>
import MetaData from './components/ApplicationMetadata.vue'
import ComponentTable from './components/ComponentTable.vue'
import Properties from '../properties/Properties.vue'

export default {
  name: 'application',
  mounted(){
    this.$store.dispatch('selectApplication', this.$route.params.applicationName)
  },
  components: {
    'metadata' : MetaData,
    'componenttable' : ComponentTable,
    'properties' : Properties
  },
  computed:{
    application(){
      return this.$store.getters.application
    }
  }
}
</script>
