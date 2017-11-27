<template>
  <v-flex xs12>
    <v-card>
      <v-toolbar card color="red" prominent>
        <v-toolbar-title class="body-2 black--text">{{ device.label }} Tuners</v-toolbar-title>
      </v-toolbar>
      <v-divider></v-divider>
      <v-list subheader>
          <v-subheader>Allocate</v-subheader>
            <v-list-tile
            :to="{path: '/devicemanagers/'+deviceManagerLabel+'/devices/'+device.label+'/allocate', query: {index : index}}"
            v-for="(tuner, index) in unusedTuners">
              <!--
              <v-list-tile-avatar>
              </v-list-tile-avatar>
              -->
              <v-list-tile-content>
                {{ tuner['FRONTEND::tuner_status::tuner_type'] }}
              </v-list-tile-content>
              <!--
              <v-list-tile-action>
              </v-list-tile-action>
              -->
            </v-list-tile>
        </v-list>
        <v-list subheader>
            <v-subheader>Deallocate</v-subheader>
              <v-list-tile
                :to="{path: '/devicemanagers/'+deviceManagerLabel+'/devices/'+device.label+'/deallocate', query: {index : index}}"
                v-for="(tuner, index) in usedTuners">
                <v-list-tile-content>
                  {{ tuner['FRONTEND::tuner_status::allocation_id_csv'] }}
                </v-list-tile-content>
              </v-list-tile>
          </v-list>
    </v-card>
  </v-flex>
</template>

<script>
export default {
  name: 'tuners',
  props: ['device'],
  mounted(){
    this.$store.dispatch('setDeviceTuners')
  },
  computed: {
    usedTuners(){
      return this.$store.getters.usedTuners
    },
    unusedTuners(){
      return this.$store.getters.unusedTuners
    },
    deviceManagerLabel(){
      return this.$route.params.devicemanagerLabel
    }
  },
  methods: {
    allocate(index){
      console.log("Allocate!!! ", index)
    }
  }
}
</script>
