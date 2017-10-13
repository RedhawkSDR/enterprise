<template>
  <md-layout md-flex='75' md-column>
    <md-toolbar class="md-warn">
      <h1 class="md-title">Event Channel {{ eventchannel.name }}</h1>
    </md-toolbar>
    <md-layout md-row>
      <md-layout md-flex='75'>
        <div id="scrollable">
          <md-table id="ecTable">
          <md-table-header>
            <md-table-row>
              <md-table-head>Timestamp</md-table-head>
              <md-table-head>Type</md-table-head>
              <md-table-head>Message Body</md-table-head>
            </md-table-row>
          </md-table-header>

          <md-table-body>
            <md-table-row v-for="(row, index) in eventchannelData"
            :key="index">
            <md-table-cell>{{row.myTS}}</md-table-cell>
            <md-table-cell>{{row.type}}</md-table-cell>
            <md-table-cell>{{row.data}}</md-table-cell>
          </md-table-row>
        </md-table-body>
      </md-table>
      </div>
      <md-button id="subscribe" v-if="!subscribed" class="md-raised" @click.native="subscribe">Subscribe</md-button>
      <md-button id="unsubscribe" v-else class="md-raised" @click.native="unsubscribe">Unsubscribe</md-button>
    </md-layout>
    <md-layout md-flex='25'>
      <md-list>
        <md-subheader>
          <span class="md-title">Registrants [{{ registrants.length }}]</span>
        </md-subheader>
        <md-list-item
        v-for="(registrant, index) in registrants"
        >
        {{ registrant }}
        <md-button class="md-icon-button md-raised" @click.native="unregisterRegistrant(registrant)">
          <md-icon>close</md-icon>
        </md-button>
      </md-list-item>
    </md-list>
  </md-layout>
</md-layout>
</md-layout>
</template>

<script>
var eventchannelWS;
//TODO Use fifo


export default{
  name: 'eventchannel',
  computed: {
    eventchannel(){
      //TODO: Not correct
      return this.$store.getters.eventchannel
    },
    registrants(){
      return this.eventchannel.registrantIds
    },
    wsurl(){
      return this.eventchannel.wsurl
    }
  },
  data(){
    return {
      eventchannelWS : null,
      eventchannelData : [],
      subscribed: false
    }
  },
  methods: {
    subscribe(){
      console.log("Subscribe to channel")
      var self = this
      this.eventchannelWS = new WebSocket(this.wsurl)
      this.eventchannelWS.onopen = function(evt){
        self.$store.dispatch("updateEventChannelRegistrants", self.eventchannel.name)
      }
      this.eventchannelWS.onmessage = function(evt){
        //console.log(evt)
        ///Figure out why evt timestamp is incorrect and use it
        evt.myTS = self.getCurrentTimestamp()
        if(self.eventchannelData.length<10){
          self.eventchannelData.unshift(evt)
        }else{
          self.eventchannelData.pop()
          self.eventchannelData.unshift(evt)
        }
      }
      this.eventchannelWS.onerror = function(evt){
        console.log("ERROR "+evt)
      }
      this.eventchannelWS.onclose = function(){
        self.$store.dispatch("updateEventChannelRegistrants", self.eventchannel.name)
      }
      this.subscribed = true
    },
    unsubscribe(){
      this.eventchannelWS.close()
      this.subscribed = false
    },
    unregisterRegistrant(id){
      this.$store.dispatch("releaseRegistrant", id)
    },
    getCurrentTimestamp(){
      var d = new Date();

      return d.getHours()+':'+d.getMinutes()+':'+d.getSeconds()+'.'+d.getMilliseconds();
    }
  },
  watch: {
    eventchannel: function(){
      if(this.eventchannelWS!=null)
      this.eventchannelWS.close()

      //Reset to default if eventchannel changes
      this.eventchannelWS = null
      this.eventchannelData = []
      this.subscribed = false
    }
  }
}
</script>

<style>
#scrollable{
  overflow: auto;
}
#wrapper {
  width: 75vw;
  max-width: 75vw;
}
#channelSub {
  float: left;
  width: 75%;
  max-width: 75%;
}
#subscribe {
  background-color: green;
  margin: auto;
  display: block;
}
#unsubscribe {
  background-color: red;
  margin: auto;
  display: block;
}
#ecTable {
  min-height: 55vh;
  min-width: 75vw;
}
</style>
