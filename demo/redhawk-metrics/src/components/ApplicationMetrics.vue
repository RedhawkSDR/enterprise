<template>
<div>
  <h1>testing</h1>
  <md-input-container>
    <label>Enter URL</label>
    <md-input v-model="appMetricsURL"></md-input>
  </md-input-container>
  <md-button class="md-primary md-raised"  v-on:click="getAppMetrics">Get App Metrics</md-button>
  <md-layout md-gutter>
    <md-layout md-flex="30">
      <span class="md-title">App Metrics</span>
      <md-list>
        <md-list-item
          v-for="(component, index) in appMetricsKeys"
          v-bind:key="component"
          v-bind:index="index">
          {{ component }}
          <md-button v-on:click="loadInTable(component)">
            View
          </md-button>
        </md-list-item>
      </md-list>
    </md-layout>
    <md-layout md-flex>
      <md-table>
        <md-table-header>
          <md-table-row>
            <md-table-head>Metric Name</md-table-head>
            <md-table-head md-numeric>Value</md-table-head>
          </md-table-row>
        </md-table-header>

        <md-table-body>
          <md-table-row v-for="(row, index) in Object.keys(appMetricsToView)" :key="index">
            <md-table-cell>{{ row }}</md-table-cell>
            <md-table-cell>{{ appMetricsToView[row]}}</md-table-cell>
          </md-table-row>
        </md-table-body>
      </md-table>
    </md-layout>
  </md-layout>
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
    appMetricsKeys(){
      return this.$store.getters.appMetricsKeys
    },
    appMetricsToView(){
      return this.$store.getters.appMetricsToView
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
      this.$store.dispatch("getAppMetricsByType", loadMe)
    }
  }
}
</script>
