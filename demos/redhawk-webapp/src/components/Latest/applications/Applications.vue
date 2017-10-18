<template>
  <div class="content">
    <div class="card">
      <div class="card-header" data-background-color="red">
        <h4 class="title">Applications</h4>
        <p class="category">List of applications in {{ domainName }} domain</p>
      </div>
      <div class="card-content">
          <md-list>
                <md-list-item
                  v-for="(application, index) in applications"
                  v-bind:key="application">
                  <router-link :to="{ path: '/applications/'+application.name }">{{ application.name }}</router-link>
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
  name: 'applications',
  mounted(){
    this.$store.dispatch('getApplicationsInDomain')
  },
  computed: {
    domainName(){
      return this.$store.getters.domainName
    },
    applications(){
      return this.$store.getters.applications
    }
  }
}
</script>
