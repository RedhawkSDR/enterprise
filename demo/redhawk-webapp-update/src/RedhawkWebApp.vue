<template>
	<div>
		<md-toolbar>
			<h1 class="md-title">REDHAWK Webapp Example</h1>
			<md-button class="md-icon-button md-accent" @click.native="addDomainConfig">
				<md-icon>add box</md-icon>
			</md-button>
		</md-toolbar>
		<md-toolbar>
			<domainmenu
				v-for="(config, index) in configurations"
				v-bind:config="config"
				v-bind:index="index"
			>
		</domainmenu>
		</md-toolbar>
		<!--
		<md-layout>
			<md-layout md-flex="20">
				<md-layout md-column>
					<md-toolbar>
						<h2 class="md-title md-accent" style="flex: 1">Domains</h2>
						<md-button class="md-icon-button md-accent" @click.native="addDomainConfig">
							<md-icon>add box</md-icon>
						</md-button>
					</md-toolbar md-flex="20">
					<md-list>
						<md-list-item
							v-for="(config, index) in configurations"
							:key=config.name
							v-bind:index="index">
							{{ config.name }}
							<md-menu md-direction="bottom left">
								<md-button md-menu-trigger>
									<md-icon>menu</md-icon>
								</md-button>
								<md-menu-content>
									<md-menu-item @click.native="viewDomain(index)">View</md-menu-item>
									<md-menu-item @click.native="editDomainConfig(index)">Edit</md-menu-item>
									<md-menu-item @click.native="deleteDomainConfig(index)">Delete</md-menu-item>
									<md-divider></md-divider>
									<md-menu-item>Launch Wavforms</md-menu-item>
								</md-menu-content>
							</md-menu>
							<md-divider></md-divider>
						</md-list-item>
					</md-list>
				</md-layout>
			</md-layout>
			<md-layout v-if="showDomain">
				<rhdomain></rhdomain>
			</md-layout>
		</md-layout>
		-->
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
		}
	},
	components: {
		'rhdomainconfig' : RHDomainConfig,
		'rhdomain': RHDomain,
		'editdomainconfig' : EditRHDomainConfig,
		'waveformcontroller' : WaveformController,
		'launchwaveform' : LaunchWaveformModal,
		'domainmenu': DomainMenu
	},
	methods: {
		addDomainConfig: function() {
			this.showAddDomainConfig = true
		},
		addDomainToList: function(data){
			this.$store.dispatch('addDomainConfig', data)
		}
	}
}
</script>

<style>
</style>
