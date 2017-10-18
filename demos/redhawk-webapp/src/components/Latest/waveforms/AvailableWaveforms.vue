<template>
  <div class="content">
    <div class="card">
      <div class="card-header" data-background-color="red">
        <h4 class="title">Waveforms</h4>
        <p class="category">List of waveforms available in {{ domainName }} domain</p>
      </div>
      <div class="card-content">
          <md-list>
                <md-list-item
                  v-for="(waveform, index) in waveforms"
                  v-bind:key="waveform">
                  <router-link :to="{ path: '/waveforms/'+waveform.name, query: {index: index}}">{{ waveform.name }}</router-link>
                  <md-divider></md-divider>
                </md-list-item>
          </md-list>
      </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'waveforms',
  mounted(){
    this.$store.dispatch('getAvailableWaveforms')
  },
  computed: {
    domainName(){
      return this.$store.getters.domainName
    },
    waveforms(){
      return this.$store.getters.availableWaveforms
    }
  }
}
</script>
