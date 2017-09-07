<template>
<div>
  <h1>GPP: {{ name }}</h1>
</div>
</template>

<script>
export default {
  name: 'gppmetrics',
  computed: {
    name(){
      return this.$store.getters.gppName
    },
    metrics(){
      return this.$store.getters.gppMetrics
    }
  }
}
</script>

<style>
</style>
