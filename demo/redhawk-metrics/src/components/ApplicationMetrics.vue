<template>
<div>
  <div class="metricslist">
      <md-toolbar class="logo md-warn">
        <h2 class="md-title">Applications</h2>
      </md-toolbar>
      <md-list>
        <md-list-item
          v-for="(application, index) in availableAppMetrics"
          v-bind:key="application"
          @click="showApplication(application)">
          {{ application }}
        </md-list-item>
      </md-list>
  </div>
  <div>

  </div>
  <!--
  <md-layout md-gutter>
    <md-layout md-flex="30">
      <span class="md-title">Metrics</span>
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
  -->
</div>
</template>

<style>

</style>

<script>
export default {
  name: 'applicationmetrics',
  data() {
    return {
      interval: null,
      metricType: null
    }
  },
  computed: {
    availableAppMetrics(){
      return this.$store.getters.available.APPLICATION
    },
    appName(){
      return this.$store.getters.appName
    },
    appMetricsURL(){
      return this.$store.getters.appMetricsURL
    },
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
      this.metricType = loadMe
      this.$store.dispatch("getAppMetricsByType", loadMe)

      //TODO: Use set interval
      //this.intervalSetup()
      if(this.interval==null){
        this.interval = setTimeout(this.loadInTable, 1000, this.metricType);
      }else{
        clearTimeout(this.interval)
        this.interval = setTimeout(this.loadInTable, 1000, this.metricType);
      }
    },
    intervalSetup(){
      console.log("HELLO")
      if(this.interval==null){
        self.setInterval(function(){
          console.log("Trying")
          console.log(this)
        }, 1000)
      }else{
        clearInterval(this.interval)
        self.setInterval(function(){
          console.log("Trying")
          console.log(this)
        }, 1000)
      }
    }
  }
}
</script>
