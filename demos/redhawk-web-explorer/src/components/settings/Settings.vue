<template>
<v-flex>
	<v-card>
          <v-toolbar card color="red" prominent>
            <v-toolbar-title class="body-2 black--text">REDHAWK Web Explorer Settings</v-toolbar-title>
          </v-toolbar>
          <v-divider></v-divider>
		<v-card-text>
			<v-layout row>
				<v-flex>
					<v-text-field
					name="input-1"
					label="Base URL for REST"
					v-model="baseURL"
					></v-text-field>
          <v-text-field
          name="input-1"
          label="Base URL for Websockets"
          v-model="wsBaseURL"
          ></v-text-field>
				</v-flex>
			</v-layout>
		</v-card-text>
	</v-card>
</v-flex>
</template>

<script>
	export default {
		name: 'settings',
    computed: {
			baseURL: {
				get(){
					return this.$store.getters.baseURL
				},
				set(value){
					this.$store.dispatch('setBaseURL', value)
				}
			},
      wsBaseURL:{
        get(){
          return this.$store.getters.wsBaseURL
        },
        set(value){
          this.$store.dispatch('setWsBaseURL', value)
        }
      }
		}
	}
</script>
