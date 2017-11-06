<template>
<v-content>
        <v-container fluid fill-height>
          <v-layout justify-center align-center>
            <!--<rhbreadcrumbs />-->
            <router-view></router-view>
            <v-snackbar
              :timeout="timeout"
              :bottom="y === 'bottom'"
              multi-line=True
              v-model="showDialog"
              >
                {{ message }}
              <v-btn flat color="pink" @click.native="closeSnackbar()">Close</v-btn>
            </v-snackbar>
            <!--
            <v-dialog v-model="showDialog" persistent>
                  <v-card>
                    <v-card-title class="headline">{{ dialogTitle }}</v-card-title>
                    <v-card-text>{{ message }}</v-card-text>
                    <v-card-actions>
                      <v-spacer></v-spacer>
                      <v-btn color="green darken-1" flat @click.native="removeError()">Ok</v-btn>
                    </v-card-actions>
                  </v-card>
                </v-dialog>
              -->
          </v-layout>
        </v-container>
      </v-content>
</template>

<script>
import RHBreadcrumbs from './Breadcrumbs.vue'

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
  data(){
    return {
      timeout: 6000,
    }
  },
  components: {
    'rhbreadcrumbs' : RHBreadcrumbs
  },
  methods: {
    removeError(){
      this.$store.dispatch('showDialog', false)
    },
    closeSnackbar(){
      this.$store.dispatch('showDialog', false)
    }
  }
}
</script>
