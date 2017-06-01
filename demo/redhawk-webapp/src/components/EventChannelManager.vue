<template>
<md-list>
  <md-list-item>
    <span>Event Channel [ {{ eventchannelCount }} ]</span>
    <md-list-expand>
        <md-list-item
          class="md-inset"
          v-for="(eventchannel, index) in eventchannels"
          v-bind:key="eventchannel"
          v-bind:index="index">
          <span>{{ eventchannel.name }}</span>
          <md-menu md-direction="bottom left">
            <md-button md-menu-trigger>
              <md-icon>menu</md-icon>
            </md-button>
            <md-menu-content>
              <md-menu-item @click.native="deleteChannel(index)">Shutdown</md-menu-item>
              <md-menu-item @click.native="showEventChannel(index)">View</md-menu-item>
            </md-menu-content>
          </md-menu>
          <md-divider></md-divider>
        </md-list-item>
    </md-list-expand>
  </md-list-item>
</md-list>
</template>

<script>
export default{
  name: 'eventchannels',
  computed: {
    eventchannels(){
      return this.$store.getters.eventchannels
    },
    eventchannelCount(){
      return this.eventchannels.length
    }
  },
  methods: {
    showEventChannel(index){
      console.log("Show Channel "+this.eventchannels[index].name)
      var eventChannel = new Object()

      eventChannel.show = true
      eventChannel.eventchannel = this.eventchannels[index]

      console.log(eventChannel)
      this.$store.dispatch('showEventChannel', eventChannel)
    }
  }
}
</script>

<style>
</style>
