<template>
	<md-layout>
		<md-layout md-column md-flex="25">
			<md-toolbar>
				<h1 class="md-title">{{ domainConfig.name }} :: {{ domainConfig.domainName}}</h1>
				<md-button @click.native="refreshDomain()" class="md-icon-button md-raised md-warn" id="refreshButton">
					<md-icon>refresh</md-icon>
				</md-button>
			</md-toolbar>
			<md-list>
				<md-list-item>
					<span>Waveforms [ {{ numberOfWaveforms }} ]
					</span>
					<md-list-expand>
						<md-list-item
						class="md-inset"
						v-for="(waveform,index) in availableWaveforms"
						v-bind:key="waveform"
						@click.native="showWaveformLauncher(waveform)"
						>
						{{ waveform.name }}
						<md-divider></md-divider>
					</md-list-item>
				</md-list-expand>
			</md-list-item>
		</md-list>
		<rhapplications></rhapplications>
		<rhdevicemanagers></rhdevicemanagers>
		<eventchannelmgr></eventchannelmgr>
	</md-layout>
	<md-layout md-flex>
		<redhawkapplication v-if="showApplication"></redhawkapplication>
		<rhdevicemanager v-if="showDeviceManager"></rhdevicemanager>
		<eventchannel v-if="showEventChannel"></eventchannel>
	</md-layout>
</md-layout>
</template>

<script>
import Plot from './Plot.vue'
import RHApplications from './RHApplications.vue'
import Components from './Components.vue'
import Ports from './Ports.vue'
import EditComponentProperties from './EditComponentProperties.vue'
import RHApplication from './RHApplicationView.vue'
import RHDeviceManagers from './RHDeviceManagers.vue'
import RHDeviceManagerView from './RHDeviceManagerView.vue'
import EventChannelManager from './EventChannelManager.vue'
import EventChannel from './EventChannel.vue'

export default {
	name: 'rhdomainview',
	components: {
		'plot': Plot,
		'rhapplications': RHApplications,
		'waveformcomponents' : Components,
		'componentports' : Ports,
		'editcomponentprops' : EditComponentProperties,
		'redhawkapplication' : RHApplication,
		'rhdevicemanagers' : RHDeviceManagers,
		'rhdevicemanager' : RHDeviceManagerView,
		'eventchannelmgr' : EventChannelManager,
		'eventchannel' : EventChannel
	},
	computed: {
		baseURI() {
			return this.$store.getters.baseURI
		},
		availableWaveforms(){
			return this.$store.getters.availableWaveforms
		},
		numberOfWaveforms(){
			return this.availableWaveforms.length
		},
		domainConfig(){
			return this.$store.getters.configToView
		},
		showComponentProperties(){
			return this.$store.getters.showComponentProperties
		},
		showApplication(){
			return this.$store.getters.showApplication
		},
		showDeviceManager(){
			return this.$store.getters.showDeviceManager
		},
		showEventChannel(){
			return this.$store.getters.showEventChannel
		}
	},
	methods: {
		showWaveformLauncher(waveform){
			this.$store.dispatch('showLaunchWaveformModal', waveform)
		},
		refreshDomain(){
			console.log("Refresh view")
			var configIndex = this.$store.getters.domainConfigs.indexOf(this.domainConfig)
			this.$store.dispatch('viewDomainConfig', configIndex)
		}
	}
}
</script>

<style>
.rowHeight {
	min-height: 430px;
}

#refreshButton {
	position: absolute;
	right: 0;
}
</style>
