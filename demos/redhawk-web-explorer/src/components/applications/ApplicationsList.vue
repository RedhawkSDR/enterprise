<template>
	<v-flex>
		<v-card>
			<v-toolbar card color="red" prominent>
				<v-toolbar-title class="body-2 black--text">{{ domainName }} Applications</v-toolbar-title>
				<v-spacer></v-spacer>
				<v-btn
					:to="'/waveforms'"
					fab color="blue">
					<v-icon>add</v-icon>
				</v-btn>
			</v-toolbar>
			<v-divider></v-divider>
			<v-list>
				<template v-for="(item, index) in applications">
					<v-list-tile
					>
					<v-list-tile-content>
						<v-list-tile-title>
							<router-link :to="{path:'/applications/'+item.name}">{{ item.name }}</router-link>
						</v-list-tile-title>
					</v-list-tile-content>
					<!-- TODO: Clean this up use an appropriate section -->
					<v-list-tile-actions>
						<v-btn
							@click="play(index)"
							:disabled="item.started"
							fab small color="green">
							<v-icon>play_arrow</v-icon>
						</v-btn>
						<v-btn
							@click="stop(index)"
							:disabled="!item.started"
							fab small color="red">
							<v-icon>stop</v-icon>
						</v-btn>
						<v-btn
							@click="release(index)"
							fab small color="blue">
							<v-icon>delete</v-icon>
						</v-btn>
					</v-list-tile-actions>
				</v-list-tile>
				<v-divider></v-divider>
			</template>
		</v-list>
	</v-card>
</v-flex>
</template>

<script>
export default {
	name: 'applications',
	mounted(){
		this.$store.dispatch('getApplicationsInDomain')
	},
	computed: {
		domainName(){
			return this.$store.getters.domainName
		},
		applications(){
			return this.$store.getters.applications
		}
	},
	methods: {
    play(index){
      var control = new Object();
      control.action = "start"
      control.applicationName = this.applications[index].name
      this.$store.dispatch('controlApplication', control)
    },
    stop(index){
      var control = new Object();
      control.action = "stop"
      control.applicationName = this.applications[index].name
      this.$store.dispatch('controlApplication', control)
    },
    release(index){
      this.$store.dispatch('releaseApplication', this.applications[index].name)
    }
  }
}
</script>
