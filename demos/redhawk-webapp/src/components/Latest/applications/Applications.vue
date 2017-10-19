<template>
  <div class="content">
    <div class="card">
      <div class="card-header" data-background-color="red">
        <h4 class="title">Applications</h4>
        <p class="category">List of applications in {{ domainName }} domain</p>
      </div>
      <div class="card-content">
          <md-list>
                <md-list-item
                  v-for="(application, index) in applications"
                  v-bind:key="application">
                  <span><router-link :to="{ path: '/applications/'+application.name }">{{ application.name }}</router-link></span>
                  <div>
                  <md-button v-if="!application.started" class="md-icon-button md-raised" @click="play(index)">
                    <md-icon>play_arrow</md-icon>
                  </md-button>
                  <md-button v-else class="md-icon-button md-raised" @click="stop(index)">
                    <md-icon>stop</md-icon>
                  </md-button>
                  <md-button class="md-icon-button md-raised" @click="release(index)">
                    <md-icon>delete</md-icon>
                  </md-button>
                  </div>
                  <md-divider></md-divider>
                </md-list-item>
          </md-list>
      </div>
      </div>
    </div>
  </div>
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
  },
  methods: {
    play(index){
      var control = new Object();
      control.action = "start"
      control.applicationName = this.applications[index].name
      this.$store.dispatch('controlApplication', control)
    },
    stop(index){
      var control = new Object();
      control.action = "stop"
      control.applicationName = this.applications[index].name
      this.$store.dispatch('controlApplication', control)
    },
    release(index){
      this.$store.dispatch('releaseApplication', this.applications[index].name)
    }
  }
}
</script>
