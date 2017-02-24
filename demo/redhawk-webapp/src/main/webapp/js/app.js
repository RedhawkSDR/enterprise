//Create plot
//Constants 
var plot = new sigplot.Plot(document.getElementById('signalplot'), {});
var baseURI = "http://127.0.0.1:8181/cxf/redhawk/localhost:2809/domains/REDHAWK_DEV"
var waveformsURL = baseURI+"/waveforms.json"
var launchWaveformURL = baseURI+"/applications"
var temp;

//Functions
function initializeAvailableWaveformsList(){
	axios.get(waveformsURL)
	.then(function(response){
		temp = response.data.waveforms.waveforms
		console.log(temp)
		availableWF.options = temp
	})
	.catch(function(error){
		console.log(error)
	})
}

var firstMessage = true

var pl = plot.overlay_array(null, {
    size: 1000,
    xdelta: 12500,
    xunits: 3,
    yunits: 26,
    xmax: 10000000,
    xstart: 1.5374980926513672E8
});

var availableWF = new Vue({
	el : '#availableWaveforms',
	data: {
		selected: null,
		options: [ ]
	},
	created : function(){
		initializeAvailableWaveformsList()
	},
	methods: {
		launch: function(){
			console.log("Launching Waveform")
			var appToLaunch = new Object()
			appToLaunch.id = 'anId'
			appToLaunch.sadLocation = this.selected
			appToLaunch.name = 'aName'
			console.log(JSON.stringify(appToLaunch))
			myPut = axios.create({
				headers: {'Content-Type': 'application/json'}
			})
			myPut.put(launchWaveformURL+"/test", JSON.stringify(appToLaunch))
		}
	}
	/*data: {
		selected: null,
		options: [
		          { name: 'One', value: 'A' },
		          { name: 'Two', value: 'B' },
		          { name: 'Three', value: 'C' }
		          ]
		  }
		  */
})



displayData = function(){
	console.log("Hitting button")
	var ws = new WebSocket("ws://localhost:8181/redhawk/localhost:2809/domains/REDHAWK_DEV/applications/wf-integration-test.*/components/SigGen.*/ports/dataFloat_out")
	
	ws.binaryType = "arraybuffer";

	ws.onopen = function(evt) {
	    console.log("Connected.");
	};

	ws.onclose = function() {
	    console.log("Shutdown.");
	};

	ws.onmessage = function(evt) {
		if(firstMessage){
	    	console.log("Received first message")
	    	var json = JSON.parse(evt.data)
	    	console.log(json)
	    	console.log(json['xdelta'])
	    	firstMessage = false
	    	console.log(plot)
	    	var overlaySettings = new Object()
	    	overlaySettings.size = 1000
	    	overlaySettings.xdelta = json.xdelta
	    	overlaySettings.xunits = json.xunits
	    	overlaySettings.yunits = json.yunits
	    	overlaySettings.xmax = 10000000
	    	overlaySettings.xstart = json.xstart
	    	console.log(overlaySettings)
	    	//TODO: Why doesn't this work
	    	//pl = plot.overlay_array(null, overlaySettings);
	    }else{
	    	console.log("Received follow on messages")
	        plot.reload(pl, evt.data);
	    }
	};
}