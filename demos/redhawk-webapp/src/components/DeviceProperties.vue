<!-- TODO: Should be able to only have one properties vue that contains all logic -->
<template>
  <md-layout md-flex="25" v-if="showDeviceProperties">
    <md-list class="md-dense">
      <md-subheader>
        <md-button @click.native="close" class="md-icon-button">
          <md-icon>close</md-icon>
        </md-button>
        {{ device.label }}
      </md-subheader>
      <md-list-item
        v-for="(property, index) in properties"
        :key="property"
        :property="property"
      >
      <md-input-container>
        <label>{{ property.name }}</label>
        <md-input v-model="property.value" :disabled="true"></md-input>
      </md-input-container>
      <md-tooltip md-direction="left" md-delay="500">{{ property.description }}</md-tooltip>        
      </md-list-item>
    </md-list>
  </md-layout>
</template>

<script>
export default{
  name: 'deviceproperties',
  computed: {
    showDeviceProperties(){
        return this.$store.getters.showDeviceProperties
    },
    device(){
      return this.$store.getters.deviceForPropView
    },
    properties(){
      return this.device.properties
    }
  },
  methods: {
    close(){
      var obj = new Object()
      obj.show = false
      this.$store.dispatch('showDeviceProperties', obj)
    }
  }
}
</script>
