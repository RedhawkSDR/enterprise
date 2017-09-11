<template>
  <div class="card">
    <!-- Put graph here -->
    <div class="card-header" data-background-color="black">
      <!--<chartexample :height="50"></chartexample>-->
      <!--<portstatchart></portstatchart>-->
      <portlinegraph
      :graphmetric="graphmetric"
      :chartvalue="chartvalue"
      :height="200"></portlinegraph>
    </div>
    <div class="card-content">
      <md-input-container>
        <label for="graphmetric">Graph</label>
        <md-select name="graphmetric" id="graphmetric" v-model="graphmetric">
          <md-option value="elementsPerSecond">Elements Per Second</md-option>
          <md-option value="bitsPerSecond">Bits Per Second</md-option>
          <md-option value="callsPerSecond">Calls Per Second</md-option>
        </md-select>
      </md-input-container>
    </div>
  </div>
</template>

<script>
import ChartExample from './VueChartExample.vue'
import PortStatisticsChart from './PortStatisticsChart'
import PortLineGraph from './PortLineGraph.js'

export default {
  name: 'portchart',
  components: {
    'chartexample': ChartExample,
    'portstatchart' : PortStatisticsChart,
    'portlinegraph' : PortLineGraph
  },
  computed: {
    chartvalue(){
      return this.$store.getters.portStatistics[this.graphmetric]
    }
  },
  data(){
    return {
      graphmetric : 'elementsPerSecond',
      values: []
    }
  },
  watch: {
    chartvalue(){
      this.values.push(this.chartvalue)
      if(this.values.length>=60)
        this.values.shift()
    }
  }
}
</script>

<style>
.Chart {
  padding: 20px;
  box-shadow: 0px 0px 20px 2px rgba(0, 0, 0, .4);
  border-radius: 20px;
  margin: 50px 0;
}
</style>
