<template>
  <div class="modal-mask">
    <div class="modal-wrapper">
      <div class="modal-container">
        <md-toolbar>
          <h1 class="md-title">REST Resource Config</h1>
        </md-toolbar>
        <p>Edit this configuration to change the underlying REDHAWK REST endpoint for the GUI to use.</p>
        <md-input-container>
          <label>REDHAWK REST Root</label>
          <md-input v-model="redhawkRESTRoot"></md-input>
        </md-input-container>
        <div>
          <md-button class="md-raised md-warn" @click.native="cancel">close</md-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default{
  name: 'restconfig',
  computed: {
    redhawkRESTRoot: {
      get(){
        return this.$store.getters.redhawkRESTRoot
      },
      set(value){
        this.$store.dispatch('updateRedhawkRESTRoot', value)
      }
    }
  },
  methods: {
    cancel(){
      this.$store.state.showEditRESTModal = false
    }
  }
}
</script>


<style>
.modal-container {
  width: 400px;
  margin: 0px auto;
  background-color: #fff;
}
</style>
