<template>
<v-content>
        <v-container fluid fill-height>
          <v-layout justify-center align-center>
            <router-view></router-view>
            <v-dialog v-model="showDialog" persistent>
                  <!--<v-btn color="primary" dark slot="activator">Open Dialog</v-btn>-->
                  <v-card>
                    <v-card-title class="headline">{{ dialogTitle }}</v-card-title>
                    <v-card-text>{{ message }}</v-card-text>
                    <v-card-actions>
                      <v-spacer></v-spacer>
                      <v-btn color="green darken-1" flat @click.native="removeError()">Ok</v-btn>
                    </v-card-actions>
                  </v-card>
                </v-dialog>
          </v-layout>
        </v-container>
      </v-content>
</template>

<script>
export default {
	name: 'content',
  computed: {
    showDialog(){
      return this.$store.getters.showDialog
    },
    message(){
      return this.$store.getters.dialogMessage
    },
    dialogTitle(){
      return this.$store.getters.dialogTitle
    }
  },
  methods: {
    removeError(){
      this.$store.dispatch('showDialog', false)
    }
  }
}
</script>
