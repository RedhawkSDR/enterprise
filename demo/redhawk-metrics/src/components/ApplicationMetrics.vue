<template>
<!--TODO: Make this it's own component -->
<div class="content">
  <div v-if="showApp" class="container-fluid">
    <div class="row">
      <div class="card">
        <div class="card-header" data-background-color="green">
          <h4 class="title">{{ appName }} </h4>
          <p class="category">Metrics for {{ appName }}</p>
        </div>
        <div class="card-content">
          <md-input-container>
            <label for="metricType">Metric Type</label>
            <md-select name="metricType" id="metricType" v-model="metricType">
              <md-option
                v-for="(appmetric, index) in appMetricsKeys"
                v-bind:key="appmetric"
                v-bind:appvalue="appmetric"
                value="appvalue"
                >
                {{ appmetric }}
              </md-option>
            </md-select>
          </md-input-container>
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
        </div>
      </div>
    </div>
  </div>
  <appmetricslist v-else></appmetricslist>
</div>
</template>

<style>

</style>

<script>
import ApplicationMetricsList from './ApplicationMetricsList'

export default {
  name: 'sidebar',
  data() {
    return {
      interval: null,
      metricType: null,
      showApp: false,
      metricType: 'application utilization'
    }
  },
  mounted(){
    this.$store.dispatch("getAvailableMetrics", 'application')
  },
  components: {
    'appmetricslist' : ApplicationMetricsList
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
  watch: {
    appName(){
      console.log("Show App")
      this.showApp = true
      console.log(this.showApp)
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
