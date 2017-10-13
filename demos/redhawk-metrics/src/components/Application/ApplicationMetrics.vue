<template>
<div class="content">
  <router-view></router-view>
</div>
</template>

<style>

</style>

<script>
import ApplicationMetricsList from './ApplicationMetricsList'

export default {
  name: 'appmetrics',
  created(){
    this.$store.dispatch("getAvailableMetrics", 'application')
  }
}
</script>
