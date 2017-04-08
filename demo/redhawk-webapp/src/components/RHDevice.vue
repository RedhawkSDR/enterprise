<template>
  <md-list-item>
    {{ device.label }}
    <md-menu v-if="!gppDeviceFound" md-direction="top left">
      <md-button md-menu-trigger>
        <md-icon>menu</md-icon>
      </md-button>
      <md-menu-content>
        <md-menu-item @click.native="control()">Control</md-menu-item>
        <md-menu-item @click.native="showTuners()">Tuners</md-menu-item>
        <md-menu-item @click.native="showPorts()">Ports</md-menu-item>
        <md-menu-item @click.native="showProperties()">Properties</md-menu-item>
      </md-menu-content>
    </md-menu>
    <md-menu v-if="gppDeviceFound" md-direction="bottom left">
      <md-button md-menu-trigger>
        <md-icon>menu</md-icon>
      </md-button>
      <md-menu-content>
        <md-menu-item @click.native="control()">Control</md-menu-item>
        <md-menu-item @click.native="showProperties()">Properties</md-menu-item>
      </md-menu-content>
    </md-menu>
    <md-divider></md-divider>
  </md-list-item>
</template>

<script>
export default{
  name: 'rhdevice',
  props: ['device'],
  computed:{
    gppDeviceFound(){
      var deviceLabel = this.device.label
      if(deviceLabel.startsWith("GPP")){
        return true
      }else {
        return false
      }
    }
  },
  methods: {
    showTuners(){
      var obj = new Object()
      obj.device = this.device
      obj.show = true
      this.$store.dispatch("showDeviceTuners", obj)
    },
    showPorts(){
      var obj = new Object();
      obj.device = this.device
      obj.show = true
      this.$store.dispatch("showDevicePorts", obj)
    },
    showProperties(){
      var obj = new Object();
      obj.device = this.device
      obj.show = true
      this.$store.dispatch("showDeviceProperties", obj)
    }
  }
}
</script>
