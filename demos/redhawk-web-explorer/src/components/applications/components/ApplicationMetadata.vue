<template>
  <v-flex>
  	<v-card>
      <v-toolbar card color="red" prominent>
        <v-toolbar-title class="body-2 black--text">{{ application.name }} Metadata</v-toolbar-title>
      </v-toolbar>
      <v-divider></v-divider>
      <v-card-text>
        <h6><b>Identifier:</b>{{ application.identifier }}</h6>
        <h6><b>Aware:</b>{{ application.aware }}</h6>
      </v-card-text>
      <v-card-actions>
        <v-btn fab color="green">
          <v-icon>play_arrow</v-icon>
        </v-btn>
        <v-btn fab color="red">
          <v-icon>stop</v-icon>
        </v-btn>
        <v-btn fab color="blue">
          <v-icon>delete</v-icon>
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-flex>
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
