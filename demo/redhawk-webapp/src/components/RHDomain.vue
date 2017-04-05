<template>
	<md-layout>
		<md-layout md-flex="25">
			<md-layout md-column>
				<md-toolbar>
					<h1 class="md-title">{{ domainConfig.name }} :: {{ domainConfig.domainName}}</h1>
				</md-toolbar>
				<md-list>
					<md-list-item>
						<span>Waveforms
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
				<!--
				TODO: Add ability to allocate a device restfully
				<md-list-item>
					<span>Device Managers</span>
				</md-list-item>
				-->
			</md-layout>
		</md-layout>
		<redhawkapplication v-if="showApplication"></redhawkapplication>
		<rhdevicemanager v-if="showDeviceManager"></rhdevicemanager>
		<!--
		<md-layout>
			<md-layout md-flex md-column>
				<md-layout md-align="center" class="rowHeight">
					<plot></plot>
				</md-layout>
				<md-layout md-align="center" class="rowHeight">
					<md-layout>
						<waveformcomponents></waveformcomponents>
					</md-layout>
					<md-layout>
						<componentports></componentports>
					</md-layout>
				</md-layout>
			</md-layout>
			<md-layout md-flex='25' v-if="showComponentProperties">
				<editcomponentprops></editcomponentprops>
			</md-layout>
		</md-layout>
		-->
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
		'rhdevicemanager' : RHDeviceManagerView
	},
	computed: {
		baseURI() {
			return this.$store.getters.baseURI
		},
		availableWaveforms(){
			return this.$store.getters.availableWaveforms
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
		}
	},
	methods: {
		showWaveformLauncher(waveform){
			this.$store.dispatch('showLaunchWaveformModal', waveform)
		}
	}
}
</script>

<style>
.rowHeight {
	min-height: 430px;
}
</style>
