<template>
<td>
  <v-edit-dialog
            lazy
          > {{ property }}
            <v-text-field
              slot="input"
              label="Edit"
              v-model="property.name"
              single-line
              counter
              :rules="[max25chars]"
            ></v-text-field>
          </v-edit-dialog>
</td>
</template>

<script>
export default {
  name: 'simpleprop',
  props: ['property'],
  data(){

  }
}
</script>
