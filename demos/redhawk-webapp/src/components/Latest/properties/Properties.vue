<template>
<div class="card">
  <div class="card-header"
    :data-background="bgColor">
    <h4 class="title">Properties</h4>
    <p class="category">Properties for {{ id }}</p>
  </div>
  <div class="card-content">
    <md-table-card>
      <md-table>
        <md-table-body>
          <md-table-row
            v-for="(property, index) in properties"
            v-bind:key="property"
          >
            <md-table-cell v-if="property.type=='simple'">
              <simple :property="property" />
            </md-table-cell>
          </md-table-row>
        </md-table-body>
      </md-table>
      <!--TODO: Make pagination work properly -->
      <md-table-pagination
      md-size="5"
      :md-total="properties.length"
      md-page="1"
      md-separator="of"
      :md-page-options="[5, 10, 25, 50]"
      @pagination="onPagination"></md-table-pagination>
    </md-table-card>
  </div>
</div>
</template>

<script>
import SimplePropertyEditor from './components/SimpleEditor.vue'

export default {
  name: 'properties',
  props: [
    'bgColor',
    'id',
    'properties'
  ],
  components: {
    'simple' : SimplePropertyEditor
  }
}
</script>
