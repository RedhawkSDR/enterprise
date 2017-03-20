<template>
	<div>
		<md-toolbar>
			<h1 class="md-title">REDHAWK Webapp Example</h1>
		</md-toolbar>
		<md-layout>
			<md-layout md-flex="20">
				<md-layout md-column>
					<md-toolbar>
						<h2 class="md-title" style="flex: 1">Domains</h2>
						<md-button class="md-icon-button" @click.native="addDomainConfig">
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
									<md-icon>more horiz</md-icon>
								</md-button>
								<md-menu-content>
									<md-menu-item @click.native="viewDomain(index)">View</md-menu-item>
									<md-menu-item @click.native="editDomainConfig(index)">Edit</md-menu-item>
									<md-menu-item @click.native="deleteDomainConfig(index)">Delete</md-menu-item>
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
		<rhdomainconfig v-if="showAddDomainConfig" @close="showAddDomainConfig=false" @domainConfig="addDomainToList">
		</rhdomainconfig>
		<editdomainconfig v-if="showEditDomainConfig" @close="showEditDomainConfig=false">
		</editdomainconfig>
	</div>
</template>

<script>
import RHDomainConfig from './components/RHDomainConfig.vue'
import RHDomain from './components/RHDomain.vue'
import EditRHDomainConfig from './components/EditRHDomainConfig'
import {EventBus} from './event-bus/event-bus.js'

export default {
	name: 'redhawkwebapp',
	data(){
		return {
			showAddDomainConfig: false,
			showEditDomainConfig: false,
			showDomain: false
		}
	},
	computed: {
		configurations() {
			return this.$store.getters.domainConfigs
		}
	},
	components: {
		'rhdomainconfig' : RHDomainConfig,
		'rhdomain': RHDomain,
		'editdomainconfig' : EditRHDomainConfig
	},
	methods: {
		addDomainConfig: function() {
			this.showAddDomainConfig = true
		},
		addDomainToList: function(data){
			this.$store.dispatch('addDomainConfig', data)
		},
		editDomainConfig: function(data){
			console.log("Need to allow editing..."+data)
			this.$store.dispatch('editDomainConfig', data)
			this.showEditDomainConfig = true
		},
		deleteDomainConfig(data){
			this.$store.dispatch('deleteDomainConfig', data)
		},
		viewDomain: function(data){
			this.showDomain = true
			console.log("View Domain")
			EventBus.$emit('updateLaunchedWaveforms')
		}
	}
}
</script>

<style>
</style>
