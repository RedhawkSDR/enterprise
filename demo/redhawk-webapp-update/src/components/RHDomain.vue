<template>
		<md-layout>
			<!--Should container Domain searching and plot -->
			<md-layout class="rowHeight">
				<div>
				<md-list>
					<md-list-item>
						Launch Waveforms
						<md-menu md-align-trigger>
							<md-button md-menu-trigger><md-icon>menu</md-icon>
							</md-button>
							<md-menu-content>
								<md-menu-item>WaveformName</md-menu-item>
								<md-menu-item>WaveformName2</md-menu-item>
								<md-menu-item>WaveformName3</md-menu-item>
							</md-menu-content>
						</md-menu>
					</md-list-item>
				</md-list>
				</div>
				<div>
					<h1>Plot Goes Here</h1>
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
import Sigplot from './Sigplot.vue'
import Waveforms from './Waveforms.vue'
import Components from './Components.vue'
import Ports from './Ports.vue'
import EditComponentProperties from './EditComponentProperties.vue'

export default {
	name: 'rhdomainview',
	components: {
		'signalplot': Sigplot,
		'rhwaveforms': Waveforms,
		'waveformcomponents': Components,
		'componentports': Ports,
		'editcomponentprops': EditComponentProperties
	},
	computed: {
		baseURI() {
			//TODO: Why doesn't this work....
			var nameServer = this.$store.configToView.nameServer
			var domainName = this.$store.configToView.domainName
			console.log('DomainName: '+domainName)
			//TODO: Should probably be configurable
			return "http://127.0.0.1:8181/cxf/redhawk/"+nameServer+"/domains/"+domainName
		}
	}
}
</script>

<style>
.rowHeight {
	min-height: 300px;
}
</style>
