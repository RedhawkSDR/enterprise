<template>
		<md-layout>
			<md-layout class="rowHeight">
				<signalplot></signalplot>
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
				Hola
			</md-layout>
		</md-layout>
</template>

<script>
import Sigplot from './Sigplot.vue'
import Waveforms from './Waveforms.vue'
import Components from './Components.vue'
import Ports from './Ports.vue'

export default {
	name: 'rhdomainview',
	components: {
		'signalplot': Sigplot,
		'rhwaveforms': Waveforms,
		'waveformcomponents': Components,
		'componentports': Ports
	}
}
</script>

<style>
.rowHeight {
	min-height: 300px;
}
</style>
