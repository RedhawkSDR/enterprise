<template>
  <v-flex xs12>
    <v-card>
      <v-toolbar card color="red" prominent>
        <v-toolbar-title class="body-2 black--text">Ports</v-toolbar-title>
      </v-toolbar>
      <v-divider></v-divider>
      <v-data-table
      v-bind:headers="headers"
      v-bind:items="ports"
      v-bind:pagination.sync="pagination"
      >
      <template slot="items" scope="props">
        <td>
          <router-link :to="{ path: '/applications/'+applicationName+'/components/'+componentName+'/ports/'+props.item.name}">{{ props.item.name }}</router-link>
        </td>
        <td class="text-xs-right">
            {{ props.item.type }}
        </td>
        <td>
          {{ props.item.repId }}
        </td>
      </template>
      <template slot="pageText" scope="{ pageStart, pageStop }">
        From {{ pageStart }} to {{ pageStop }}
      </template>
    </v-data-table>
  </v-card>
  </v-flex>
</template>

<script>
export default {
  name: 'componentportstable',
  props: ['ports'],
  computed:{
    applicationName(){
      return this.$route.params.applicationName
    },
    componentName(){
      return this.$route.params.componentName
    }
  },
  data() {
    return{
      max25chars: (v) => v.length <= 25 || 'Input too long!',
      tmp: '',
      search: '',
      pagination: {},
      headers: [
        {
          text: 'Port Name',
					align: 'left',
					sortable: true,
					value: 'name'
        },
        {
          text: 'Type',
					align: 'left',
					sortable: true,
					value: 'name'
        },
        {
          text: 'Representation Id',
					align: 'left',
					sortable: true,
					value: 'name'
        }
      ]
    }
  }
}
</script>
