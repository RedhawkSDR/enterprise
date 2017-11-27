<template>
  <v-app id="inspire" dark>
    <v-navigation-drawer
      clipped
      persistent
      v-model="drawer"
      enable-resize-watcher
      app
    >
      <v-list dense>
        <!--
        <v-list-tile @click="">
          <v-list-tile-action>
            <v-icon>dashboard</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>Dashboard</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
        -->
        <v-list-tile
        :to="'/settings'">
          <v-list-tile-action>
            <v-icon>settings</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>Settings</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
        <v-list-tile
        :to="'/domain'"
        >
          <v-list-tile-action>
            <v-icon>domain</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>Domain</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
        <!--
        <v-list-tile
        :to="'/waveforms'">
          <v-list-tile-action>
            <v-icon>list</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>Waveforms</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
        -->
        <v-list-tile
        :to="'/applications'">
          <v-list-tile-action>
            <v-icon>list</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>Applications</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
        <v-list-tile
        :to="'/devicemanagers'">
          <v-list-tile-action>
            <v-icon>list</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>Device Managers</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
        <v-list-tile
        :to="'/eventchannels'">
          <v-list-tile-action>
            <v-icon>list</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>Event Channels</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
      </v-list>
    </v-navigation-drawer>
    <v-toolbar app fixed clipped-left>
      <v-toolbar-side-icon @click.stop="drawer = !drawer"></v-toolbar-side-icon>
      <v-toolbar-title>REDHAWK Web Explorer</v-toolbar-title>
      <v-spacer/>
      <img src="static/RedHawk_Logo.png"
        style="width:50px;height:50px; float: left;" />
    </v-toolbar>
    <main>
      <explorercontent />
    </main>
    <!--
    <v-footer app fixed>
      <span>&copy; 2017</span>
    </v-footer>
    -->
  </v-app>
</template>

<script>
  import Content from './components/Content.vue'

  export default {
    data: () => ({
      drawer: true
    }),
    components: {
      'explorercontent' : Content
    },
    props: {
      source: String
    }
  }
</script>
