<template>
<div>
<h1>Plot Goes Here</h1>
<!--<canvas id="mycanvas" width="400" height="300"></canvas>-->
</div>
</template>

<script>
//import smoothie from 'smoothie'

//var smoothieChart = new smoothie.SmoothieChart()
//require('../libs/bluefile-minimized.js')
//require('../libs/sigplot.plugins-minimized.js')
//require('../libs/sigplot-minimized.js')


/*var plot = new sigplot.Plot(document.getElementById('signalplot'), {
    autol: 5,
    cmod: "L2",
    autohide_panbars: true,
    nogrid: true,
});
*/
//import {SigPlotVue} from '../libs/sigplot-wrapper.js'



export default {
	name: 'plot',
	created() {
		console.log("Made it")
		//console.log(smoothieChart)
		console.log(document.getElementById("mycanvas"))
		console.log(window.Audio)
		//smoothieChart.streamTo(document.getElementById("mycanvas"));
		/*SigPlotVue.Plot(document.getElementById('signalplot'), {
		    autol: 5,
		    cmod: "L2",
		    autohide_panbars: true,
		    nogrid: true,
		});
		*/
	}
}
</script>
