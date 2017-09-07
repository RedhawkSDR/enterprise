<template>
<div>
  <div class="metricslist">
    <md-toolbar class="logo md-warn">
      <h2 class="md-title">GPPs</h2>
    </md-toolbar>
    <md-list>
      <md-list-item
        v-for="(gpp, index) in availableGPPMetrics"
        v-bind:key="gpp"
        @click="showGPP(gpp)">
        {{ gpp }}
      </md-list-item>
    </md-list>
  </div>
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
    },
    availableGPPMetrics(){
      return this.$store.getters.available.GPP
    }
  }
}
</script>

<style>
</style>
