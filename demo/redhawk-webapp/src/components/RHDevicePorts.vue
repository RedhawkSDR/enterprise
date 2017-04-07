<template>
    <md-list class="md-dense">
        <!--<md-subheader> {{ device.label }} : Ports</md-subheader> TODO: Make this work -->
        <md-subheader> Ports</md-subheader> 
        <md-list-item
        v-for="(port, index) in ports"
        v-bind:key=port
        v-bind:index="index"
        >
          {{ port.name }}
          <md-button v-if="port.name.endsWith('out')" @click.native="plot(port)">plot</md-button>
        </md-list-item>
    </md-list>
</template>

<script>
export default{
  name: 'deviceports',
  computed: {
    device(){
        return this.$store.getters.devicePorts.device
    },
    ports(){
      return this.$store.getters.devicePorts.ports
    }
  }
}
</script>
