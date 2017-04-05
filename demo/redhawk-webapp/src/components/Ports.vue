<template>
  <div>
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
  </div>
</template>

<script>
import axios from 'axios'

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
      this.$store.dispatch('plotPortData', port)
    }
  }
}
</script>
