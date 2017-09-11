<template>
  <div class="card">
    <!-- Put graph here -->
    <div class="card-header" data-background-color="black">
      <chartexample :height="50"></chartexample>
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

export default {
  name: 'portchart',
  components: {
    'chartexample': ChartExample
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
