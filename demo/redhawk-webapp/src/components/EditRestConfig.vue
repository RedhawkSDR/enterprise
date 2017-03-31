<template>
  <div class="modal-mask">
    <div class="modal-wrapper">
      <div class="modal-container">
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
