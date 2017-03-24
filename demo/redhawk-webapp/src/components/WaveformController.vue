<template>
  <div class="modal-mask">
          <div class="modal-wrapper">
                  <div class="control-modal-container">
                          <md-toolbar>
                                  <h1 class="md-title md-dense">Control Waveform</h1>
                          </md-toolbar>
                          <div class="waveform-info">
                          <md-input-container>
                            <label>Id</label>
                            <md-input v-model="identifier" :disabled="true"></md-input>
                          </md-input-container>
                          <md-input-container>
                            <label>Name</label>
                            <md-input v-model="name" :disabled="true"></md-input>
                          </md-input-container>
                          <md-input-container>
                            <label>Started</label>
                            <md-input v-model="started" :disabled="true"></md-input>
                          </md-input-container>
                          </div>
                          <div class="control-buttons">
                                  <md-button @click.native="control('start')" :disabled="started">Start</md-button>
                                  <md-button @click.native="control('stop')" :disabled="!started">Stop</md-button>
                                  <md-button @click.native="release">Release</md-button>
                                  <md-button @click.native="cancel">Cancel</md-button>
                          </div>
                  </div>
          </div>
  </div>
</template>

<script>
import axios from 'axios'

export default{
  name: 'waveformcontroller',
  computed: {
    identifier(){
      return this.$store.getters.waveformToControl.identifier
    },
    name(){
      return this.$store.getters.waveformToControl.name
    },
    started() {
      return this.$store.getters.waveformToControl.started
    }
  },
  methods: {
    cancel(){
      this.$store.dispatch('closeWaveformController')
    },
    control(action){
        var control = new Object()
        control.action = action
        control.waveformName = this.name
        this.$store.dispatch('controlWaveform', control)
        this.$store.dispatch('closeWaveformController')
    },
    release(){
      this.$store.dispatch('releaseWaveform', this.name)

      //this.$store.dispatch('updateDomainStateAfterWaveformRelease', this.name)

      //Need to update app state based on this action.
      this.$store.dispatch('closeWaveformController')
    }
  }
}
</script>

<style>
.modal-mask {
position: fixed;
z-index: 9998;
top: 0;
left: 0;
width:
100%;
height: 100%;
background-color: rgba(0, 0, 0, .5);
display: table;
transition: opacity .3s ease;
}

.modal-wrapper {
display: table-cell;
vertical-align: middle;
}

.control-modal-container {
        width: 500px;
        background-color: #fff;
        margin: 0px auto;
}

.modal-container {
width: 300px;
margin: 0px
auto;
padding: 20px 30px;
background-color: #fff;
border-radius: 2px;
box-shadow: 0 2px 8px rgba(0, 0, 0, .33);
transition: all .3s ease;
font-family: Helvetica, Arial, sans-serif;
}
</style>
