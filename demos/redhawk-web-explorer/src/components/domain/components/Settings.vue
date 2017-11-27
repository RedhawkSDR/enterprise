<template>
<v-flex>
	<v-card>
          <v-toolbar card color="red" prominent>
            <v-toolbar-title class="body-2 black--text">Domain Manager</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn
            @click="editDomain()"
            icon>
                 <v-icon>edit</v-icon>
            </v-btn>
          </v-toolbar>
          <v-divider></v-divider>
		<v-card-text>
			<v-layout row>
				<v-flex>
					<v-text-field
					name="input-1"
					label="domainName"
					v-model="domainName"
          :disabled="!edit"
					></v-text-field>
          <v-text-field
          name="input-1"
          label="Name Server"
          v-model="nameServer"
          :disabled="!edit"
          ></v-text-field>
				</v-flex>
			</v-layout>
		</v-card-text>
    <v-card-actions>
      <v-btn
      @click="connectToDomain()"
      :disabled="!edit"
      >
        Connect
      </v-btn>
    </v-card-actions>
	</v-card>
</v-flex>
</template>

<script>
	export default {
		name: 'domainsettings',
    data(){
      return {
        edit : false,
      }
    },
    methods: {
      editDomain(){
        this.edit = true
      },
      connectToDomain(){
        this.edit = false
        console.log("Calling selectDomainProperties "+this.domainName)
        this.$store.dispatch('selectDomainProperties', this.domainName)
      }
    },
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
