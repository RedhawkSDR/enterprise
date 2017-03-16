<template>
<div>
<md-input-container>
	<label>Configuration Name</label>
	<md-input v-model="configurationName"></md-input>
</md-input-container>
<md-input-container>
	<label>Name Server</label>
	<md-input v-model="nameServer"></md-input>
</md-input-container>
<md-input-container>
	<label>Domain Name</label>
	<md-input v-model="domainName"></md-input>
</md-input-container>
<div>
<md-button class="md-raised md-warn">Cancel</md-button>
<md-button class="md-raised md-primary">Connect</md-button>
</div>
</div>
</template>

<script>
export default{
	name: 'rhdomainconfig'
}
</script>

<style>
</style>