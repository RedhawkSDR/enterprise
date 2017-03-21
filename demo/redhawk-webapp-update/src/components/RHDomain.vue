<template>
	<md-layout>
		<!--Should container Domain searching and plot -->
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
			<!--<signalplot></signalplot>-->
		</md-layout>
		<!--
		Waveforms | Components | Ports
	-->
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
		}
	},
	methods: {
		showWaveformLauncher(waveform){
			console.log("Show Launcher for this wavform "+waveform)
			this.$store.dispatch('showLaunchWaveformModal', waveform)
		}
	}
}
</script>

<style>
.rowHeight {
	min-height: 300px;
}
</style>
