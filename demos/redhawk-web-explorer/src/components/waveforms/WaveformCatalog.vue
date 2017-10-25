<template>
<v-flex>
	<v-card>
          <v-toolbar card color="red" prominent>
            <v-toolbar-title class="body-2 black--text">{{ domainName }} Waveform Catalog</v-toolbar-title>
          </v-toolbar>
          <v-divider></v-divider>
					<v-list>
					          <template v-for="(item, index) in waveforms">
					            <v-list-tile
												:to="{path:'/waveforms/'+item.name, query: {index : index } }"
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
		name: 'waveformcatalog',
		mounted(){
			this.$store.dispatch('getAvailableWaveforms')
		},
    computed: {
			domainName(){
					return this.$store.getters.domainName
			},
			waveforms(){
					return this.$store.getters.waveformcatalog
			}
		}
	}
</script>
