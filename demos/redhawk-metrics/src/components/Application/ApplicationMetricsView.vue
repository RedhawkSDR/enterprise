<template>
  <div>
    <chartcard :name="metricType" :metricType="metricId"></chartcard>
    <div class="card">
      <div class="card-header" data-background-color="green">
        <h4 class="title">{{ appName }} </h4>
        <p class="category">Metrics for {{ appName }}</p>
      </div>
      <div class="card-content">
        <md-input-container>
          <label for="metricId">Metric Id</label>
          <md-select
            name="metricId" id="metricId" v-model="metricId">
            <!--<md-option value="0">Application Utilization</md-option>-->
          <md-option
            v-for="(appmetric, index) in metricIndex"
            v-bind:key="appmetric"
            :value="index"
            >
            {{ appmetric }}
          </md-option>
        </md-select>
      </md-input-container>
      <apptable :metricId="metricId"></apptable>
    </div>
  </div>
</div>
</template>

<script>
import Table from './ApplicationTable.vue'
import ChartCard from './ChartCard.vue'

export default {
  name: 'appmetricsview',
  data() {
    return {
      showApp: false,
      metricId: 0,
      metricType: 'application utization'
    }
  },
  components: {
    'apptable' : Table,
    'chartcard' : ChartCard
  },
  created(){
    //Get application parameters
    console.log("Route Params")
    var appName = this.$route.params.appName

    //Use name to fill out view
    this.$store.dispatch('chooseApplication', appName)
  },
  computed: {
    appName(){
      return this.$store.getters.appName
    },
    appMetricsURL(){
      return this.$store.getters.appMetricsURL
    },
    appMetrics(){
      return this.$store.getters.appMetrics
    },
    metricIndex(){
      return this.$store.getters.appMetricsIndex
    }
  },
  watch: {
    metricId(){
      this.metricType = this.metricIndex[this.metricId]
    }
  }
}
</script>
