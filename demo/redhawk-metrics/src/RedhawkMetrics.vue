<template>
  <div class="wrapper">
    <availablemetrics/>
    <div class="main-panel">
      <!-- TODO: Make more like template -->
      <domainconfig></domainconfig>
      <userprofile v-if="false"></userprofile>
      <appmetrics v-if="showAppMetrics">
      </appmetrics>
      <gppmetrics v-if="false">
      </gppmetrics>
      <portstats v-if="showPortStats">
      </portstats>
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
import AvailableMetrics from './components/AvailableMetrics.vue'
import ApplicationMetrics from './components/ApplicationMetrics.vue'
import GPPMetrics from './components/GPPMetrics.vue'
import PortStats from './components/PortStatistics.vue'
import DomainConfig from './components/DomainConfig.vue'
import UserProfile from './components/UserProfile.vue'

export default {
  name: 'redhawkmetrics',
  components: {
    'availablemetrics' : AvailableMetrics,
    'appmetrics' : ApplicationMetrics,
    'gppmetrics' : GPPMetrics,
    'portstats' : PortStats,
    'domainconfig' : DomainConfig,
    'userprofile' : UserProfile
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
    }
  }
}
</script>


<style>
</style>
