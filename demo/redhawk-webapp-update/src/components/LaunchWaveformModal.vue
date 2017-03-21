<template>
<div class="modal-mask">
  <div class="modal-wrapper">
    <div class="launch-modal-container">
      <md-toolbar>Waveform Launcher</md-toolbar>
      <md-input-container>
        <label>Id</label>
        <md-input v-model="id" :disabled="true"></md-input>
      </md-input-container>
      <md-input-container>
        <label>Name</label>
        <md-input v-model="name" :disabled="true"></md-input>
      </md-input-container>
      <md-input-container>
        <label>Software Assembly Locations</label>
        <md-input v-model="sadLocation" :disabled="true"></md-input>
      </md-input-container>
      <md-input-container>
        <label>Waveform Name</label>
        <md-input v-model="waveformName" placeholder="Enter waveform name"></md-input>
      </md-input-container>
      <div>
        <md-button class="md-raised md-warn" @click.native="cancel">Cancel</md-button>
        <md-button class="md-raised md-primary" @click.native="launch" :disabled="disableLaunch">Launch</md-button>
      </div>
    </div>
  </div>
</div>
</template>

<script>
export default{
  name: 'waveformlauncher',
  data(){
    return {
      waveformName: null,
      disableLaunch: true
    }
  },
  computed: {
    id(){
      return this.$store.getters.waveformToLaunch.id
    },
    name(){
      return this.$store.getters.waveformToLaunch.name
    },
    sadLocation(){
      return this.$store.getters.waveformToLaunch.sadLocation
    }
  },
  methods: {
    cancel(){
      this.$store.dispatch('closeLaunchWaveformModal')
    },
    launch(){
      var waveformToLaunch = new Object()
      waveformToLaunch.name = this.waveformName
      waveformToLaunch.id = this.waveformName
      waveformToLaunch.sadLocation = this.sadLocation
      this.$store.dispatch('launchWaveform', waveformToLaunch)
      this.$store.dispatch('closeLaunchWaveformModal')
    }
  },
  watch: {
      waveformName: function(){
        if(this.waveformName==null || this.waveformName==''){
          this.disableLaunch = true
        }else{
          this.disableLaunch = false
      }
    }
  }
}
</script>

<style>
.launch-modal-container {
        width: 600px;
        background-color: #fff;
        margin: 0px auto;
}
</style>
