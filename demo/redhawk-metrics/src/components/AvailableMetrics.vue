<template>
  <div>
    <md-button class="md-primary md-raised" v-on:click="getAvailable">Available</md-button>
    <tree-view :data="available" :options="{maxDepth: 7, rootObjectKey: 'metrics'}"></tree-view>
  </div>
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
