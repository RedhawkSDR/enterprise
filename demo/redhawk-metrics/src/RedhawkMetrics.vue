<template>
  <div>
    <md-toolbar>
      REDHAWK Metrics
    </md-toolbar>
    <md-layout md-gutter>
      <md-layout md-flex="30">
        <availablemetrics/>
      </md-layout>
      <md-layout>
        <appmetrics/>
      </md-layout>
    </md-layout>
  </div>
</template>

<script>
import AvailableMetrics from './components/AvailableMetrics.vue'
import ApplicationMetrics from './components/ApplicationMetrics.vue'

export default {
  name: 'redhawkmetrics',
  components: {
    'availablemetrics' : AvailableMetrics,
    'appmetrics' : ApplicationMetrics
  }
}
</script>


<style>
/* Root node should not be indented */
.tree-view-item-root {
  margin-left: 0;
}
</style>
