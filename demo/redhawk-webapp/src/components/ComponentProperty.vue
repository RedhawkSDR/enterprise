<template id="property-template">
  <!--
  <div v-if="property.type=='simple'">
          {{property.id}}<input v-model="property.value"></input><button v-on:click="updateProperty">update</button>
  </div>
-->
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
