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
					:to="{path:'/applications/'+item.name}"
					>
					<v-list-tile-content>
						<v-list-tile-title>{{ item.name }}</v-list-tile-title>
					</v-list-tile-content>
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
	}
}
</script>
