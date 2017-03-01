//Constants 
var plot = new sigplot.Plot(document.getElementById('signalplot'), {
    autol: 5,
    cmod: "L2",
    autohide_panbars: true,
    nogrid: true,
});

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
//SigPlot Related variables
var xdelta, ydelta, xstart, ystart, xunits, ydelta, yunits;

//WebSocket Related variables
var sigplotWS, audioWS, wsURL;
var audioService = new AudioService()


var pl = plot.overlay_array(null, {
            size: 1000,
            xdelta: xdelta,
            xunits: xunits,
            yunits: yunits,
            ydelta: ydelta,
            xstart: xstart
        });

/*
var pl = plot.overlay_array(null, {
    size: 1000,
    xdelta: 12500,
    xunits: 3,
    yunits: 26,
    xmax: 10000000,
    xstart: 1.5374980926513672E8
});
*/

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
		console.log("Calling Initialized Waveform "+launchedWFJson)
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

/*
 * This launches a waveform and updates the 
 * list of waveforms available
 */
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
		
		//Once you launch this I need to update the Launched Waveforms
		initializeLaunchedWaveformsList()
	})
	.catch(function(error){
		console.log(error)
	})
}

/*
 * This releases a waveform and updates the list of waveforms that are
 * availabel
 */
function releaseWaveform(waveformName){
	axios.delete(launchWaveformURL+"/"+waveformName)
	.then(function(response){
		console.log(response)
		
		//Once you launch this I need to update the Launched Waveforms
		initializeLaunchedWaveformsList()
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
			
			console.log("Waveforms List Should be up to date: ")
			
			//Reset Components and Ports
			rhComponents.options = []
			rhPorts.options = []
			
			this.$emit('close')
		},
		cancel: function(){
			this.$emit('close')
		}
	}
})

Vue.component('property-component',{
	template: '#property-template',
	methods:{
		updateProperty: function(){
			console.log("Update this prop: "+this.property.id+" with value "+this.property.value)
			var propertyUpdateURL = baseURI+"/applications/"+launchedWaveforms.selected[0].name+"/components/"+rhComponents.selected[0]+"/properties/"+this.property.id
			
			/*
			 * Run the put with the updated property
			 */
			myPut = axios.create({
				headers: {
					'Content-Type': 'application/json'
					}
			})
			myPut.put(propertyUpdateURL, JSON.stringify(this.property))
			.then(function(response){
				console.log(response)
			})
			.catch(function(error){
				console.log(error)
			})
		},
		view: function(){
			console.log("View Struct")
		}
	},
	props: ['property']
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

var componentProperties = new Vue({
	el: '#component-properties',
	data: {
		properties: [],
		componentSelected: ''
	}
})

var domainSetup = new Vue({
	el: '#domainConfig',
	data: {
		domainName: "REDHAWK_DEV",
		nameServer: "127.0.0.1:2809"
	},
	methods: {
		connect: function(){
			//Initialize Available Waveform List
			initializeAvailableWaveformsList()

			//Initialize Launched Waveform List
			initializeLaunchedWaveformsList()
		}
	}
})

var listenUp = new Vue({
	el: '#listenUp',
	data: {
		listen: "Listen"
	},
	methods: {
		listenToAudio: function(){
			if(this.listen == "Listen"){
				this.listen = "Mute"
				audioWS = new WebSocket(wsURL)
				
				audioWS.onopen = function(evt){
					console.log("Audio Socket Connected")
				}
				
				audioWS.onclose = function(){
					console.log("Shutdown Audio")
				}
				
				audioWS.onmessage = function(evt){
					if(typeof evt.data !== "string"){
						audioService.playAudio(evt.data)
					}else{
                        var sri = JSON.parse(evt.data);
                        audioService.setSampleRate(Math.round(1 / sri.xdelta));						
					}
				}
			}else{
				this.listen = "Listen"
				audioService.stopAudio();
				audioWS.close();
			}
		}
	}
})

var availableWF = new Vue({
	el : '#availableWaveforms',
	data: {
		selected: null,
		options: [ ],
		showLaunchModal: false,
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
			var propertiesURL = baseURI+"/applications/"+launchedWaveforms.selected[0].name+"/components/"+this.selected[0]+"/properties.json"
			componentProperties.componentSelected = this.selected[0]
			axios.get(propertiesURL)
			.then(function(response){
				console.log(response.data)
				componentProperties.properties = response.data.properties
			})
			.catch(function(response){
				console.log(response)
			})
			
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
			if(sigplotWS!=null){
				console.log("Closing old websocket")
				sigplotWS.close()
			}
			
			wsURL = "ws://localhost:8181/redhawk/localhost:2809/domains/REDHAWK_DEV/applications/"+launchedWaveforms.selected[0].name+"/components/"+rhComponents.selected[0]+"/ports/"+rhPorts.selected[0]
			console.log(wsURL)
			sigplotWS = new WebSocket(wsURL)

			sigplotWS.binaryType = "arraybuffer";

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
					console.log("NEW SRI")
					/*
					 * {"endOfStream":false,"streamId":"SigGen Stream","hversion":1,"xstart":0.0,"xdelta":2.0E-4,"xunits":1,"subsize":0,"ystart":0.0,"ydelta":0.0,
					 * "yunits":0,"mode":0,"blocking":false,"keywords":{},"tcmode":1,"tcstatus":1,"tfsec":0.7909940000000002,"toff":0.0,"twsec":1.488296658E9}
					 */
					var sri = JSON.parse(evt.data);
					xdelta = sri.xdelta
					ydelta = sri.ydelta
					xstart = sri.xstart
					ystart = sri.ystart
					xunits = sri.xunits
					yunits = sri.yunits
				}
			};
		}
	}
})