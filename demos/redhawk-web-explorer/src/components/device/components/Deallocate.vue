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
            v-model="allocation_id_csv"
            disabled="true"
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
            v-model="centerFrequency"
            disabled="true"
            ></v-text-field>
            <v-text-field
            name="input-1"
            label="Bandwidth"
            v-model="bandwidth"
            disabled="true"
            ></v-text-field>
            <v-text-field
            name="input-1"
            label="Sample Rate(Msps)"
            v-model="sample_rate"
            disabled="true"
            ></v-text-field>
            <v-text-field
            name="input-1"
            label="Enabled"
            v-model="enabled"
            disabled="true"
            ></v-text-field>
            <v-text-field
            name="input-1"
            label="gain"
            v-model="gain"
            disabled="true"
            ></v-text-field>
          </v-flex>
        </v-layout>
      </v-card-text>
      <v-card-action>
        <v-btn @click="deallocate()">Deallocate</v-btn>
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
  name: 'deallocate',
  methods: {
    deallocate(){
      this.$store.dispatch('deallocate', this.allocation_id_csv)

      this.$router.go(-1)
    }
  },
  computed: {
    deviceLabel(){
      return this.$route.params.deviceLabel
    },
    tunerStatus(){
      return this.$store.getters.usedTuners[this.$route.query.index];
    },
    allocation_id_csv(){
      return this.tunerStatus['FRONTEND::tuner_status::allocation_id_csv']
    },
    bandwidth(){
      return this.tunerStatus['FRONTEND::tuner_status::bandwidth']
    },
    centerFrequency(){
      return this.tunerStatus['FRONTEND::tuner_status::center_frequency']
    },
    enabled(){
      return this.tunerStatus['FRONTEND::tuner_status::enabled']
    },
    gain(){
      return this.tunerStatus['FRONTEND::tuner_status::gain']
    },
    group_id(){
      return this.tunerStatus['FRONTEND::tuner_status::group_id']
    },
    rf_flow_id(){
      return this.tunerStatus['FRONTEND::tuner_status::rf_flow_id']
    },
    sample_rate(){
      return this.tunerStatus['FRONTEND::tuner_status::sample_rate']
    },
    tunerType(){
      return this.tunerStatus['FRONTEND::tuner_status::tuner_type']
    }
  }
}
</script>
