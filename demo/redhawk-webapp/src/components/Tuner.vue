<template>
<md-list-item>
  <span v-if="type=='USED'">
    {{ tuner["FRONTEND::tuner_status::tuner_type"] }} : {{ tuner["FRONTEND::tuner_status::allocation_id_csv"]}}
    <md-button @click.native="deallocate">
      DEALLOCATE
    </md-button>
  </span>
  <span v-else>
    {{ tuner["FRONTEND::tuner_status::tuner_type"] }}
    <md-button @click.native="allocate">
      ALLOCATE
    </md-button>
  </span>
</md-list-item>
</template>

<script>
export default{
  name: 'tuner',
  props: ['tuner', 'deviceLabel'],
  computed: {
    type(){
      if(this.tuner["FRONTEND::tuner_status::allocation_id_csv"].length > 0){
        return "USED"
      }else{
        return "UNUSED"
      }
    }
  },
  methods: {
    allocate(){
      console.log("Allocate")
      this.$store.dispatch("showAllocationModal", true)
    },
    deallocate(){
      var obj = new Object()
      obj.deviceLabel = this.deviceLabel
      obj.allocationId = this.tuner["FRONTEND::tuner_status::allocation_id_csv"]

      console.log(obj)
      this.$store.dispatch("deallocate", obj)
      var obj = new Object()
      obj.device = this.device
      obj.show = true
      this.$store.dispatch("showDeviceTuners", obj)
    }
  }
}
</script>
