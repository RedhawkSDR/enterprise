<template>
  <v-flex>
  	<v-card>
      <v-toolbar card color="red" prominent>
        <v-toolbar-title class="body-2 black--text">{{ device.label }} Metadata</v-toolbar-title>
      </v-toolbar>
      <v-divider></v-divider>
      <v-card-text>
        <h6><b>Started:</b>{{ device.started }}</h6>
        <h6><b>Admin State:</b>{{ device.adminState }}</h6>
        <h6><b>Usage State:</b>{{ device.usageState }}</h6>
        <h6><b>Operation State:</b>{{ device.operationState }}</h6>
        <h6><b>Implementation:</b>{{ device.implementation }}</h6>
      </v-card-text>
    </v-card>
  </v-flex>
</template>

<script>
export default {
  name: 'deviceMetaData',
  props: ['device'],
  computed: {
    domainName(){
      return this.$store.getters.domainName
    },
    devicemanagerLabel(){
      return this.$route.params.devicemanagerName
    }
  }
}
</script>
