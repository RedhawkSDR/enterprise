<template>
  <div>
    <md-button class="md-primary md-raised" v-on:click="getAvailable">Available</md-button>
    <div>
      <md-list>
      <md-list-item>
        <span> Applications </span>
        <md-list-expand>
          <md-list-item
          v-for="(application, index) in available.APPLICATION"
          v-bind:key="application"
          v-bind:index="index"
          v-bind:application="application">
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
    v-bind:device="device">
    {{ device }}
  </md-list-item>
</md-list-expand>
</md-list-item>
</md-list>
</div>
<div>
<md-list class="md-triple-line">
  <md-list-item>
  <span> Ports </span>
  <md-list-expand>
    <md-list-item
    v-for="(port, index) in available.PORT"
    v-bind:key="port"
    v-bind:index="index"
    v-bind:port="port">
    <div>
      <span>{{ port.PORT }}</span></br>
      <span>{{ port.COMPONENT }}</span>
      <span>{{ port.APP }}</span>
    </div>
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
    }
  },
  methods: {
    getAvailable: function(){
      console.log("Making it to available")
      this.$store.dispatch("getAvailableMetrics")
    }
  }
}
</script>
