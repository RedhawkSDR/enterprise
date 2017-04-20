<template>
	<div class="modal-mask">
		<div class="modal-wrapper">
			<div class="config-container">
				<md-toolbar style="flex:1">
					<h1 class="md-title">REDHAWK Domain Configuration</h1>
				</md-toolbar>
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
				<div style="text-align: center">
					<md-button class="md-raised md-warn" @click.native="cancel">Cancel</md-button>
					<md-button class="md-raised md-primary" @click.native="addConfig">Add</md-button>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
export default{
	name: 'rhdomainconfig',
	data () {
		return {
			configurationName: null,
			nameServer: '127.0.0.1:2809',
			domainName: 'REDHAWK_DEV'
		}
	},
	methods: {
		cancel: function(){
			this.$emit('close')
		},
		addConfig: function(){
			var configuration = new Object()
			configuration.name = this.configurationName
			configuration.nameServer = this.nameServer
			configuration.domainName = this.domainName

			this.$emit("domainConfig", configuration)
			this.$emit("close")
		}
	}
}
</script>

<style>
.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, .5);
  display: table;
  transition: opacity .3s ease;
}

.modal-wrapper {
  display: table-cell;
  vertical-align: middle;
}

.config-container {
  width: 400px;
  margin: 0px auto;
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, .33);
  transition: all .3s ease;
  font-family: Helvetica, Arial, sans-serif;
}
</style>
