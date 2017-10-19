<template>
<div class="content">
  <div class="container-fluid">
    <div class="row">
      <metadata :application="application"/>
    </div>
    <div class="row">
      <componenttable :application="application"/>
    </div>
    <div class="row">
      <properties
        :bgColor="propertiesBg"
        :id="application.name"
        :properties="application.properties" />
    </div>
  </div>
</div>
</template>

<script>
import MetaData from './components/ApplicationMeta.vue'
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
  },
  data(){
    return {
      propertiesBg: 'green'
    }
  }
}
</script>
