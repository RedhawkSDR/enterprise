<template>
  <div class="content">
    <div class="card">
      <div class="card-header" data-background-color="red">
        <h4 class="title">GPPs</h4>
        <p class="category">List of GPPs with metrics available</p>
      </div>
      <div class="card-content">
        <md-list>
          <md-list-item
            v-for="(gpp, index) in availableGPPMetrics"
            v-bind:key="gpp"
            @click="showGPP(gpp)">
            {{ gpp }}
          </md-list-item>
        </md-list>
          </div>
      </div>
    </div>
</template>

<script>
export default {
  name: 'gppmetrics',
  computed: {
    name(){
      return this.$store.getters.gppName
    },
    metrics(){
      return this.$store.getters.gppMetrics
    },
    availableGPPMetrics(){
      return this.$store.getters.available.GPP
    }
  }
}
</script>

<style>
</style>
