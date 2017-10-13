<template>
  <div class="modal-mask">
    <div class="modal-wrapper">
      <div class="rest-container">
        <md-toolbar>
          <h1 class="md-title">REST Resource Config</h1>
        </md-toolbar>
        <p>Edit this configuration to change the underlying REDHAWK REST endpoint for the GUI to use.</p>
        <md-input-container>
          <label>REDHAWK REST Root</label>
          <md-input v-model="redhawkRESTRoot"></md-input>
        </md-input-container>
        <!--
        <md-input-container>
          <label>User Name</label>
          <md-input v-model="userName"></md-input>
        </md-input-container>
        <md-input-container>
          <label>Password</label>
          <md-input type="password" v-model="password"></md-input>
        </md-input-container>
        -->
        <div style="text-align: center">
          <md-button class="md-raised md-warn" @click.native="cancel">close</md-button>
          <!--<md-button class="md-raised" @click.native="TEST">TEST</md-button>-->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

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
    },
    userName: {
      get(){
        return this.$store.getters.userName
      }
    },
    password: {
      get(){
        return this.$store.getters.password
      }
    }
  },
  methods: {
    cancel(){
      this.$store.state.showEditRESTModal = false
    },
    TEST(){
      console.log("HELLO WORLD")
      //var workedurl = 'http://localhost:8181/rest/redhawk/localhost:2809/domains.json'
      var workedurl = 'http://localhost:8181/rest/redhawk/localhost:2809/domains.json'
      var url = 'http://127.0.0.1:8181/rest/redhawk/127.0.0.1:2809/domains/REDHAWK_DEV/devicemanagers.json'
      var AUTH = 'Basic '+btoa("redhawk:redhawk")
      console.log("Auth: "+AUTH)
      axios.get(workedurl)
      .then(function(response){
        console.log("YAY")
        console.log(response)
      })
      .catch(function(error){
        console.log("BOOOO")
        console.log(error)
      })
    }
  }
}
</script>


<style>
.rest-container {
  width: 400px;
  margin: 0px auto;
  background-color: #fff;
}
</style>
