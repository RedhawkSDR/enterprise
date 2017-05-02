<template>
<md-list class="md-dense">
  <div id="tuneHeader">
    <md-button @click.native="close" class="md-icon-button">
      <md-icon>close</md-icon>
    </md-button>
    <h2>{{ device.label }} : Tuners</h2>
  </div>
  <md-divider></md-divider>
  <md-subheader>
    Unused Tuners
  </md-subheader>
  <md-list-item v-for="(tuner, index) in tuners.unusedTuners"
  @click.native="allocate">
      {{ tuner["FRONTEND::tuner_status::tuner_type"] }}
      <md-button>
        ALLOCATE
      </md-button>
  </md-list-item>
  <md-divider></md-divider>
  <md-subheader>
    Used Tuners
  </md-subheader>
  <md-list-item v-for="(tuner, index) in tuners.usedTuners"
    v-bind:tuner="tuner"
    @click.native="deallocate(tuner)">
      {{ tuner["FRONTEND::tuner_status::tuner_type"] }} : {{ tuner["FRONTEND::tuner_status::allocation_id_csv"]}}
    <md-button>
      DEALLOCATE
    </md-button>
  </md-list-item>
</md-list>
</template>

<script>
//import Tuner from './Tuner.vue'

export default{
  name: 'tuners',
  computed: {
    tuners(){
      return this.$store.getters.tuners
    },
    usedTuners(){
      return this.$store.getters.tuners.usedTuners
    },
    unusedTuners(){
      return this.$store.getters.tuners.unusedTuners
    },
    device(){
      return this.tuners.device
    }
  },
  methods: {
    close(){
      var t = new Object();
      t.show = false
      this.$store.dispatch("showDeviceTuners", t)
    },
    allocate(){
      console.log("Allocate")
      this.$store.dispatch("showAllocationModal", true)
      //this.$forceUpdate()
    },
    deallocate(tuner){
      console.log("In deallocate")
      console.log("Tuner is "+this.tuner)
      //console.log(tuner)
      var obj = new Object()
      obj.deviceLabel = this.device.label
      obj.allocationId = tuner["FRONTEND::tuner_status::allocation_id_csv"]

      console.log(obj)
      this.$store.dispatch("deallocate", obj)
      //var t = new Object()
      //t.show = true
      //t.device = new Object()
      //t.device.label = this.device.label
      //console.log(t)
      //this.$store.dispatch("showDeviceTuners", t)
      //this.$forceUpdate()
      //this.$store.dispatch("updateTuners", this.deviceLabel)
    },
    updated(){
      console.log("Received an update")
    }
  }
}
</script>

<style>
#tuneHeader {
    display : flex;
}
</style>
