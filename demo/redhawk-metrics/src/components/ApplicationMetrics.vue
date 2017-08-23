<template>
<div>
  <h1>testing</h1>
  <md-input-container>
    <label>Enter URL</label>
    <md-input v-model="appMetricsURL"></md-input>
  </md-input-container>
  <md-button class="md-primary md-raised"  v-on:click="getAppMetrics">Get App Metrics</md-button>
  <md-list>
    <md-list-item
      v-for="(component, index) in appMetricKeys"
      v-bind:key="component"
      v-bind:index="index">
      {{ component }}
      <md-button v-on:click="loadInTable(component)">
        View
      </md-button>
    </md-list-item>
  </md-list>
</div>
</template>

<script>
export default {
  name: 'applicationmetrics',
  data() {
    return {
      appMetricsURL: '',
    }
  },
  computed: {
    appMetrics(){
      return this.$store.getters.appMetrics
    },
    appMetricKeys(){
      return this.$store.getters.appMetricsKeys
    }
  },
  methods: {
    getAppMetrics(){
      console.log("Made it")
      console.log("URL: "+this.appMetricsURL)
      this.$store.dispatch("setAppMetricsURL", this.appMetricsURL)
      this.$store.dispatch("getAppMetrics", this.appMetricsURL)
    },
    loadInTable(loadMe){
      console.log(loadMe)
    }
  }
}
</script>
