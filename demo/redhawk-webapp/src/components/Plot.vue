<template>
<div>
	<h1 v-if="portName!=null">{{ componentName }}::{{ portName }}</h1>
	<div id="plot">
	</div>
	<md-input-container>
    <label for="plotType">Plot Type</label>
    <md-select name="plotType" id="plotType" v-model="plotType">
      <md-option value="time">Time</md-option>
      <md-option value="raster">Raster</md-option>
    </md-select>
  </md-input-container>
	<!--
	<md-button>
		Update Plot
	</md-button>
	-->
</div>
</template>

<script>
var plot, pl;
var sigplotWS = null

function plotTimeData(wsURL){
	//Check to see if websocket already exists
	if(sigplotWS!=null){
			//Close open websocket
			sigplotWS.close()
			plot = new sigplot.Plot(document.getElementById('plot'), {
					autol: 5,
					cmode: "L2",
					format: 'SF',
					autohide_panbars: true,
					nogrid: true,
			})
	}

	sigplotWS = new WebSocket(wsURL)
	sigplotWS.binaryType = 'arraybuffer'
	sigplotWS.onopen = function(evt) {
		console.log("Connected.");
	};

	sigplotWS.onclose = function() {
		console.log("Shutdown.");
	};

	sigplotWS.onmessage = function(evt) {
		if(typeof evt.data !== "string"){
			plot.reload(pl, evt.data);
		}else{
			/*
			* {"endOfStream":false,"streamId":"SigGen Stream","hversion":1,"xstart":0.0,"xdelta":2.0E-4,"xunits":1,"subsize":0,"ystart":0.0,"ydelta":0.0,
			* "yunits":0,"mode":0,"blocking":false,"keywords":{},"tcmode":1,"tcstatus":1,"tfsec":0.7909940000000002,"toff":0.0,"twsec":1.488296658E9}
			*/
			var sri = JSON.parse(evt.data);

			pl = plot.overlay_array(null, {
				format: 'SF',
				cmode: 'L2',
				xdelta: sri.xdelta,
				xunits: sri.xunits				
			})
		}
	}
}

function plotRasterData(wsURL){
	//Check to see if websocket already exists
	if(sigplotWS!=null){
			//Close open websocket
			sigplotWS.close()
			plot = new sigplot.Plot(document.getElementById('plot'), {
					all: true,
					expand: true,
					autol: 100,
					autohide_panbars: true,
					pan: false,
					show_y_axis: false,
					cmode: 'L2'
				});
	}



	sigplotWS = new WebSocket(wsURL)
	sigplotWS.binaryType = 'arraybuffer'
	sigplotWS.onopen = function(evt) {
		console.log("Connected.");
	};

	sigplotWS.onclose = function() {
		console.log("Shutdown.");
	};

	sigplotWS.onmessage = function(evt) {
		if(typeof evt.data !== "string"){
			plot.push(pl, evt.data);
		}else{
			/*
			* {"endOfStream":false,"streamId":"SigGen Stream","hversion":1,"xstart":0.0,"xdelta":2.0E-4,"xunits":1,"subsize":0,"ystart":0.0,"ydelta":0.0,
			* "yunits":0,"mode":0,"blocking":false,"keywords":{},"tcmode":1,"tcstatus":1,"tfsec":0.7909940000000002,"toff":0.0,"twsec":1.488296658E9}
			*/
			var sri = JSON.parse(evt.data);

			pl = plot.overlay_pipe({
				type: 2000,
				subsize: 1024,
				xdelta: sri.xdelta,
				xunits: sri.xunits
			})
			console.log(sri.subsize)
		}
	}
}

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
			componentName: null,
			plotType: 'time'
		}
	},
	computed: {
		wsURL(){
			return this.$store.getters.wsURL
		},
		portName(){
			return this.$store.getters.portToDisplayName
		},
		propertyUpdate(){
			return this.$store.getters.propertyUpdate
		},
		plotData(){
			if(this.plotType=='time'){
				console.log('Switching to time')
				plotTimeData(this.wsURL)
			}else if(this.plotType=='raster'){
				console.log('Switching to Raster')
				plotRasterData(this.wsURL)
			}else{
				console.log("Plot Type is "+this.plotType)
			}
		}
	},
	watch: {
		propertyUpdate: function(){
			//A property has been updated if plotting is occuring refresh plot
			console.log('Property was updated')
			if(this.wsURL!=null){
				this.componentName = this.$store.state.portsComponentName//TODO:This is not intuitive clean up logic
				plotData()
			}
		},
		wsURL: function(){
			if(this.wsURL!=null){
				this.componentName = this.$store.state.portsComponentName//TODO:This is not intuitive clean up logic
				console.log('Do Websocket stuff')
				plotData()
			}else{
				console.log('No url so no websocket')

				//Reset plot to initial state.
				plot = new sigplot.Plot(document.getElementById('plot'), {
						autol: 5,
						cmode: "L2",
						format: 'SF',
						autohide_panbars: true,
						nogrid: true,
				})
			}
		},
		plotType: function(){
			console.log('Plot Type Changed')
			this.plotData()
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
        cmode: "L2",
        autohide_panbars: true,
        nogrid: true,
    })
	}
}
</script>

<style>
#plot { height: 400px; width: 600px; }
</style>
