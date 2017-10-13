<template>
<md-layout>
    <md-list class="md-dense">
        <md-subheader>{{ componentName }} :: Ports</md-subheader>
        <md-list-item
        v-for="(port, index) in ports"
        v-bind:key=port
        v-bind:index="index"
        >
          {{port.name}}
          <md-button v-if="port.name.endsWith('out')" @click.native="plot(port)">plot</md-button>
        </md-list-item>
    </md-list>
</md-layout>
</template>

<script>
export default{
  name: 'componentports',
  computed: {
    ports(){
      return this.$store.getters.componentPorts
    },
    componentName(){
      return this.$store.getters.portsComponentName
    }
  },
  methods: {
    plot(port){
      var obj = new Object()
      obj.portType = 'component'
      obj.port = port
      this.$store.dispatch('plotPortData', obj)
    }
  }
}
</script>
