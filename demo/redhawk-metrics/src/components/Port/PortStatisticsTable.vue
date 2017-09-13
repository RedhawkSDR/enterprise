<template>
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
</template>

<script>
export default {
  name: 'portstatisticstable',
  computed: {
    statistics(){
      return this.$store.getters.portStatistics
    },
    interval(){
      return this.$store.getters.interval
    }
  },
  data(){
    return {
      intervalTime : 1000,
    }
  },
  mounted(){
    this.updateStats()
  },
  destroyed(){
    clearInterval(this.$store.state.interval)
  },
  methods: {
    updateStats(){
      self = this
      if(this.interval==null){
        this.$store.state.interval = setInterval(function(){
          self.$store.dispatch("updatePortStats")
        }, self.intervalTime);
      }else{
        clearInterval(this.$store.state.interval)
        this.$store.state.interval = setInterval(function(){
          self.$store.dispatch("updatePortStats")
        }, self.intervalTime);
      }
    }
  }
}
</script>
