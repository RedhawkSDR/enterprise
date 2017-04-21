<template>
<div class="modal-mask">
  <div class="modal-wrapper">
    <div class="allocation-modal-container">
      <md-toolbar>
        <h1 class="md-title">Allocate Device</h1>
      </md-toolbar>
      <md-input-container>
        <label>Allocation Id</label>
        <md-input v-model="allocation.id"></md-input>
      </md-input-container>
      <md-input-container>
        <label>Tuner Type</label>
        <md-input v-model="allocation.tunerType" :disabled="true">{{ allocation.tunerType }}</md-input>
      </md-input-container>
      <md-input-container>
        <label>Center Frequency (MHz)</label>
        <md-input v-model="allocation.centerFrequency"></md-input>
      </md-input-container>
      <md-input-container>
        <label>Bandwidth (MHz)</label>
        <md-input v-model="allocation.bandwidth"></md-input>
      </md-input-container>
      <md-input-container>
        <label>Sample Rate (Msps)</label>
        <md-input v-model="allocation.samplerate"></md-input>
      </md-input-container>
      <md-input-container>
        <label>Bandwidth Tolerance</label>
        <md-input v-model="allocation.bandwidthTolerance"></md-input>
      </md-input-container>
      <md-input-container>
        <label>Sample Rate Tolerance</label>
        <md-input v-model="allocation.sampleRateTolerance"></md-input>
      </md-input-container>
      <div style="text-align: center">
        <md-button class="md-raised md-warn" @click.native="cancel">Cancel</md-button>
        <!--<md-button class="md-raised md-primary" @click.native="allocate" :disabled="disableAllocate">Allocate</md-button>TODO: Make this work-->
        <md-button class="md-raised md-primary" @click.native="allocate">Allocate</md-button>
      </div>
    </div>
  </div>
</div>
</template>

<script>
export default{
  name: 'allocation',
  data(){
    return {
      allocation: {
        id : null,
        tunerType : "RX_DIGITIZER", //TODO: Should be dynamic
        centerFrequency : null,
        bandwidth : null,
        samplerate : null,
        bandwidthTolerance : 20.0,
        sampleRateTolerance : 20.0
      },
      disableAllocate : true
    }
  },
  methods: {
    cancel(){
      this.$store.dispatch('showAllocationModal', false)
    },
    allocate(){
      this.$store.dispatch('allocate', this.allocation)

      this.$store.dispatch('showAllocationModal', false)
    }
  },
  watch: {
    allocation: function(){
      console.log("Watching u")
      if(this.allocation.id!=null && this.allocation.centerFrequency!=null && (this.allocation.bandwith!=null || this.allocation.samplerate!=null)){
        this.disableAllocate = false
      }else{
        this.disableAllocate = true
      }
    }
  }
}
</script>

<style>
.allocation-modal-container {
        width: 600px;
        background-color: #fff;
        margin: 0px auto;
}
</style>
