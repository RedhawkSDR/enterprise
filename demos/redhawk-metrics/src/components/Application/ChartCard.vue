<template>
<div class="card">
  <div class="card-header">
    <h4 style="text-align:center;">{{name}}</h4>
    <reactivelinegraph
    :values="values"
    :labels="labels"
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
  props: ['metricType', 'name'],
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
      values: [],
      labels: []
    }
  },
  created(){
    this.labels.length = 60
    this.values.fill('')

    this.values.length = 60
    this.values.fill(0)
  },
  watch: {
    chartvalue(){
      this.values.push(this.chartvalue)
      this.values.shift()

      this.labels.push(this.getLabel())
      this.labels.shift()
    },
    graphmetric(){
      this.values.fill(0)
      this.values.push(this.chartvalue)

      this.labels.fill('')
      this.labels.push(this.getLabel())
      this.labels.shift()
    },
    metricType(){
      this.values.fill(0)
      this.values.push(this.chartvalue)

      this.labels.fill('')
      this.labels.push(this.getLabel())
      this.labels.shift()
    }
  },
  methods: {
    getLabel(){
      var t = new Date()
      var label;
      if(t.getSeconds()<10){
        label = t.getMinutes()+':0'+t.getSeconds()
      }else{
        label = t.getMinutes()+':'+t.getSeconds()
      }
      return label
    }
  }
}
</script>
