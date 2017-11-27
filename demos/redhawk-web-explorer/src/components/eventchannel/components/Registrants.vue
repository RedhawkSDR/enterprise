<template>
	<v-flex>
		<v-card>
			<v-toolbar card color="red" prominent>
				<v-toolbar-title class="body-2 black--text">{{ eventchannel.name }} Registrants</v-toolbar-title>
				<v-spacer></v-spacer>
				<!--
        <v-btn
					:to="'/eventchannels/create'"
					fab color="blue">
					<v-icon>add</v-icon>
				</v-btn>
        -->
			</v-toolbar>
			<v-divider></v-divider>
      <v-list>
				<template v-for="(item, index) in eventchannel.registrantIds">
					<v-list-tile
					>
					<v-list-tile-content>
						<v-list-tile-title>
							{{ item }}
						</v-list-tile-title>
					</v-list-tile-content>
					<v-list-tile-actions>
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
	name: 'registrants',
  props: ['eventchannel'],
  methods: {
    release(index){
			this.$store.dispatch('releaseRegistrant', this.eventchannel.registrantIds[index])
		}
  }
}
</script>
