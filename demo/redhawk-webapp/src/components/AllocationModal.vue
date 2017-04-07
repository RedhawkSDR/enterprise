<template>
<div class="modal-mask">
  <div class="modal-wrapper">
    <div class="launch-modal-container">
      <md-toolbar>
        <h1 class="md-title">Allocate Device</h1>
      </md-toolbar>
      <md-input-container>
        <label>Id</label>
        <md-input v-model="id" :disabled="true"></md-input>
      </md-input-container>
      <md-input-container>
        <label>Name</label>
        <md-input v-model="name" :disabled="true"></md-input>
      </md-input-container>
      <md-input-container>
        <label>Software Assembly Location</label>
        <md-input v-model="sadLocation" :disabled="true"></md-input>
      </md-input-container>
      <md-input-container>
        <label>Waveform Name</label>
        <md-input v-model="waveformName" placeholder="Enter waveform name"></md-input>
      </md-input-container>
      <div>
        <md-button class="md-raised md-warn" @click.native="cancel">Cancel</md-button>
        <md-button class="md-raised md-primary" @click.native="allocate" :disabled="disableAllocate">Allocate</md-button>
      </div>
    </div>
  </div>
</div>
</template>

<script>
</script>
