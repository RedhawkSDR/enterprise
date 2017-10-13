<template>
<div class="modal-mask">
  <div class="modal-wrapper">
    <div class="create-eventchannel-container">
      <md-toolbar>
        <h1 class="md-title">Event Channel Creator</h1>
      </md-toolbar>
      <md-input-container>
        <label>Event Channel Name</label>
        <md-input v-model="eventChannelName" placeholder="Enter Event Channel Name"></md-input>
      </md-input-container>
      <div>
        <md-button class="md-raised md-warn" @click.native="cancel">Cancel</md-button>
        <md-button class="md-raised md-primary" @click.native="create" :disabled="disableCreate">Create</md-button>
      </div>
    </div>
  </div>
</div>
</template>

<script>
export default{
  name: 'eventchannelcreator',
  data(){
    return {
      eventChannelName: null,
      disableCreate: true
    }
  },
  methods: {
    cancel(){
      this.$store.dispatch('showEventChannelModal', false)
    },
    create(){
      console.log("Create Button hit "+this.eventChannelName)
      this.$store.dispatch('createEventChannel', this.eventChannelName)
      this.$store.dispatch('showEventChannelModal', false)
    }
  },
  watch: {
    eventChannelName: function(){
      if(this.eventChannelName==null || this.eventChannelName==''){
        this.disableCreate = true
      }else{
        this.disableCreate = false
      }
    }
  }
}
</script>


<style>
.create-eventchannel-container {
        width: 600px;
        background-color: #fff;
        margin: 0px auto;
}
</style>
