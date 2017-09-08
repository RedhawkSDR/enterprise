<template>
  <md-table>
    <md-table-header>
      <md-table-row>
        <md-table-head>Metric Name</md-table-head>
        <md-table-head md-numeric>Value</md-table-head>
      </md-table-row>
    </md-table-header>
    <md-table-body>
      <md-table-row
        v-for="(metricKey, index) in metricKeys" :key="index">
        <md-table-cell>{{ metricKey }}</md-table-cell>
        <!-- TODO: Do some logic here to decide if number if so make the column have a graph
        button
        -->
        <md-table-cell>{{ metrics[metricKey]}}</md-table-cell>
      </md-table-row>
    </md-table-body>
  </md-table>
</template>

<script>
export default {
  name: 'gppmetricstable',
  props: {
    metricType: '',
  },
  computed: {
    metrics(){
      return this.$store.getters.gppMetrics[this.metricType]
    },
    metricKeys(){
      return Object.keys(this.$store.getters.gppMetrics[this.metricType])
    }
  }
}
</script>
