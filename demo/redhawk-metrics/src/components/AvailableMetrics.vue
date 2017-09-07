<template>
  <div :class="sidebar">
    <md-toolbar>
      REDAHAWK Metrics
      <md-button @click="getAvailable" class="md-icon-button md-raised">
        <md-icon>
          refresh
        </md-icon>
      </md-button>
    </md-toolbar>
    <div>
      <md-list>
        <md-list-item>
          <span> Applications </span>
          <md-list-expand>
            <md-list-item
            v-for="(application, index) in available.APPLICATION"
            v-bind:key="application"
            v-bind:index="index"
            v-bind:application="application"
            @click="showApplication(application)">
            {{ application }}
          </md-list-item>
        </md-list-expand>
      </md-list-item>
    </md-list>
  </div>
  <div>
    <md-list>
      <md-list-item>
        <span> Devices </span>
        <md-list-expand>
          <md-list-item
          v-for="(device, index) in available.GPP"
          v-bind:key="device"
          v-bind:index="index"
          v-bind:device="device"
          @click="showGPP(device)">
          {{ device }}
        </md-list-item>
      </md-list-expand>
    </md-list-item>
  </md-list>
</div>
<div>
  <md-list class="custom-list md-triple-line">
    <md-list-item>
      <span> Ports </span>
      <md-list-expand>
        <md-list-item
        v-for="(port, index) in available.PORT"
        v-bind:key="port"
        v-bind:index="index"
        v-bind:port="port"
        @click="showPort(port)">
        <div class="md-list-text-container">
          <span><b>Port:</b> {{ port.PORT }}</span>
          <span><b>Comp:</b> {{ port.COMPONENT }}</span>
          <span><b>App:</b> {{ port.APP }}</span>
        </div>
        <md-divider></md-divider>
      </md-list-item>
    </md-list-expand>
  </md-list-item>
</md-list>
</div>
</div>
<!--
<div>
<md-button class="md-primary md-raised" v-on:click="getAvailable">Available</md-button>
<tree-view :data="available" :options="{maxDepth: 7, rootObjectKey: 'metrics'}"></tree-view>
</div>
-->
</template>

<script>
export default {
  name: 'availablemetrics',
  computed: {
    available(){
      return this.$store.getters.available
    },
    port(){

    }
  },
  methods: {
    getAvailable: function(){
      console.log("Making it to available")
      this.$store.dispatch("getAvailableMetrics")
    },
    showApplication(application){
      this.$store.dispatch('appMetricsView', application)
    },
    showGPP(device){
      this.$store.dispatch('gppMetricsView', device)
    },
    showPort(port){
      this.$store.dispatch('portMetricsView', port)
    }
  }
}
</script>
