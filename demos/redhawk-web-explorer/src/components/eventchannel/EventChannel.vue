<template>
  <v-container grid-list-md text-xs-center>
  		<v-layout row wrap>
        <eventchannellistener :eventchannel="eventchannel" />
  			<registrants :eventchannel="eventchannel"/>
  		</v-layout>
  	</v-container>
</template>

<script>
import Registrants from './components/Registrants'
import EventChannelListener from './components/EventChannelListener'

export default {
  name: 'eventchannel',
  mounted(){
    this.$store.dispatch('getEventChannel', this.$route.params.eventchannelName)
  },
  components: {
    'registrants' : Registrants,
    'eventchannellistener' : EventChannelListener
  },
  computed: {
    eventchannel(){
      return this.$store.getters.eventchannel
    }
  }
}
</script>
