<template>
<div class="card">
  <div class="card-header" data-background-color="green">
    <h4 class="title">{{ component.name }}</h4>
    <p class="category">Component running within application {{ applicationName }} on Domain {{ domainName }}</p>
  </div>
  <div class="card-content">
    <div>
      <h6><b>Started: </b>{{ component.started }}</h6>
    </div>
    <div>
      <h6><b>Process Id: </b>{{ component.processId }}</h6>
    </div>
    <div>
      <h6><b>Device Identifier: </b>{{ component.deviceIdentifier }}</h6>
    </div>
    <div>
      <h6><b>Implementation: </b>{{ component.implementation }}</h6>
    </div>
  </div>
</div>
</template>

<script>
export default {
  name: 'componentMetaData',
  props: ['component'],
  computed: {
    domainName(){
      return this.$store.getters.domainName
    },
    applicationName(){
      return this.$route.params.applicationName
    }
  }
}
</script>
