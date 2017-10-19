<template>
<div class="content">
  <div class="container-fluid">
    <div class="row">
      <metadata :component="component"/>
    </div>
    <div class="row">
      <ports :component="component"/>
    </div>
    <div class="row">
      <properties
        :bgColor="green"
        :id="component.name"
        :properties="component.properties" />
    </div>
  </div>
</div>
</template>

<script>
import ComponentMeta from './components/ComponentMeta.vue'
import Properties from '../properties/Properties.vue'
import Ports from './components/PortList.vue'

export default {
  name: 'component',
  mounted(){
    var comp = new Object();
    comp.name = this.$route.params.componentName
    comp.applicationName = this.$route.params.applicationName
    this.$store.dispatch('selectComponent', comp)
  },
  components: {
    'metadata' : ComponentMeta,
    'properties' : Properties,
    'ports' : Ports
  },
  computed:{
    component(){
      return this.$store.getters.component
    }
  }
}
</script>
