<template>
<div id="wrapper">
  <md-toolbar class="md-warn">
    <h1 class="md-title">Event Channel {{ eventchannel.name }}</h1>
  </md-toolbar>
  <div id="channelSub">
  <!-- Add Event Channel Name and Way to close view -->
  <md-table v-once>
    <md-table-header>
      <md-table-row>
        <md-table-head>Timestamp</md-table-head>
        <md-table-head>Message Body</md-table-head>
      </md-table-row>
    </md-table-header>

    <md-table-body>
      <!--<md-table-row v-for="(row, index) in messages"-->
    </md-table-body>
  </md-table>

  <md-button class="md-raised" @click.native="subscribe">Subscribe</md-button>
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
export default{
  name: 'eventchannel',
  computed: {
    eventchannel(){
      console.log("Getting event channel")
      return this.$store.getters.eventchannel
    },
    registrants(){
      return this.eventchannel.registrantIds
    }
  },
  methods: {
    subscribe(){
      console.log("Subscribe to channel")
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
</style>
