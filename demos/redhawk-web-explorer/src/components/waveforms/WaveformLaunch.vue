<template>
	<v-flex>
		<v-card>
			<v-toolbar card color="red" prominent>
				<v-toolbar-title class="body-2 black--text">REDHAWK Domain</v-toolbar-title>
			</v-toolbar>
			<v-divider></v-divider>
			<v-card-text>
				<v-layout row>
					<v-flex>
						<v-text-field
						name="input-1"
						label="Id"
						v-model="waveform.id"
						disabled
						></v-text-field>
						<v-text-field
						name="input-1"
						label="Name"
						v-model="waveform.name"
						disabled
						></v-text-field>
						<v-text-field
						name="input-1"
						label="Name"
						v-model="waveform.sadLocation"
						disabled
						></v-text-field>
						<v-text-field
						name="input-1"
						label="Application Name"
						v-model="applicationName"
						></v-text-field>
					</v-flex>
				</v-layout>
			</v-card-text>
			<v-card-action>
				<v-btn @click="launch" :disabled="disableLaunch">Launch</v-btn>
			</v-card-action>
		</v-card>
	</v-flex>
</template>

<script>
export default {
	name: 'waveformlaunch',
	data(){
		return {
			applicationName: null,
			disableLaunch: true
		}
	},
	mounted(){
		console.log(this.$route.query.index)
	},
	watch: {
		applicationName: function(){
			if(this.applicationName==null || this.applicationName==''){
				this.disableLaunch = true
			}else{
				this.disableLaunch = false
			}
		}
	},
	computed: {
		index(){
			return this.$route.query.index
		},
		waveform(){
			return this.$store.getters.waveformcatalog[this.index]
		}
	},
	methods: {
		launch(){
			var waveformToLaunch = new Object()
			waveformToLaunch.name = this.applicationName
			waveformToLaunch.id = this.applicationName
			waveformToLaunch.sadLocation = this.waveform.sadLocation
			this.$store.dispatch('launchChoosenWaveform', waveformToLaunch)

			//TODO: On successful launch user should be redirected
		}
	}
}
</script>
