<template>
<md-layout>
  <md-list class="md-dense">
      <md-subheader>{{ applicationName }} :: Components</md-subheader>
      <md-list-item
      v-for="(component, index) in components"
      v-bind:key="component"
      v-bind:index="index">
      {{ component.name}}
      <md-menu md-direction="bottom left">
        <md-button md-menu-trigger>
          <md-icon>menu</md-icon>
        </md-button>
        <md-menu-content>
          <md-menu-item @click.native="editComponent(index)">Edit</md-menu-item>
          <md-menu-item @click.native="showPorts(index)">Ports</md-menu-item>
        </md-menu-content>
      </md-menu>
      <md-divider></md-divider>
      </md-list-item>
  </md-list>
</md-layout>
</template>

<script>
import axios from 'axios'

export default {
  name: 'waveformcomponents',
  computed: {
    components(){
      return this.$store.getters.waveformComponents
    },
    applicationName(){
      return this.$store.getters.applicationName
    }
  },
  methods: {
    showPorts(index){
      console.log("Show ports for this index "+index)
      this.$store.dispatch('showComponentPorts', index)
    },
    editComponent(index){
      this.$store.dispatch('showComponentProperties', index)
    }
  }
}
</script>
