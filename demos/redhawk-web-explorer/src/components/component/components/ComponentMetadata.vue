<template>
  <v-flex>
  	<v-card>
      <v-toolbar card color="red" prominent>
        <v-toolbar-title class="body-2 black--text">{{ component.name }} Metadata</v-toolbar-title>
      </v-toolbar>
      <v-divider></v-divider>
      <v-card-text>
        <h6><b>Started:</b>{{ component.started }}</h6>
        <h6><b>Process Id:</b>{{ component.processId }}</h6>
        <h6><b>Device Identifier:</b>{{ component.deviceIdentifier }}</h6>
        <h6><b>Implementation:</b>{{ component.implementation }}</h6>
      </v-card-text>
      <v-card-actions>
        <v-flex justify-center>
          <v-btn
            @click="play()"
            :disabled="component.started"
            fab small color="green">
            <v-icon>play_arrow</v-icon>
          </v-btn>
          <v-btn
            @click="stop()"
            :disabled="!component.started"
            fab small color="red">
            <v-icon>stop</v-icon>
          </v-btn>
        </v-flex>
      </v-card-actions>
    </v-card>
  </v-flex>
</template>

<script>
export default {
  name: 'ComponentMetaData',
  props: ['component'],
  computed: {
    domainName(){
      return this.$store.getters.domainName
    },
    applicationName(){
      return this.$route.params.applicationName
    }
  },
  methods: {
    play(){
      var control = new Object();
      control.action = 'start'
      control.applicationName = this.applicationName
      control.componentName = this.component.name

      this.$store.dispatch('controlComponent', control)
    },
    stop(){
      var control = new Object();
      control.action = 'stop'
      control.applicationName = this.applicationName
      control.componentName = this.component.name

      this.$store.dispatch('controlComponent', control)
    }
  }
}
</script>
