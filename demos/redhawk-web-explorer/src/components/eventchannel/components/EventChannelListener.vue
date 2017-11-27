<template>
	<v-flex xs12>
		<v-card>
			<v-toolbar card color="red" prominent>
				<v-toolbar-title class="body-2 black--text">{{ eventchannel.name }} Listener</v-toolbar-title>
			</v-toolbar>
			<v-divider></v-divider>
      <v-card-title>
				<v-spacer></v-spacer>
				<!--
        <v-text-field
				append-icon="search"
				label="Search"
				single-line
				hide-details
				v-model="search"
				></v-text-field>
        -->
			</v-card-title>
      <v-data-table
			v-bind:headers="headers"
			v-bind:items="messages"
			v-bind:search="search"
			v-bind:pagination.sync="pagination"
			>
			<template slot="items" scope="props">
        <td>
          {{ props.item.myTS}}
				</td>
				<td class="text-xs-right">
            {{ props.item.type }}
        </td>
        <td>
          {{ props.item.data }}
        </td>
      </template>
			<template slot="pageText" scope="{ pageStart, pageStop }">
				From {{ pageStart }} to {{ pageStop }}
			</template>
		</v-data-table>
    <v-card-actions>
      <v-btn v-if="!subscribed" @click="subscribe()">Connect</v-btn>
      <v-btn v-else="subscribe" @click="unsubscribe()">Disconnect</v-btn>
    </v-card-actions>
	</v-card>
</v-flex>
</template>

<script>
export default {
  name: 'eventchannellistener',
  props: ['eventchannel'],
	computed: {
    wsURL(){
      return this.eventchannel.wsURL
    }
  },
	destroyed(){
		if(this.websocket!=null){
			this.websocket.close()
			this.websocket = null
		}
	},
  methods: {
    subscribe(){
      console.log("Subscribe to channel")
      var self = this
      var name = this.eventchannel.name
      this.websocket = new WebSocket(this.wsURL)
      this.websocket.onopen = function(evt){
        self.$store.dispatch("getEventChannel", name)

        this.onmessage = function(evt){
          console.log('Received Data')
          evt.myTS = self.getCurrentTimestamp()
          if(self.messages.length<10){
            self.messages.unshift(evt)
          }else{
            self.messages.pop()
            self.messages.unshift(evt)
          }
        }

        this.onerror = function(evt){
          console.log("ERROR "+evt)
        }

        this.onclose = function(){
        }
      }

      this.subscribed = true
    },
    unsubscribe(){
      this.websocket.close()
      this.subscribed = false
      this.$store.dispatch('getEventChannel', this.eventchannel.name)
    },
    getCurrentTimestamp(){
      var d = new Date();

      return d.getHours()+':'+d.getMinutes()+':'+d.getSeconds()+'.'+d.getMilliseconds();
    }
  },
  data() {
    return{
      max25chars: (v) => v.length <= 25 || 'Input too long!',
      tmp: '',
      search: '',
      pagination: {},
      headers: [
        {
          text: 'Timestamp',
					align: 'left',
					sortable: true,
					value: 'name'
        },
        {
          text: 'Type',
					align: 'left',
					sortable: true,
					value: 'name'
        },
        {
          text: 'Message Body',
					align: 'left',
					sortable: true,
					value: 'name'
        }
      ],
      messages: [],
      websocket: null,
      subscribed : false
    }
  }
}
</script>
