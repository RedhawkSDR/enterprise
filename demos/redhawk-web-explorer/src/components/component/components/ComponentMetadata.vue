<template>
  <v-flex>
  	<v-card>
      <v-toolbar card color="red" prominent>
        <v-toolbar-title class="body-2 black--text">{{ component.name }} Metadata</v-toolbar-title>
      </v-toolbar>
      <v-divider></v-divider>
      <v-card-text>
        <h6><b>Identifier:</b>{{ component.started }}</h6>
        <h6><b>Process Id:</b>{{ component.processId }}</h6>
        <h6><b>Device Identifier:</b>{{ component.deviceIdentifier }}</h6>
        <h6><b>Implementation:</b>{{ component.implementation }}</h6>
      </v-card-text>
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
  }
}
</script>
