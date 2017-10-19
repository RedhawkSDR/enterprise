<template>
<div class="card">
  <div class="card-header" data-background-color="green">
    <h4 class="title">Components</h4>
    <p class="category">Components in application {{ application.name }}</p>
  </div>
  <div class="card-content">
    <md-table-card>
      <md-table>
        <md-table-header>
          <md-table-row>
            <md-table-head>Component Name</md-table-head>
            <md-table-head>Started</md-table-head>
            <md-table-head>Process Id</md-table-head>
            <md-table-head>Device Identifier</md-table-head>
          </md-table-row>
        </md-table-header>
        <md-table-body>
          <md-table-row
            v-for="(component, index) in application.components"
            v-bind:key="component"
          >
            <md-table-cell>
              <router-link :to="{ path: '/applications/'+application.name+'/components/'+component.name }">{{ component.name }}</router-link>
            </md-table-cell>
            <md-table-cell>
              {{ component.started }}
            </md-table-cell>
            <md-table-cell>
              {{ component.processId }}
            </md-table-cell>
            <md-table-cell>
              {{ component.deviceIdentifier}}
            </md-table-cell>
          </md-table-row>
        </md-table-body>
      </md-table>
      <!--TODO: Make pagination work properly -->
      <md-table-pagination
      md-size="5"
      :md-total="application.components.length"
      md-page="1"
      md-separator="of"
      :md-page-options="[5, 10, 25, 50]"
      @pagination="onPagination"></md-table-pagination>
    </md-table-card>
  </div>
</div>
</template>

<script>
export default {
  name: 'componentList',
  props: ['application']
}
</script>
