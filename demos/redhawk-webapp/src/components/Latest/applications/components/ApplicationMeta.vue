<template>
<div class="card">
  <div class="card-header" data-background-color="green">
    <h4 class="title">{{ application.name }}</h4>
    <p class="category">Application running in {{ domainName }} domain</p>
  </div>
  <div class="card-content">
    <div>
      <h6><b>Identifier:</b>{{ application.identifier }}</h6>
    </div>
    <div>
      <h6><b>aware:</b>{{ application.aware }}</h6>
    </div>
    <div>
      <!--TODO: Make this a footer and move to middle oh and make work fluently -->
      <md-button :disabled="application.started" class="md-icon-button md-raised" @click="play()">
        <md-icon>play_arrow</md-icon>
      </md-button>
      <md-button :disabled="!application.started" class="md-icon-button md-raised" @click="stop()">
        <md-icon>stop</md-icon>
      </md-button>
      <md-button class="md-icon-button md-raised" @click="release()">
        <md-icon>delete</md-icon>
      </md-button>
    </div>
  </div>
</div>
</template>

<script>
export default {
  name: 'applicationMetaData',
  props: ['application'],
  computed: {
    domainName(){
      return this.$store.getters.domainName
    }
  },
  methods: {
    play(){
      var control = new Object();
      control.action = "start"
      control.applicationName = this.application.name
      this.$store.dispatch('controlApplication', control)
    },
    stop(){
      var control = new Object();
      control.action = "stop"
      control.applicationName = this.application.name
      this.$store.dispatch('controlApplication', control)
    },
    release(){
      this.$store.dispatch('releaseApplication', this.application.name)
    }
  }
}
</script>
