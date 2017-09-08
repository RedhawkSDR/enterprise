<template>
    <div class="card">
      <div class="card-header" data-background-color="green">
        <h4 class="title">Port: {{ name }}</h4>
        <h5 class="title">Component: {{ componentName }}</h5>
        <h6 class="title">Application: {{ applicationName }}</h6>
        <p class="category">Statistics for port {{ name }}</p>
      </div>
      <div class="card-content">
        <md-table>
          <md-table-header>
            <md-table-row>
              <md-table-head>Metric Name</md-table-head>
              <md-table-head md-numeric>Value</md-table-head>
            </md-table-row>
          </md-table-header>
          <!-- Finite number of metrics for now just hard code no need to loop -->
          <md-table-body>
            <md-table-row>
              <md-table-cell>connectionId</md-table-cell>
              <md-table-cell>{{ this.statistics.connectionId }}</md-table-cell>
            </md-table-row>
            <md-table-row>
              <md-table-cell>Elements Per Second</md-table-cell>
              <md-table-cell>{{ this.statistics.elementsPerSecond }}</md-table-cell>
            </md-table-row>
            <md-table-row>
              <md-table-cell>Bits Per Second</md-table-cell>
              <md-table-cell>{{ this.statistics.bitsPerSecond }}</md-table-cell>
            </md-table-row>
            <md-table-row>
              <md-table-cell>Calls Per Second</md-table-cell>
              <md-table-cell>{{ this.statistics.callsPerSecond }}</md-table-cell>
            </md-table-row>
            <md-table-row>
              <md-table-cell>Stream Ids</md-table-cell>
              <md-table-cell>{{ this.statistics.streamIDs }}</md-table-cell>
            </md-table-row>
            <md-table-row>
              <md-table-cell>Time Since Last Call</md-table-cell>
              <md-table-cell>{{ this.statistics.timeSinceLastCall }}</md-table-cell>
            </md-table-row>
            <md-table-row>
              <md-table-cell>Average Queue Depth</md-table-cell>
              <md-table-cell>{{ this.statistics.averageQueueDepth }}</md-table-cell>
            </md-table-row>
            <md-table-row>
              <md-table-cell>Keywords</md-table-cell>
              <md-table-cell>{{ this.statistics.keywords }}</md-table-cell>
            </md-table-row>
          </md-table-body>
        </md-table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'portstatisticsview',
  mounted(){
    console.log("Route Params")
    console.log(this.$route.params)

    this.$store.dispatch('choosePort', this.$route.params)
  },
  computed: {
    name(){
      return this.$store.getters.portName
    },
    componentName(){
      return this.$store.getters.portComponentName
    },
    applicationName(){
      return this.$store.getters.portApplicationName
    },
    statistics(){
      return this.$store.getters.portStatistics
    }
  }
}
</script>
