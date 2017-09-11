<template>
  <div class="card">
    <canvas id="chart0" style="width:512px; height=320px;"></canvas>
  </div>
</template>

<script>
import Chart from 'chart.js';

var samples = 100;
var speed = 250;
var values = [];
var labels = [];
var charts = [];
var value = 0;

values.length = samples;
labels.length = samples;
values.fill(0);
labels.fill(0);

function initialize() {
  charts.push(new Chart(document.getElementById("chart0"), {
    type: 'line',
    data: {
      labels: labels,
      datasets: [{
        data: values,
        backgroundColor: 'rgba(255, 99, 132, 0.1)',
        borderColor: 'rgb(255, 99, 132)',
        borderWidth: 2,
        lineTension: 0.25,
        pointRadius: 0
      }]
    },
    options: {
      responsive: false,
      animation: {
        duration: speed * 1.5,
        easing: 'linear'
      },
      legend: false,
      scales: {
        xAxes: [{
          display: false
        }],
        yAxes: [{
          ticks: {
            max: 1,
            min: -1
          }
        }]
      }
    }
  }))
}


function advance() {
  value = Math.min(Math.max(value + (0.1 - Math.random() / 5), -1), 1);

  values.push(value);
  values.shift();
  charts.forEach(function(chart) { chart.update(); });

  setTimeout(function() {
    requestAnimationFrame(advance);
  }, speed);
}

export default {
  name: 'portchart',
  mounted() {
      initialize()
      advance()
  }
}
</script>
