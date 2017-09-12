<template>
  <div class="card">
    <!-- TODO: Clean up graph background to make more appealing -->
    <div class="card-header" data-background-color="black">
      <reactivelinegraph
      :values="values"
      :height="200"></reactivelinegraph>
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
import ReactiveLineGraph from '../UIComponents/Charts/ReactiveLineGraph.js'

export default {
  name: 'portchart',
  components: {
    'reactivelinegraph' : ReactiveLineGraph
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
  created(){
    this.values.length = 60
    this.values.fill(0)
  },
  watch: {
    chartvalue(){
      this.values.push(this.chartvalue)
      if(this.values.length>=60)
        this.values.shift()
    },
    graphmetric(){
      this.values.fill(0)
    }
  }
}
</script>
