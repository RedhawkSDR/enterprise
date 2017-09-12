import { Line, mixins} from 'vue-chartjs'
const { reactiveData } = mixins

//import BarChart from '../BaseCharts/Bar'
//import reactiveData from '../mixins/reactiveData'
export default Line.extend({
  mixins: [reactiveData],
  props: ['graphmetric', 'chartvalue'],
  data () {
    return {
      samples: 60,
      speed: 250,
      values: [],
      labels: [],
      value: 0,
      options: {
        responsive: true,
        maintainAspectRatio: false,
        legend: false
      }
    }
  },
  created () {
    this.values.length = this.samples
    this.labels.length = this.samples
    this.values.fill(0)
    this.labels.fill('')
    this.fillData()
  },

  mounted () {
    this.renderChart(this.chartData, this.options)

    setInterval(() => {
      this.fillData()
    }, 1000)
  },

  methods: {
    fillData(){
      this.values.push(this.chartvalue)
      if(this.values.length>=60)
        this.values.shift()

      this.chartData = {
        labels: this.labels,
        datasets: [
          {
            data: this.values,
            backgroundColor: 'rgba(255, 99, 132, 0.1)',
            borderColor: 'rgb(255, 99, 132)',
            borderWidth: 2,
            lineTension: 0.25,
            pointRadius: 0             
          }
        ]
      }
    },
    getRandomInt () {
      return this.$store.getters.portStatistics[this.graphmetric]
    }
  }
})
