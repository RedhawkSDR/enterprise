<template>
  <md-layout>
    <md-layout md-flex md-column>
      <md-toolbar class="md-warn">
        <h1 class="md-title">Application: {{ application.name }}</h1>
      </md-toolbar>
      <plot></plot>
      <md-layout md-align="center" class="rowHeight">
        <waveformcomponents></waveformcomponents>
        <componentports></componentports>
      </md-layout>
    </md-layout>
    <md-layout md-flex='25' v-if="showComponentProperties">
      <editcomponentprops></editcomponentprops>
    </md-layout>
  </md-layout>
</template>

<script>
import Plot from './Plot.vue'
import Components from './Components.vue'
import Ports from './Ports.vue'
import EditComponentProperties from './EditComponentProperties.vue'

export default {
  name: 'redhawkapplication',
  components: {
    'plot': Plot,
		'waveformcomponents': Components,
		'componentports': Ports,
		'editcomponentprops': EditComponentProperties
  },
  computed: {
		showComponentProperties(){
			return this.$store.getters.showComponentProperties
		},
    application(){
      return this.$store.getters.application
    }
  }
}
</script>
