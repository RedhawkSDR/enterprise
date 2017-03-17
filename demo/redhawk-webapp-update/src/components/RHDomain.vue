<template>
	<md-layout>
		<signalplot></signalplot>
	</md-layout>
</template>

<script>
import Sigplot from './Sigplot.vue'

export default {
	name: 'rhdomainview',
	components: {
		'signalplot': Sigplot
	}
}
</script>