<template>
<div>
  <chart/>
  <div>
    <div class="card">
      <div class="card-header" data-background-color="green">
        <h4 class="title">Port: {{ name }}</h4>
        <h5 class="title">Component: {{ componentName }}</h5>
        <h6 class="title">Application: {{ applicationName }}</h6>
        <p class="category">Statistics for port {{ name }}</p>
      </div>
      <div class="card-content">
        <porttable></porttable>
      </div>
    </div>
  </div>
</div>
</template>

<script>
import Table from './PortStatisticsTable.vue'
import Chart from './Chart.vue'

export default {
  name: 'portstatisticsview',
  components: {
    'porttable': Table,
    'chart' : Chart
  },
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
    }
  }
}
</script>
