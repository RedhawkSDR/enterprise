<template>
  <v-container grid-list-md text-xs-center>
    <v-layout row wrap>
      <v-flex xs12>
       <v-breadcrumbs icons divider="forward">
         <v-breadcrumbs-item
           v-for="item in items" :key="item.text"
           :disabled="item.disabled"
         >
           {{ item.text }}
         </v-breadcrumbs-item>
       </v-breadcrumbs>
     </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
export default {
  name: 'rhexplorerbreadcrumbs',
  data(){
    return {
      items: [
        {
          text: 'hello'
        },
        {
          text: 'world'
        }
      ]
    }
  }
}
</script>
