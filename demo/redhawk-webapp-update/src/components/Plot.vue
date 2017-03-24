<template>
<div id="plot">
</div>
</template>

<script>
var plot, pl, sigplotWS

export default {
	name: 'plot',
	data(){
		return {
			xdelta: null,
			ydelta: null,
			xstart: null,
			ystart: null,
			xunits: null,
			ydelta: null,
			yunits: null,
		}
	},
	computed: {
		wsURL(){
			return this.$store.getters.wsURL
		}
	},
	watch: {
		wsURL: function(){
			if(this.wsURL!=null){
				console.log('Do Websocket stuff')
				sigplotWS = new WebSocket(this.wsURL)
				sigplotWS.binaryType = 'arraybuffer'
				sigplotWS.onopen = function(evt) {
					console.log("Connected.");
				};

				sigplotWS.onclose = function() {
					console.log("Shutdown.");
				};

				var self = this

				sigplotWS.onmessage = function(evt) {
					if(typeof evt.data !== "string"){
						//console.log('Received Data')
						plot.reload(pl, evt.data);
					}else{
						console.log("NEW SRI")
						/*
						* {"endOfStream":false,"streamId":"SigGen Stream","hversion":1,"xstart":0.0,"xdelta":2.0E-4,"xunits":1,"subsize":0,"ystart":0.0,"ydelta":0.0,
						* "yunits":0,"mode":0,"blocking":false,"keywords":{},"tcmode":1,"tcstatus":1,"tfsec":0.7909940000000002,"toff":0.0,"twsec":1.488296658E9}
						*/
						var sri = JSON.parse(evt.data);
						self.xdelta = sri.xdelta
						self.ydelta = sri.ydelta
						self.xstart = sri.xstart
						self.ystart = sri.ystart
						self.xunits = sri.xunits
						self.yunits = sri.yunits

						pl = plot.overlay_array({
							size: 1000,
							xdelta: self.xdelta,
							xunits: self.xunits,
							yunits: self.yunits,
							ydelta: self.ydelta,
							xstart: self.xstart
						});
					}
				};
			}else{
				console.log('No url so no websocket')
			}
		}
	},
	mounted() {
		console.log("Made it")
		console.log(sigplot)
		var divToPass = document.getElementById('plot')
    console.log('Passing in div from create: '+divToPass)
    //console.log(vm)
    plot = new sigplot.Plot(divToPass, {
        autol: 5,
        cmod: "L2",
        autohide_panbars: true,
        nogrid: true,
    })

		this.$store.hello = "World"
		//console.log(smoothieChart)
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

<style>
#plot { height: 400px; width: 600px; }
</style>
