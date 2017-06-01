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
  }
}
</script>

<style>
</style>
