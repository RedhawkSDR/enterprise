<template>
<div class="card">
  <div class="card-header" data-background-color="green">
    <h4 class="title">Ports</h4>
    <p class="category">Ports available in {{ device.label }}</p>
  </div>
  <div class="card-content">
    <md-table-card>
      <md-table>
        <md-table-header>
          <md-table-row>
            <md-table-head>Device Name</md-table-head>
            <md-table-head>Type</md-table-head>
            <md-table-head>Representation Id</md-table-head>
          </md-table-row>
        </md-table-header>
        <md-table-body>
          <md-table-row
            v-for="(port, index) in ports"
            v-bind:key="index"
          >
            <md-table-cell>
              <router-link :to="{ path: '/devicemanagers/'+devicemanagerLabel+'/devices/'+device.label+'/ports/'+port.name}">{{ port.name }}</router-link>
            </md-table-cell>
            <md-table-cell>
              {{ port.type }}
            </md-table-cell>
            <md-table-cell>
              {{ port.repId }}
            </md-table-cell>
          </md-table-row>
        </md-table-body>
      </md-table>
      <!--TODO: Make pagination work properly -->
      <md-table-pagination
      md-size="5"
      :md-total="ports.length"
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
  name: 'deviceports',
  props: ['device'],
  computed:{
    ports(){
      return this.device.ports
    },
    applicationName(){
      return this.$route.params.devicemanagerLabel
    }
  }
}
</script>
