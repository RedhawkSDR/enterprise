<template>
  <div class="content">
    <div class="card">
      <div class="card-header" data-background-color="red">
        <h4 class="title">Launch Waveform</h4>
        <p class="category">Launch selected waveform</p>
      </div>
      <div class="card-content">
        <form>
          <md-input-container>
            <label>Id</label>
            <md-input v-model="waveform.id" :disabled="true"></md-input>
          </md-input-container>
          <md-input-container>
            <label>Name</label>
            <md-input v-model="waveform.name" :disabled="true"></md-input>
          </md-input-container>
          <md-input-container>
            <label>Software Assembly Location</label>
            <md-input v-model="waveform.sadLocation" :disabled="true"></md-input>
          </md-input-container>
          <md-input-container>
            <label>Application Name</label>
            <md-input v-model="applicationName" placeholder="Enter application name"></md-input>
          </md-input-container>
        </form>
        <md-button class="md-raised md-primary" @click.native="launch" :disabled="disableLaunch">Launch</md-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'waveformlauncher',
  data(){
    return {
      applicationName: null,
      disableLaunch: true
    }
  },
  computed: {
    waveformname() {
      return this.$route.params.waveformname
    },
    index(){
      return this.$route.query.index
    },
    waveform(){
      return this.$store.getters.availableWaveforms[this.index]
    }
  },
  watch: {
      applicationName: function(){
        if(this.applicationName==null || this.applicationName==''){
          this.disableLaunch = true
        }else{
          this.disableLaunch = false
      }
    }
  },
  methods: {
    launch(){
      var waveformToLaunch = new Object()
      waveformToLaunch.name = this.applicationName
      waveformToLaunch.id = this.applicationName
      waveformToLaunch.sadLocation = this.waveform.sadLocation
      this.$store.dispatch('launchChoosenWaveform', waveformToLaunch)

      //TODO: On successful launch user should be redirected
    }
  }
}
</script>
