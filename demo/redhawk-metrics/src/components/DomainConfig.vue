<template>
  <div class="content">
    <div class="card">
      <div class="card-header" data-background-color="red">
        <h4 class="title">Domain & REST Config</h4>
        <p class="category">Update REST endpoint location, Name Server and Domain Name information</p>
      </div>
      <div class="card-content">
        <form>
          <md-input-container>
            <label>Base URL</label>
            <md-input v-model="baseURL"></md-input>
          </md-input-container>
          <md-input-container>
            <label>Domain Name</label>
            <md-input v-model="domainName"></md-input>
          </md-input-container>
          <md-input-container>
            <label>Name Server</label>
            <md-input v-model="nameServer"></md-input>
          </md-input-container>
        </form>
        <md-button class="md-raised md-warn">
          Update
        </md-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'domainconfig',
  computed:{
    baseURL: {
      get(){
        return this.$store.getters.baseURL
      }
    },
    domainName: {
      get(){
        return this.$store.getters.domainName
      },
      set(value){
				this.$store.dispatch('editDomainConfigName', value)
			}
    },
    nameServer: {
      get(){
        return this.$store.getters.nameServer
      },
      set(value){
        this.$store.dispatch('editDomainConfigNameServer', value)
      }
    }
  }
}
</script>
