<template>
	<div class="spa">
		<md-toolbar>
			<h1 class="md-title">REDHAWK Webapp Example</h1>
		</md-toolbar>
		<md-toolbar>
			<h1 class="md-title">Domains</h1>
			<md-button class="md-icon-button md-accent" @click.native="addDomainConfig">
				<md-icon>add</md-icon>
			</md-button>
			<domainmenu
				v-for="(config, index) in configurations"
				v-bind:config="config"
				v-bind:index="index"
			>
			</domainmenu>
		</md-toolbar>
		<md-layout v-if="showDomain">
			<rhdomain></rhdomain>
		</md-layout>
		<rhdomainconfig v-if="showAddDomainConfig" @close="showAddDomainConfig=false" @domainConfig="addDomainToList">
		</rhdomainconfig>
		<editdomainconfig v-if="showEditDomainConfig" @close="showEditDomainConfig=false">
		</editdomainconfig>
		<waveformcontroller v-if="showWaveformController" @close="showWaveformController=false">
		</waveformcontroller>
		<launchwaveform v-if="showLaunchWaveformModal" @close="showLaunchWaveformModal=false">
		</launchwaveform>
	</div>
</template>

<script>
import RHDomainConfig from './components/RHDomainConfig.vue'
import RHDomain from './components/RHDomain.vue'
import EditRHDomainConfig from './components/EditRHDomainConfig'
import WaveformController from './components/WaveformController'
import LaunchWaveformModal from './components/LaunchWaveformModal'
import DomainMenu from './components/DomainMenu.vue'

export default {
	name: 'redhawkwebapp',
	data(){
		return {
			showAddDomainConfig: false,
			test: [
				{
					name: 'Marcus'
				}
			]
		}
	},
	computed: {
		configurations() {
			return this.$store.getters.domainConfigs
		},
		showWaveformController(){
			return this.$store.getters.showWaveformController
		},
		showLaunchWaveformModal(){
			return this.$store.getters.showLaunchWaveformModal
		},
		showDomain(){
			return this.$store.getters.showDomain
		},
		showEditDomainConfig(){
			return this.$store.getters.showEditDomainConfig
		},
		availableWaveforms(){
			return this.$store.getters.availableWaveforms
		}
	},
	components: {
		'rhdomainconfig' : RHDomainConfig,
		'rhdomain': RHDomain,
		'editdomainconfig' : EditRHDomainConfig,
		'waveformcontroller' : WaveformController,
		'launchwaveform' : LaunchWaveformModal,
		'domainmenu': DomainMenu,
	},
	methods: {
		addDomainConfig: function() {
			this.showAddDomainConfig = true
		},
		addDomainToList: function(data){
			this.$store.dispatch('addDomainConfig', data)
		},
		showWaveformLauncher(waveform){
			console.log("Show Launcher for this wavform "+waveform)
			this.$store.dispatch('showLaunchWaveformModal', waveform)
		}
	}
}
</script>

<style>
.spa {
	overflow: hidden;
}
</style>
