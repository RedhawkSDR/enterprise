<template>
	<v-flex xs12>
		<v-card>
			<v-toolbar card color="red" prominent>
				<v-toolbar-title class="body-2 black--text">Devices</v-toolbar-title>
			</v-toolbar>
			<v-divider></v-divider>
      <v-data-table
			v-bind:headers="headers"
			v-bind:items="devices"
			v-bind:pagination.sync="pagination"
			>
			<template slot="items" scope="props">
        <td>
					<router-link :to="{ path: '/devicemanagers/'+devicemanagerlabel+'/devices/'+props.item.label }">{{ props.item.label }}</router-link>
				</td>
				<td class="text-xs-right">
            {{ props.item.started }}
        </td>
        <td>
          {{ props.item.usageState }}
        </td>
        <td>
          {{ props.item.operationState }}
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
  name: 'devicetable',
  props: ['devices'],
  computed: {
    devicemanagerlabel(){
      return this.$route.params.devicemanagerLabel
    },
    devicemanager(){
      return this.$store.getters.devicemanager
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
          text: 'Device Label',
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
          text: 'Usage State',
					align: 'left',
					sortable: true,
					value: 'name'
        },
        {
          text: 'Operation State',
					align: 'left',
					sortable: true,
					value: 'name'
        }
      ]
    }
  }
}
</script>
