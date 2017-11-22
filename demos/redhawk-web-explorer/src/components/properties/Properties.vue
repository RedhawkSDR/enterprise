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
				<tr v-if="props.item.type=='simple'">
					<td>
						<div v-if="props.item.name">{{props.item.name}}</div>
						<div v-else>{{props.item.id}}</div>
					</td>
					<td class="text-xs-right">
						<v-select
							v-if="props.item.type=='simple' && 'enumerations' in props.item"
							v-bind:items="props.item.enumerations.enumerations"
							v-model="props.item.value"
							label="Select"
							@input="updateSimple(props.item)"
							dark
							item-text="label"
							item-value="value"
						></v-select>
						<v-edit-dialog
						v-else-if="props.item.type=='simple'"
						lazy
						> {{ props.item.value }}
						<v-text-field
						slot="input"
						label="Edit"
						v-model="props.item.value"
						single-line
						counter
						:disabled="isPropertyDisabled(props.item)"
						:rules="[max25chars]"
						@change="updateSimple(props.item)"
						></v-text-field>
					</v-edit-dialog>
					</td>
				</tr>
				<tr v-else-if="props.item.type=='struct'"
					@click="props.expanded = !props.expanded">
					<td>
						<div v-if="props.item.name">{{ props.item.name }}</div>
						<div v-else>{{ props.item.id }}</div>
					</td>
					<td/>
				</tr>
				<tr v-else>
					<td>
						<div v-if="props.item.name">{{ props.item.name }}</div>
						<div v-else>{{ props.item.id }}</div>
					</td>
					<td/>
				</tr>
	</template>
	<template slot="expand" scope="props">
	<v-card flat>
		<v-card-text>
			<v-text-field v-for="(attr, index) in props.item.attributes"
			slot="input"
			:prefix="attr.id"
			v-model="attr.value"
			single-line
			counter
			:disabled="isPropertyDisabled(props.item)"
			:rules="[max25chars]"
			@change="updateStruct(props.item)"
			></v-text-field>
		</v-card-text>
	</v-card>
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
	props: ['id', 'properties', 'type'],
	components: {
		'simple' : SimpleEditor
	},
	methods: {
		isPropertyDisabled(prop){
			if(prop.mode=='READONLY'){
				return true
			}else if(!this.isKindConfigurable(prop.kinds)){
				return true
			}else{
				return false
			}
		},
		isKindConfigurable(kinds){
			console.log(kinds)
			if(kinds != undefined){
				for(var i = 0; i < kinds.length; i++){
					if(kinds[i].kindtype=='CONFIGURE')
						return true;
				}
			}
			return false
		},
		updateSimple(simple){
			var obj = new Object()
			obj.property = simple
			obj.type = this.type
			console.log(simple)
			this.$store.dispatch('updateProperty', obj)
		},
		updateStruct(struct){
			console.log("STRUCTS!!!!")
			console.log(struct)
		}
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
