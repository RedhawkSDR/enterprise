<template>
<div class="card">
  <div class="card-header">
    <reactivelinegraph
    :values="values"
    :height="200"></reactivelinegraph>
  </div>
  <div class="card-content">
    <!--TODO: Make a reusable component -->
    <md-input-container>
      <label for="graphmetric">Graph</label>
      <md-select name="graphmetric" id="graphmetric" v-model="graphmetric">
        <md-option value="cores">Cores</md-option>
        <md-option value="memory">Memory</md-option>
        <md-option value="threads">Threads</md-option>
      </md-select>
    </md-input-container>
  </div>
</div>
</template>

<script>
import ReactiveLineGraph from '../UIComponents/Charts/ReactiveLineGraph.js'

export default {
  name: 'appchartcard',
  props: ['metricType'],
  components: {
    'reactivelinegraph' : ReactiveLineGraph
  },
  computed: {
    chartvalue(){
      if(Object.keys(this.$store.getters.appMetrics).length == 0){
        return 0;
      }else{
        return this.$store.getters.appMetrics[this.metricType][this.graphmetric]
      }
    }
  },
  data(){
    return {
      graphmetric: 'cores',
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
      this.values.push(this.chartvalue)
    }
  }
}
</script>
