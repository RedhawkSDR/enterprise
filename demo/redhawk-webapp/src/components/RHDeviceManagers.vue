<template>
<md-list>
  <md-list-item>
    <span>Device Managers</span>
    <md-list-expand>
      <md-list-item
        class="md-inset"
        v-for="(devicemanager, index) in devicemanagers"
        v-bind:key="devicemanager"
        v-bind:index="index"
        v-bind:devicemanager="devicemanager">
        <span>{{ devicemanager.label }}</span>
        <md-menu md-direction="bottom left">
          <md-button md-menu-trigger>
            <md-icon>menu</md-icon>
          </md-button>
          <md-menu-content>
            <md-menu-item>Shutdown</md-menu-item>
            <md-menu-item @click.native="showDeviceManager(index)">View</md-menu-item>
          </md-menu-content>
        </md-menu>
        <md-divider></md-divider>
      </md-list-item>
    </md-list-expand>
  </md-list-item>
</md-list>
</template>

<script>
export default {
  name: 'rhdevicemanagers',
  computed: {
    devicemanagers(){
      return this.$store.getters.devicemanagers
    }
  },
  methods: {
    showDeviceManager(index){
      console.log('Show device manager at index '+index)
      var dmRequest = new Object()
      dmRequest.show = true
      dmRequest.index = index

      //Can only show one thing at a time so close the application if open
      this.$store.dispatch('showApplication', false)
      this.$store.dispatch('showDeviceManager', dmRequest)
    }
  }
}
</script>
