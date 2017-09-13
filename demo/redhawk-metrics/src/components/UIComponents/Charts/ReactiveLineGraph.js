//TODO Just make this a reusable component
import { Line, mixins} from 'vue-chartjs'
const { reactiveData } = mixins

//import BarChart from '../BaseCharts/Bar'
//import reactiveData from '../mixins/reactiveData'
export default Line.extend({
  mixins: [reactiveData],
  props: ['values', 'labels'],
  data () {
    return {
      samples: 60,
      speed: 250,
      labels: [],
      options: {
        responsive: true,
        maintainAspectRatio: false,
        legend: false,
        animation: {
          easing: 'linear'
        }
        /*scales: {
          yAxes: [
            {
              scaleLabel: {
                display: true,
                labelString: this.metricType
              }
            }
          ]
        }*/
      }
    }
  },
  created () {
    //this.labels.length = 60
    //this.labels.fill('')

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
    }
  }
})
