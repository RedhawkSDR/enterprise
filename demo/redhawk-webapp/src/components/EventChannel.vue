<template>
<div id="wrapper">
  <md-toolbar class="md-warn">
    <h1 class="md-title">Event Channel {{ eventchannel.name }}</h1>
  </md-toolbar>
  <div id="channelSub">
  <!-- Add Event Channel Name and Way to close view -->
  <md-table>
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
        <md-table-cell>{{row.timeStamp}}</md-table-cell>
        <md-table-cell>{{row.type}}</md-table-cell>
        <md-table-cell>{{row.data}}</md-table-cell>
      </md-table-row>
    </md-table-body>
  </md-table>
  <!--
  Works!!!!
  <table class="table">
    <thead>
      <tr>
        <td><strong>Timestamp</strong></td>
        <td><strong>Type</strong></td>
        <td><strong>Data</strong></td>
      </tr>
    </thead>
    <tbody>
      <tr v-for="row in eventchannelData">
        <td>Timestamp</td>
        <td>{{row.type}}</td>
        <td><input type="text" v-model="row.data"></td>
      </tr>
    </tbody>
  </table>
  -->
  <md-button id="subscribe" v-if="!subscribed" class="md-raised" @click.native="subscribe">Subscribe</md-button>
  <md-button id="unsubscribe" v-else class="md-raised" @click.native="unsubscribe">Unsubscribe</md-button>
  </div>
  <div id="registrants">
    <md-toolbar md-theme="white">
      <span class="md-title">Registrants [{{ registrants.length }}]</span>
    </md-toolbar>
    <md-list>
      <md-list-item
        v-for="(registrant, index) in registrants"
      >
      {{ registrant }}
      </md-list-item>
    </md-list>
  </div>
</div>
</template>

<script>
var eventchannelWS;
//TODO Use fifo


export default{
  name: 'eventchannel',
  computed: {
    eventchannel(){
      console.log("Getting event channel")
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
        console.log("Connected")
      }
      this.eventchannelWS.onmessage = function(evt){
        console.log(evt)
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
        console.log("Closed")
      }
      this.subscribed = true
    },
    unsubscribe(){
      this.eventchannelWS.close()
      this.subscribed = false
    }
  }
}
</script>

<style>
  #wrapper {
    width: 100%
  }
  #channelSub {
    float: left;
    width: 75%
  }
  #subscribe {
    background-color: green
  }
  #unsubscribe {
    background-color: red
  }
</style>
