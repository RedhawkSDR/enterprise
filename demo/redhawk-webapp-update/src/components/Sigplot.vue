<template>
<div>
	<h1>Hello Sigplot</h1>
	<div id="signalplot">

	</div>
</div>
</template>

<script>
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
	name: 'signalplot',
	created() {
		console.log("Made it")
		console.log(SigPlotVue)

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
