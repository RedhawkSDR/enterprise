<template>
  <v-flex>
    <v-card>
      <v-toolbar card color="red" prominent>
        <v-toolbar-title class="body-2 black--text">{{ domainName }} Device Managers</v-toolbar-title>
      </v-toolbar>
      <v-divider></v-divider>
      <v-list>
        <template v-for="(item, index) in devicemanagers">
          <v-list-tile
          :to="{path:'/devicemanagers/'+item.label}"
          >
          <v-list-tile-content>
            <v-list-tile-title>{{ item.label }}</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
        <v-divider></v-divider>
      </template>
    </v-list>
  </v-card>
</v-flex>
</template>

<script>
export default {
  name: 'devicemanagers',
  mounted(){
    this.$store.dispatch('getDeviceManagersInDomain')
  },
  computed: {
    domainName(){
      return this.$store.getters.domainName
    },
    devicemanagers(){
      return this.$store.getters.devicemanagers
    }
  }
}
</script>
