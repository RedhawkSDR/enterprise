<template>
<md-list class="md-dense">
  <div id="tuneHeader">
    <md-button @click.native="close" class="md-icon-button">
      <md-icon>close</md-icon>
    </md-button>
    <h2>{{ device.label }} : Tuners</h2>
  </div>
  <md-divider></md-divider>
  <md-subheader>
    Unused Tuners
  </md-subheader>
  <md-list>
    <tuner v-for="(tuner, index) in unusedTuners"
      v-bind:tuner="tuner"
      v-bind:deviceLabel="device.label"
      >
    </tuner>
  </md-list>
  <md-subheader>
    Used Tuners
  </md-subheader>
  <md-list>
    <tuner
      v-for="(tuner, index) in usedTuners"
      v-bind:tuner="tuner"
      v-bind:deviceLabel="device.label"
      >
    </tuner>
  </md-list>
</md-list>
</template>

<script>
import Tuner from './Tuner.vue'

export default{
  name: 'tuners',
  computed: {
    usedTuners(){
      return this.$store.getters.tuners.usedTuners
    },
    unusedTuners(){
      return this.$store.getters.tuners.unusedTuners
    },
    device(){
      return this.$store.getters.tuners.device
    }
  },
  methods: {
    close(){
      var t = new Object();
      t.show = false
      this.$store.dispatch("showDeviceTuners", t)
    }
  },
  components: {
    'tuner' : Tuner
  }
}
</script>

<style>
#tuneHeader {
    display : flex;
}
</style>
