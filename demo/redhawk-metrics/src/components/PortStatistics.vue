<template>
  <div class="content">
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'portstats',
  mounted(){
    this.$store.dispatch("getAvailableMetrics", 'application')
  },
}
</script>

<style>
</style>
