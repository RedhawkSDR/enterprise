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
					label="Domain Name"
					v-model="domainName"
					></v-text-field>
          <v-text-field
          name="input-1"
          label="Name Server Address"
          v-model="nameServer"
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
			domainName: {
				get(){
					return this.$store.getters.domainName
				},
				set(value){
					this.$store.dispatch('setDomainName', value)
				}
			},
      nameServer:{
        get(){
          return this.$store.getters.nameServer
        },
        set(value){
          this.$store.dispatch('setNameServer', value)
        }
      }
		}
	}
</script>
