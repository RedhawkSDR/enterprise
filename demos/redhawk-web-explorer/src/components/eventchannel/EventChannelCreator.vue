<template>
<v-flex>
	<v-card>
          <v-toolbar card color="red" prominent>
            <v-toolbar-title class="body-2 black--text">Create Event Creator</v-toolbar-title>
          </v-toolbar>
          <v-divider></v-divider>
		<v-card-text>
			<v-layout row>
				<v-flex>
					<v-text-field
					name="input-1"
					label="Event Channel Name"
					v-model="name"
					></v-text-field>
				</v-flex>
			</v-layout>
		</v-card-text>
    <v-card-actions>
      <v-btn
        @click="createEventChannel()"
        :disabled="!create">
        Create
      </v-btn>
    </v-card-actions>
	</v-card>
</v-flex>
</template>

<script>
	export default {
		name: 'eventchannelcreator',
    data(){
      return {
        create : false,
        name : null
      }
    },
    watch: {
      name: function(){
        console.log("sdflmsdmfs")
        if(this.name!=null && this.name.length>0){
          this.create = true
        }else{
          this.create = false
        }
      }
    },
    methods: {
      createEventChannel(){
        this.$store.dispatch('createEventChannel', this.name)
        this.$router.go(-1)
      }
    }
	}
</script>
