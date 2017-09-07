<template>
<div>
<h1>Port: {{ name }}</h1>
<h2>Comp: {{ componentName }}</h2>
<h3>App: {{ appName }}</h3>
</div>
</template>

<script>
export default {
  name: 'portstats',
  computed: {
    name(){
      return this.$store.getters.portName
    },
    appName(){
      return this.$store.getters.portApplicationName
    },
    componentName(){
      return this.$store.getters.portComponentName
    },
    statistics(){
      return this.$store.getters.portStatistics
    }
  }
}
</script>

<style>
</style>
