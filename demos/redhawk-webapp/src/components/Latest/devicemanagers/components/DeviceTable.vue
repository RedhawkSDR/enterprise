<template>
<div class="card">
  <div class="card-header" data-backaground="green">
    <h4 class="title">Devices</h4>
    <p class="category">Devices managed by devicemanager {{ devicemanager.label }}</p>
  </div>
  <div class="card-content">
    <md-table-card>
      <md-table>
        <md-table-header>
          <md-table-row>
            <md-table-head>Device Label</md-table-head>
            <md-table-head>Started</md-table-head>
            <md-table-head>Usage State</md-table-head>
            <md-table-head>Operational State</md-table-head>
          </md-table-row>
        </md-table-header>
        <md-table-body>
          <md-table-row
            v-for="(device, index) in devicemanager.devices"
            v-bind:key="device"
          >
            <md-table-cell>
              <router-link :to="{ path: '/devicemanagers/'+devicemanager.label+'/devices/'+device.label }">{{ device.label }}</router-link>
            </md-table-cell>
            <md-table-cell>
              {{ device.started }}
            </md-table-cell>
            <md-table-cell>
              {{ device.usageState }}
            </md-table-cell>
            <md-table-cell>
              {{ device.operationState }}
            </md-table-cell>
          </md-table-row>
        </md-table-body>
      </md-table>
      <!--TODO: Make pagination work properly -->
      <md-table-pagination
      md-size="5"
      :md-total="devices.length"
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
  name: 'devicetable',
  props: ['devices'],
  computed: {
    devicemanager(){
      return this.$store.getters.devicemanager
    }
  }
}
</script>
