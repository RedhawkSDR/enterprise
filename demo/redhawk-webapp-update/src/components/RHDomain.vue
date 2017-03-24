<template>
	<md-layout>
		<md-layout md-flex="25">
			<md-layout md-column>
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
				<md-list-item>
					<span>Device Managers</span>
				</md-list-item>
			</md-layout>
		</md-layout>
		<md-layout>
			<md-layout md-flex md-column>
				<md-layout md-align="center" class="rowHeight">
					<plot></plot>
					<!--<smoothieplot></smoothieplot>-->
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
	</md-layout>
</template>

<script>
import Plot from './Plot.vue'
import Waveforms from './Waveforms.vue'
import Components from './Components.vue'
import Ports from './Ports.vue'
import EditComponentProperties from './EditComponentProperties.vue'
import SmoothiePlot from './SmoothiePlot.vue'

export default {
	name: 'rhdomainview',
	components: {
		'plot': Plot,
		'rhwaveforms': Waveforms,
		'waveformcomponents': Components,
		'componentports': Ports,
		'editcomponentprops': EditComponentProperties,
		'smoothieplot' : SmoothiePlot
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
