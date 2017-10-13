<template>
	<div class="modal-mask">
		<div class="modal-wrapper">
			<div class="config-container">
				<md-toolbar>
	        <h1 class="md-title">Domain Config</h1>
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
					<md-button class="md-raised md-warn" @click.native="cancel">close</md-button>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
export default{
	name: 'editdomainconfig',
	props: ['showEditDomainConfig'],
	computed: {
		configurationName: {
			get(){
				return this.$store.getters.configToEdit.name
			},
			set(value){
				this.$store.dispatch('editDomainConfigName', value)
			}
		},
		nameServer: {
			get(){
				return this.$store.getters.configToEdit.nameServer
			},
			set(value){
				this.$store.dispatch('editDomainConfigNameServer', value)
			}
		},
		domainName: {
			get(){
				return this.$store.getters.configToEdit.domainName
			},
			set(value){
				this.$store.dispatch('editDomainConfigDomainName', value)
			}
		}
	},
	methods: {
		cancel: function(){
			this.$store.dispatch('closeEditDomainConfig')
		}
	}
}
</script>

<style>
.config-container {
  width: 400px;
  margin: 0px auto;
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, .33);
  transition: all .3s ease;
  font-family: Helvetica, Arial, sans-serif;
}
</style>
