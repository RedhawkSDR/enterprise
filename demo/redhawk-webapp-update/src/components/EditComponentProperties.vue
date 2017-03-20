<template>
<div>
  <h2>{{ name }}</h2>
  <md-list class="md-dense">
    <md-list-item
      v-for="(property, index) in properties"
      :key="property"
      v-bind:propVal="property.val"
      v-bind:index="index"
      >
      <!--UGH This is ugly figure out below-->
      <componentprop v-bind:property="property"></componentprop>
    </md-list-item>
  </md-list>
  <!--TODO: Figure out why this is giving me a hard time....
  Probably has something to do with this: https://github.com/vuejs/vue/issues/820
  <componentprop
    v-for="property in properties",
    :key='property.id',
    v-bind:property="property"
  >
  </componentprop>
  -->
</div>
</template>

<script>
import ComponentProperty from './ComponentProperty.vue'

export default {
  name: 'editcomponentprops',
  computed: {
    properties(){
      console.log('Getting properties')
      return this.$store.getters.componentPropertiesToEdit
    },
    name(){
      return this.$store.getters.propComponentName
    }
  },
  components:{
    'componentprop': ComponentProperty
  }
}
</script>
