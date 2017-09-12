<template>
<div class="card">
  <div class="card-header">
    <h4 style="text-align:center;">{{metricType}}</h4>    
    <reactivelinegraph
    :values="values"
    :height="200"></reactivelinegraph>
  </div>
  <div class="card-content">
    <md-input-container>
      <label for="graphmetric">Graph</label>
      <md-select name="graphmetric" id="graphmetric" v-model="graphmetric">
        <md-option v-if="metricType=='sys_limits'" value="current_open_files">Current Open Files</md-option>
        <md-option v-if="metricType=='sys_limits'" value="current_threads">Current Threads</md-option>
        <md-option v-if="metricType=='utilization'" value="component_load">Component Load</md-option>
        <md-option v-if="metricType=='utilization'" value="subscribed">Subscribed</md-option>
        <md-option v-if="metricType=='utilization'" value="system_load">System Load</md-option>
      </md-select>
    </md-input-container>
  </div>
</div>
</template>

<script>
import ReactiveLineGraph from '../UIComponents/Charts/ReactiveLineGraph.js'

export default {
  name: 'gppchartcard',
  props: ['metricType'],
  components: {
    'reactivelinegraph' : ReactiveLineGraph
  },
  computed: {
    chartvalue(){
      if(Object.keys(this.$store.getters.gppMetrics).length == 0){
        return 0;
      }else{
        return this.$store.getters.gppMetrics[this.metricType][this.graphmetric]
      }
    }
  },
  created(){
    if(this.metricType=='utilization'){
      this.graphmetric = 'component_load'
    }else if(this.metricType=='sys_limits'){
      this.graphmetric = 'current_open_files'
    }
  },
  data(){
    return {
      graphmetric: 'current_open_files',
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
    },
    metricType(){
      this.values.fill(0)
      this.values.push(this.chartvalue)

      //Setting defaults based on metric type
      if(this.metricType=='utilization'){
        this.graphmetric = 'component_load'
      }else if(this.metricType=='sys_limits'){
        this.graphmetric = 'current_open_files'
      }
    }
  }
}
</script>
