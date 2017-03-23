<template>
	<div class="modal-mask">
		<div class="modal-wrapper">
			<div class="modal-container">
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
					<md-button class="md-raised md-warn" @click.native="cancel">close</md-button>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
//import {EventBus} from '../event-bus/event-bus.js'

export default{
	name: 'editdomainconfig',
	props: ['showEditDomainConfig'],
	computed: {
		configurationName: {
			get(){
				return this.$store.getters.configToEdit.name
			},
			set(value){
				console.log('setttng')
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
	.modal-mask {
	position: fixed;
	z-index: 9998;
	top: 0;
	left: 0;
	width:
	100%;
	height: 100%;
	background-color: rgba(0, 0, 0, .5);
	display: table;
	transition: opacity .3s ease;
	}

	.modal-wrapper {
	display: table-cell;
	vertical-align: middle;
	}

	.modal-container {
	width: 300px;
	margin: 0px
	auto;
	padding: 20px 30px;
	background-color: #fff;
	border-radius: 2px;
	box-shadow: 0 2px 8px rgba(0, 0, 0, .33);
	transition: all .3s ease;
	font-family: Helvetica, Arial, sans-serif;
	}
</style>
