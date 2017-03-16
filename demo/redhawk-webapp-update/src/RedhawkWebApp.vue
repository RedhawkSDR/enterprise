<template>
<div>
<md-toolbar>
	<h1 class="md-title">REDHAWK Webapp Example</h1>
</md-toolbar>
<!--
<rhdomainconfig></rhdomainconfig>
-->
</div>
</template>

<script>
import RHDomainConfig from './components/RHDomainConfig.vue'

export default {
	name: 'redhawkwebapp',
	components: {
		'rhdomainconfig' : RHDomainConfig
	}
}
</script>

<style>
</style>
