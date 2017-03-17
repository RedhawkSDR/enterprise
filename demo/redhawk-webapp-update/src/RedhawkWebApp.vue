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
						<md-list-item v-for="config in configurations">
							{{ config.name }}
							<md-button class="md-icon-button" @click.native="editDomainConfig">
								<md-icon>settings</md-icon>
							</md-button>
							<md-button class="md-icon-button" @click.native="viewDomain">
								<md-icon>tv</md-icon><!-- Alternative is search icon -->
							</md-button>
							<md-divider></md-divider>
						</md-list-item>
					</md-list>
				</md-layout>
			</md-layout>
			<md-layout v-if="showDomain">
				<rhdomain></rhdomain>
			</md-layout>
		</md-layout>
		<rhdomainconfig v-if="showDomainConfig" @close="showDomainConfig=false" @domainConfig="addDomainToList">
		</rhdomainconfig>
	</div>
</template>

<script>
import RHDomainConfig from './components/RHDomainConfig.vue'
import RHDomain from './components/RHDomain.vue'
import {EventBus} from './event-bus/event-bus.js'


export default {
	name: 'redhawkwebapp',
	data(){
		return {
			showDomainConfig: false,
			configurations: [],
			showDomain: false
		}
	},
	components: {
		'rhdomainconfig' : RHDomainConfig,
		'rhdomain': RHDomain
	},
	methods: {
		addDomainConfig: function() {
			this.showDomainConfig = true
		},
		addDomainToList: function(data){
			console.log("HOLA! "+JSON.stringify(data))
			this.configurations.push(data)
		},
		editDomainConfig: function(data){
			console.log("Need to allow editing...")
		},
		viewDomain: function(data){
			this.showDomain = true
		}
	}
}
</script>

<style>
</style>
