<template>
<div class="card">
  <div class="card-header" data-background-color="green">
    <h4 class="title">{{ device.label }}</h4>
    <p class="category">Device managed by {{ devicemanagerLabel }} running on Domain {{ domainName }}</p>
  </div>
  <div class="card-content">
    <div>
      <h6><b>Started: </b>{{ device.started }}</h6>
    </div>
    <div>
      <h6><b>Admin State: </b>{{ device.adminState }}</h6>
    </div>
    <div>
      <h6><b>Usage State: </b>{{ device.usageState }}</h6>
    </div>
    <div>
      <h6><b>Operation State: </b>{{ device.operationState }}</h6>
    </div>
    <div>
      <h6><b>Implementation: </b>{{ device.implementation }}</h6>
    </div>
  </div>
</div>
</template>

<script>
export default {
  name: 'deviceMetaData',
  props: ['device'],
  computed: {
    domainName(){
      return this.$store.getters.domainName
    },
    devicemanagerLabel(){
      return this.$route.params.devicemanagerName
    }
  }
}
</script>
