<template>
<v-flex>
    <properties
      :id="domainName"
      :properties="properties"
      />
</v-flex>
<!--
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
            <md-table-cell v-if="property.type=='struct'">
              <struct :property="property" />
            </md-table-cell>
          </md-table-row>
        </md-table-body>
      </md-table>
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
-->
</template>

<script>
import Properties from '../properties/Properties.vue'

export default {
  name: 'domain',
  mounted(){
    this.$store.dispatch('selectDomainProperties', this.domainName)
  },
  computed: {
    domainName(){
      return this.$store.getters.domainName
    },
    properties(){
      return this.$store.getters.domainProperties
    }
  },
  components: {
    'properties' : Properties
  }
}
</script>
