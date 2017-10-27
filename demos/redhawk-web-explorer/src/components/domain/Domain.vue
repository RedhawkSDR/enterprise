<template>
  <v-container grid-list-md text-xs-center>
    <v-layout row wrap>
      <settings />
      <properties
        :id="domainName"
        :properties="properties"
        />
    </v-layout>
  </v-container>
</template>

<script>
import Properties from '../properties/Properties.vue'
import Settings from './components/Settings.vue'

export default {
  name: 'domain',
  mounted(){
    this.$store.dispatch('selectDomainProperties', this.domainName)
  },
  computed: {
    domainName(){
      return this.$store.getters.domainName
    },
    properties(){
      return this.$store.getters.domainProperties
    }
  },
  components: {
    'properties' : Properties,
    'settings' : Settings
  }
}
</script>
