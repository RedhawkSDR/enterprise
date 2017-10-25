<template>
	<v-flex xs12>
		<v-card>
			<v-toolbar card color="red" prominent>
				<v-toolbar-title class="body-2 black--text">{{ id }} Properties</v-toolbar-title>
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
			v-bind:items="properties"
			v-bind:search="search"
			v-bind:pagination.sync="pagination"
			>
			<template slot="items" scope="props">
				<td>
              <div v-if="props.item.name">{{props.item.name}}</div>
              <div v-else>{{props.item.id}}</div>
				</td>
				<td class="text-xs-right">
            <div>{{ props.item.value }}</div>
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
  import SimpleEditor from './components/SimpleEditor'

	export default {
		name: 'properties',
		props: ['id', 'properties'],
    components: {
      'simple' : SimpleEditor
    },
		data(){
			return {
				max25chars: (v) => v.length <= 25 || 'Input too long!',
				tmp: '',
				search: '',
				pagination: {},
				headers: [
				{
					text: 'Name',
					align: 'left',
					sortable: true,
					value: 'name'
				},
				{
					text: 'Value',
					align: 'left',
					sortable: true,
					value: 'value'
				}
				]
			}
		}
	}
</script>
