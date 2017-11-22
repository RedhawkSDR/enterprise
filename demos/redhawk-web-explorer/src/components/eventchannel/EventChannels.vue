<template>
	<v-flex>
		<v-card>
			<v-toolbar card color="red" prominent>
				<v-toolbar-title class="body-2 black--text">{{ domainName }} Event Channels</v-toolbar-title>
				<v-spacer></v-spacer>
        <v-btn
					:to="'/eventchannelcreator'"
					fab color="blue">
					<v-icon>add</v-icon>
				</v-btn>
			</v-toolbar>
			<v-divider></v-divider>
      <v-list>
				<template v-for="(item, index) in eventchannels">
					<v-list-tile
					>
					<v-list-tile-content>
						<v-list-tile-title>
							<router-link :to="{path:'/eventchannels/'+item.name}">{{ item.name }}</router-link>
						</v-list-tile-title>
					</v-list-tile-content>
					<v-list-tile-action>
						<v-btn
							@click="release(index)"
							fab small color="blue">
							<v-icon>delete</v-icon>
						</v-btn>
					</v-list-tile-action>
				</v-list-tile>
				<v-divider></v-divider>
			</template>
		</v-list>
	</v-card>
</v-flex>
</template>

<script>
export default {
	name: 'eventchannellist',
	mounted(){
		this.$store.dispatch('getEventChannels')
	},
  computed: {
    domainName(){
      return this.$store.getters.domainName
    },
		eventchannels(){
			return this.$store.getters.eventchannels
		}
  },
	methods: {
		release(index){
			var name = this.eventchannels[index].name
			this.$store.dispatch('deleteEventChannel', name)
		}
	}
}
</script>
