<template>
  <v-flex xs12>
  	<v-card>
      <v-toolbar card color="red" prominent>
        <v-toolbar-title class="body-2 black--text">{{ port.name }} Metadata</v-toolbar-title>
      </v-toolbar>
      <v-divider></v-divider>
      <v-card-text>
        <h6 v-if="meta.type=='Component'"><b>Application:</b>{{ meta.applicationName }}</h6>
        <h6 v-if="meta.type=='Component'"><b>Component:</b>{{ meta.componentName }}</h6>
        <h6 v-if="meta.type=='Device'"><b>Device Manager:</b>{{ meta.devicemanagerLabel }}</h6>
        <h6 v-if="meta.type=='Device'"><b>Device:</b>{{ meta.deviceLabel }}</h6>
        <h6><b>Type:</b>{{ port.type }}</h6>
        <h6><b>Represantation Id:</b>{{ port.repId }}</h6>
        <h6 v-if="port.connections"><b>Connections:</b>{{ port.connections }}</h6>
      </v-card-text>
    </v-card>
  </v-flex>
</template>

<script>
export default {
  name: 'PortMetadata',
  props: ['port', 'meta'],
  computed: {
    type(){
      return this.meta.type
    }
  }
}
</script>
