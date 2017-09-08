<template>
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
              :value="appmetric"
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
</template>

<script>
export default {
  name: 'appmetricsview',
  data() {
    return {
      interval: null,
      showApp: false,
      metricType: 'application utilization'
    }
  },
  mounted(){
    //Get application parameters
    console.log("Route Params")
    var appName = this.$route.params.appName

    //Use name to fill out view
    this.$store.dispatch('chooseApplication', appName)
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
