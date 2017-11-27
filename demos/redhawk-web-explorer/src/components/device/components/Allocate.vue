<template>
  <v-flex>
    <v-card>
      <v-toolbar card color="red" prominent>
        <v-toolbar-title class="body-2 black--text">{{ deviceLabel }} Allocation</v-toolbar-title>
      </v-toolbar>
      <v-divider></v-divider>
      <v-card-text>
        <v-layout row>
          <v-flex>
            <v-text-field
            name="input-1"
            label="Id"
            v-model="allocation.id"
            ></v-text-field>
            <v-text-field
            name="input-1"
            label="Tuner Type"
            v-model="tunerType"
            disabled
            ></v-text-field>
            <v-text-field
            name="input-1"
            label="Center Frequency"
            v-model="allocation.centerFrequency"
            ></v-text-field>
            <v-text-field
            name="input-1"
            label="Bandwidth"
            v-model="allocation.bandwidth"
            ></v-text-field>
            <v-text-field
            name="input-1"
            label="Sample Rate(Msps)"
            v-model="allocation.samplerate"
            ></v-text-field>
            <v-text-field
            name="input-1"
            label="Sample Rate Tolerance(%)"
            v-model="allocation.sampleRateTolerance"
            ></v-text-field>
            <v-text-field
            name="input-1"
            label="Bandwidth Tolerance(%)"
            v-model="allocation.bandwidthTolerance"
            ></v-text-field>
          </v-flex>
        </v-layout>
      </v-card-text>
      <v-card-action>
        <v-btn @click="allocate()" :disabled="disableAllocate">Allocate</v-btn>
      </v-card-action>
    </v-card>
  </v-flex>
</template>

<script>
function notNullAndNotEmpty(obj){
  if(obj!=null && obj.length>0){
    return true
  }

  return false
}

export default {
  name: 'allocate',
  data(){
    return {
      allocation: {
        id: null,
        tunerType: null,
        centerFrequency: null,
        bandwidth: null,
        samplerate: null,
        bandwidthTolerance: 20.0,
        sampleRateTolerance : 20.0
      },
      disableAllocate: true
    }
  },
  methods: {
    isAllocateEnabled(){
      if(notNullAndNotEmpty(this.allocation.id) && notNullAndNotEmpty(this.allocation.centerFrequency) && notNullAndNotEmpty(this.allocation.bandwidth)){
        this.disableAllocate = false
      }else if(notNullAndNotEmpty(this.allocation.id) && notNullAndNotEmpty(this.allocation.centerFrequency) && notNullAndNotEmpty(this.allocation.samplerate)){
        this.disableAllocate = false
      }else{
        this.disableAllocate = true
      }
    },
    allocate(){
      this.allocation.tunerType = this.tunerType

      this.$store.dispatch('allocate', this.allocation)

      this.$router.go(-1)
    }
  },
  watch: {
    'allocation.id' : function(){
      this.isAllocateEnabled();
    },
    'allocation.centerFrequency' : function(){
      this.isAllocateEnabled();
    },
    'allocation.bandwidth' : function(){
      this.isAllocateEnabled();
    },
    'allocation.samplerate' : function(){
      this.isAllocateEnabled();
    }
  },
  computed: {
    deviceLabel(){
      return this.$route.params.deviceLabel
    },
    tunerInfo(){
      return this.$store.getters.unusedTuners[this.$route.query.index]
    },
    tunerType(){
        return this.tunerInfo['FRONTEND::tuner_status::tuner_type']
    }
  }
}
</script>
