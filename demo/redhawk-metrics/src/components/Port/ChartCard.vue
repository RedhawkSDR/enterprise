<template>
  <div class="card">
    <!-- TODO: Clean up graph background to make more appealing -->
    <div class="card-header" data-background-color="black">
      <h4 style="text-align:center;">{{name}}</h4>
      <reactivelinegraph
      :values="values"
      :labels="labels"
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
  props: ['name'],
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
