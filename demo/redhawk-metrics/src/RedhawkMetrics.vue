<template>
  <div class="wrapper">
    <!-- Sidebar from template -->
    <sidebar/>
    <div class="main-panel">
      <dashboardcontent/>
      <!-- TODO: Make more like template -->
      <!--
      <domainconfig v-if="showConfiguration"></domainconfig>
      <userprofile v-if="false"></userprofile>
      <appmetrics v-if="showAppMetrics">
      </appmetrics>
      <gppmetrics v-if="showGPPMetrics">
      </gppmetrics>
      <portstats v-if="showPortStats">
      </portstats>
      -->
    </div>
    <!--
    <md-layout md-gutter>
      <md-layout md-flex="30">
        <availablemetrics/>
      </md-layout>
      <md-layout md-flex>
        <appmetrics v-if="showAppMetrics">
        </appmetrics>
        <gppmetrics v-if="showGPPMetrics">
        </gppmetrics>
        <portstats v-if="showPortStats">
        </portstats>
      </md-layout>
    </md-layout>
    -->
  </div>
</template>

<script>
import Sidebar from './components/Sidebar.vue'
import ApplicationMetrics from './components/ApplicationMetrics.vue'
import GPPMetrics from './components/GPPMetrics.vue'
import PortStats from './components/PortStatistics.vue'
import DomainConfig from './components/DomainConfig.vue'
import UserProfile from './components/UserProfile.vue'
import Content from './components/Content.vue'

export default {
  name: 'redhawkmetrics',
  components: {
    'sidebar' : Sidebar,
    'appmetrics' : ApplicationMetrics,
    'gppmetrics' : GPPMetrics,
    'portstats' : PortStats,
    'domainconfig' : DomainConfig,
    'userprofile' : UserProfile,
    'dashboardcontent' : Content
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
