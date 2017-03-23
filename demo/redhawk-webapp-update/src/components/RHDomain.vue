<template>
	<md-layout>
		<md-layout md-flex="25">
			<md-layout md-column>
				<!--
				<md-menu :md-offset-x="190" md-size=5>
					<md-button md-menu-trigger>Launch Waveforms</md-button>
					<md-menu-content>
						<md-menu-item
						v-for="(waveform,index) in availableWaveforms"
						v-bind:key="waveform"
						@click.native="showWaveformLauncher(waveform)"
						>
						{{ waveform.name }}
						</md-menu-item>
					</md-menu-content>
				</md-menu>
				-->
				<md-toolbar>
					<h1 class="md-title">{{ domainConfig.name }} :: {{ domainConfig.domainName}}</h1>
				</md-toolbar>
				<md-list>
					<md-list-item>
						<span>Launch Waveform
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
				<rhwaveforms></rhwaveforms>
			</md-layout>
		</md-layout>
		<md-layout>
			<md-layout md-column>
				<md-layout md-align="center" class="rowHeight">
					<plot></plot>
				</md-layout>
				<md-layout md-align="center" class="rowHeight">
					<waveformcomponents></waveformcomponents>
					<componentports></componentports>
				</md-layout>
			</md-layout>
		</md-layout>
	</md-layout>
<!--
<md-layout>
		<md-layout class="rowHeight">
			<div>
				<md-list>
					<md-list-item>
						<span>Launch Waveforms</span>
						<md-list-expand>
							<md-list>
								<md-list-item
								v-for="(waveform,index) in availableWaveforms"
								v-bind:key="waveform"
								@click.native="showWaveformLauncher(waveform)"
								>{{ waveform.name }}</md-list-item>
							</md-list>
						</md-list-expand>
					</md-list-item>
				</md-list>
			</div>
			<div>
				<plot></plot>
			</div>
		</md-layout>
	<md-layout  class="rowHeight" md-row md-flex="100">
		<md-layout md-flex="33">
			<rhwaveforms></rhwaveforms>
		</md-layout>
		<md-layout md-flex="33">
			<waveformcomponents></waveformcomponents>
		</md-layout>
		<md-layout md-flex="33">
			<componentports></componentports>
		</md-layout>
	</md-layout>
	<md-layout>
		<editcomponentprops></editcomponentprops>
	</md-layout>
</md-layout>
-->
</template>

<script>
import Plot from './Plot.vue'
import Waveforms from './Waveforms.vue'
import Components from './Components.vue'
import Ports from './Ports.vue'
import EditComponentProperties from './EditComponentProperties.vue'

export default {
	name: 'rhdomainview',
	components: {
		'plot': Plot,
		'rhwaveforms': Waveforms,
		'waveformcomponents': Components,
		'componentports': Ports,
		'editcomponentprops': EditComponentProperties,
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
		showWaveformComponents(){
			return this.$store.getters.showWaveformComponents
		}
	},
	methods: {
		showWaveformLauncher(waveform){
			console.log("Show Launcher for this wavform "+waveform)
			this.$store.dispatch('showLaunchWaveformModal', waveform)
		}
	},
	watch: {
		showWaveformComponents: function(){
			if(this.showWaveformComponents){
				console.log('Show Side Nav with Components')
			}else{
				console.log('Close Side Name Bar')
			}
		}
	}
}
</script>

<style>
.rowHeight {
	min-height: 430px;
}
</style>
