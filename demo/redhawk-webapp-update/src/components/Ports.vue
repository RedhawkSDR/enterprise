<template>
  <div>
    <h1>Ports</h1>
    <md-list class="md-dense">
        <md-list-item
        v-for="(port, index) in ports"
        v-bind:index="index">
          {{port.name}}
          <md-button @click.native="plot">plot</md-button>
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
    }
  },
  methods: {
    plot(){
      console.log('Plot Me')
    }
  }
}
</script>
