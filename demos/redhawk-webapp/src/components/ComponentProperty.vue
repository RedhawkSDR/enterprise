<template id="property-template">
<md-list-item>
  <md-input-container v-if="property.type=='simple'">
    <label>{{ property.id }}</label>
    <md-input v-model="property.value"></md-input>
    <md-button @click.native="updateProperty()">Update</md-button>
  </md-input-container>
  <md-button v-else-if="property.type=='struct'" :disabled="true">{{property.id}}</md-button>
  <md-input-container v-else>
    <label>{{ property.id }}</label>
    <md-input v-model="property.value"></md-input>
    <md-button :disabled="true">Update</md-button>
  </md-input-container>
  <md-tooltip md-direction="left" md-delay="500">{{ property.description }}</md-tooltip>  
</md-list-item>
</template>

<script>
export default{
  name: 'componentprop',
  template: '#property-template',
  props: ['property'],
  methods: {
    updateProperty(){
      console.log('Do Update....'+this.property)
      this.$store.dispatch('updateComponentProperty', this.property)
    }
  }
}
</script>
