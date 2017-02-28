//Constants 
var plot = new sigplot.Plot(document.getElementById('signalplot'), {});
var baseURI = "http://127.0.0.1:8181/cxf/redhawk/localhost:2809/domains/REDHAWK_DEV"
var waveformsURL = baseURI+"/waveforms.json"
var launchedWaveformsURL = baseURI+"/applications.json"
var launchWaveformURL = baseURI+"/applications"
var componentsURL, portsURL;
var componentJson, portsJson;
var firstMessage = true
var launchData = {
		waveformName : null
}
var sandbox;

var pl = plot.overlay_array(null, {
            size: 1000,
            xdelta: 12500,
            xunits: 3,
            yunits: 26,
            xmax: 10000000,
            xstart: 1.5374980926513672E8
        });


//Functions
function initializeAvailableWaveformsList(){
	axios.get(waveformsURL)
	.then(function(response){
		var availableWaveForms = response.data.domains
		//console.log(availableWaveForms)
		availableWF.options = availableWaveForms
	})
	.catch(function(error){
		console.log(error)
	})
}

function initializeLaunchedWaveformsList(){
	var launchedWFJson; 
	axios.get(launchedWaveformsURL)
	.then(function(response){
		//TODO: Clean way to handle just one.
		var launchedWFJson = response.data.applications
		console.log(launchedWFJson)
		launchedWaveforms.options = launchedWFJson		
	})
	.catch(function(error){
		console.log(error)
	})
}

function getComponentsForWaveform(){
	componentsURL = baseURI+"/applications/"+launchedWaveforms.selected[0].name+"/components.json"
	console.log(componentsURL)
	axios.get(componentsURL)
	.then(function(response){
		componentJson = response.data.components
		//console.log(componentJson)
		rhComponents.options = componentJson
	})
	.catch(function(error){
		console.log(error)
	})	
}

function getComponentPorts(){
	portsURL = baseURI+"/applications/"+launchedWaveforms.selected[0].name+"/components/"+rhComponents.selected[0]+"/ports.json"
	console.log(portsURL)
	axios.get(portsURL)
	.then(function(response){
		portsJson = response.data.ports
		//console.log(portsJson)
		rhPorts.options = portsJson
	})
	.catch(function(error){
		console.log(error)
	})
}

function launchWaveform(waveformName, sadLocation){
	var appToLaunch = new Object()
	appToLaunch.id = waveformName 
	appToLaunch.sadLocation = sadLocation
	appToLaunch.name = waveformName
	console.log(JSON.stringify(appToLaunch))
	myPut = axios.create({
		headers: {
			'Content-Type': 'application/json',
			'mimeType':'text/html'	
			}
	})
	myPut.put(launchWaveformURL+"/"+waveformName, JSON.stringify(appToLaunch))
	.then(function(response){
		console.log(response)
	})
	.catch(function(error){
		console.log(error)
	})
}

function releaseWaveform(waveformName){
	axios.delete(launchWaveformURL+"/"+waveformName)
	.then(function(response){
		console.log(response)
	})
	.catch(function(error){
		console.log(error)
	})
	
}
//End Functions

//Define Components
Vue.component('launch-modal',{
	template: '#launch-modal-template',
	data : function(){
		return launchData
	},
	methods: {
		updateLaunchedWaveforms: function(){
			var launchedWFJson; 
			axios.get(launchedWaveformsURL)
			.then(function(response){
				//TODO: Clean way to handle just one.
				launchedWFJson = response.data.applications
				console.log(launchedWFJson)
				launchedWaveforms.options = launchedWFJson		
			})
			.catch(function(error){
				console.log(error)
			})
		},
		cancel: function(){
			this.$emit('close')
		},
		finish: function(){
			console.log("Launch Waveform "+this.waveformName)
			launchWaveform(this.waveformName, availableWF.selected.sadLocation)
			
			console.log("Launched Waveform")
			this.updateLaunchedWaveforms()
			console.log("Finished Initialization")

			//Emit a close event on exit
			this.$emit('close')
		}
	},
	computed: {
		identifier: function(){
			return availableWF.selected.id
		},
		name: function(){
			return availableWF.selected.name
		},
		sadLocation: function(){
			return availableWF.selected.sadLocation
		}
	}
})

Vue.component('waveform-control-modal', {
	template: '#waveform-control-modal-template',
	methods: {
		start: function(){
			this.$emit('close')
		},
		stop: function(){
			this.$emit('close')
		},
		release: function(){
			console.log(launchedWaveforms.selected.name)
			releaseWaveform(launchedWaveforms.selected[0].name)
			
			initializeLaunchedWaveformsList()
			console.log("Waveforms List Should be up to date: ")
			this.$emit('close')
		},
		cancel: function(){
			this.$emit('close')
		}
	}
})
//End Components 

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
		options: [ ],
		showLaunchModal: false,
	},
	created : function(){
		initializeAvailableWaveformsList()
	},
	computed: {
		disabled : function(){
			if(this.selected==null){
				return true
			}else{
				return false;
			}
		}
	}
})

var launchedWaveforms = new Vue({
	el : '#launchedWaveforms',
	data: {
		selected: [],
		options: [],
		showWaveformController: false
	},
	created: function(){
		initializeLaunchedWaveformsList()
	},
	methods : {
		fillComponents: function(){
			console.log("Hello Click")
			getComponentsForWaveform()
			//Resert ports
			rhPorts.options = []
		},
		controlWaveform: function(){
			console.log("Double Click")
			this.showWaveformContoller = true
		}
	}
})

var rhComponents = new Vue({
	el: '#components',
	data: {
		selected: [],
		options: []
	},
	methods : {
		fillPorts: function(){
			getComponentPorts();
		}
	}
})

var rhPorts = new Vue({
	el: '#ports',
	data: {
		selected: [],
		options: []
	},
	methods: {
		graphData: function(){
			console.log("Graph Data Plz")
			var wsURL = "ws://localhost:8181/redhawk/localhost:2809/domains/REDHAWK_DEV/applications/"+launchedWaveforms.selected[0].name+"/components/"+rhComponents.selected[0]+"/ports/"+rhPorts.selected[0]
			console.log(wsURL)
			var ws = new WebSocket(wsURL)

			ws.binaryType = "arraybuffer";

			ws.onopen = function(evt) {
	    			console.log("Connected.");
			};

			ws.onclose = function() {
				console.log("Shutdown.");
			};

			ws.onmessage = function(evt) {	
	       	 		plot.reload(pl, evt.data);			
			};
		}
	}
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
