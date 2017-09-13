<template>
  <div class="wrapper">
    <!-- Sidebar from template -->
    <sidebar/>
    <div class="main-panel">
      <!--<topnavbar></topnavbar>-->
      <dashboardcontent/>
    </div>
  </div>
</template>

<script>
import Sidebar from './components/Sidebar.vue'
import Content from './components/Content.vue'
import TopNavBar from './components/TopNavBar'

export default {
  name: 'redhawkmetrics',
  components: {
    'sidebar' : Sidebar,
    'dashboardcontent' : Content,
    'topnavbar' : TopNavBar
  },
  mounted(){
    this.$store.dispatch("getAvailableMetrics", 'application')
  },
  computed: {
    showAppMetrics(){
      return this.$store.getters.showAppMetrics
    },
    showGPPMetrics(){
      return this.$store.getters.showGPPMetrics
    },
    showPortStats(){
      return this.$store.getters.showPortStats
    },
    showConfiguration(){
      return this.$store.getters.showConfiguration
    }
  }
}
</script>


<style>
</style>
