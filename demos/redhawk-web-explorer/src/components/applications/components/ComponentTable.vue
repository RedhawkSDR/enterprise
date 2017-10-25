<template>
	<v-flex xs12>
		<v-card>
			<v-toolbar card color="red" prominent>
				<v-toolbar-title class="body-2 black--text">Components</v-toolbar-title>
			</v-toolbar>
			<v-divider></v-divider>
      <v-card-title>
				<v-spacer></v-spacer>
				<v-text-field
				append-icon="search"
				label="Search"
				single-line
				hide-details
				v-model="search"
				></v-text-field>
			</v-card-title>
      <v-data-table
			v-bind:headers="headers"
			v-bind:items="components"
			v-bind:search="search"
			v-bind:pagination.sync="pagination"
			>
			<template slot="items" scope="props">
        <td>
					<router-link :to="{ path: '/applications/'+applicationName+'/components/'+props.item.name }">{{ props.item.name }}</router-link>
				</td>
				<td class="text-xs-right">
            {{ props.item.started }}
        </td>
        <td>
          {{ props.item.processId }}
        </td>
        <td>
          {{ props.item.deviceIdentifier }}
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
  name: 'componenttable',
  props: ['components'],
	computed: {
    applicationName(){
      return this.$store.getters.applicationName
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
          text: 'Component Name',
					align: 'left',
					sortable: true,
					value: 'name'
        },
        {
          text: 'Started',
					align: 'left',
					sortable: true,
					value: 'name'
        },
        {
          text: 'Process Id',
					align: 'left',
					sortable: true,
					value: 'name'
        },
        {
          text: 'Device Manager',
					align: 'left',
					sortable: true,
					value: 'name'
        }
      ]
    }
  }
}
</script>
