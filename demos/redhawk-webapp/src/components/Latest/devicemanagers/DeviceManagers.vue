<template>
  <div class="content">
    <div class="card">
      <div class="card-header" data-background-color="red">
        <h4 class="title">Device Managers</h4>
        <p class="category">List of device managers in {{ domainName }} domain</p>
      </div>
      <div class="card-content">
          <md-list>
                <md-list-item
                  v-for="(devicemanager, index) in devicemanagers"
                  v-bind:key="devicemanger">
                  <router-link :to="{ path: '/devicemanagers/'+devicemanager.label }">{{ devicemanager.label }}</router-link>
                  <md-divider></md-divider>
                </md-list-item>
          </md-list>
      </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'devicemanagers',
  mounted(){
    this.$store.dispatch('getDeviceManagersInDomain')
  },
  computed: {
    domainName(){
      return this.$store.getters.domainName
    },
    devicemanagers(){
      return this.$store.getters.devicemanagers
    }
  }
}
</script>
