<template>
<div>
  <div class="metricslist">
      <md-toolbar class="logo md-warn">
        <h2 class="md-title">Ports</h2>
      </md-toolbar>
      <md-list>
        <md-list-item
        v-for="(port, index) in availablePortStats"
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
      </md-list>
  </div>
</div>
<!--
<div>
<h1>Port: {{ name }}</h1>
<h2>Comp: {{ componentName }}</h2>
<h3>App: {{ appName }}</h3>
</div>
-->
</template>

<script>
export default {
  name: 'portstats',
  computed: {
    availablePortStats(){
      return this.$store.getters.available.PORT
    },
    name(){
      return this.$store.getters.portName
    },
    appName(){
      return this.$store.getters.portApplicationName
    },
    componentName(){
      return this.$store.getters.portComponentName
    },
    statistics(){
      return this.$store.getters.portStatistics
    }
  }
}
</script>

<style>
</style>
