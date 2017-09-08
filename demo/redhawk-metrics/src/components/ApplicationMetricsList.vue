<template>
  <div class="card">
    <div class="card-header" data-background-color="red">
      <h4 class="title">Applications</h4>
      <p class="category">List of applications with metrics available</p>
    </div>
    <div class="card-content">
        <md-list>
              <md-list-item
                v-for="(application, index) in availableAppMetrics"
                v-bind:key="application"
                @click="showApplication(application)">
                {{ application }}
                <md-divider></md-divider>
              </md-list-item>
            </md-list>
    </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'applicationmetriclist',
  computed: {
    availableAppMetrics(){
      return this.$store.getters.available.APPLICATION
    }
  },
  methods: {
    showApplication(application){
      this.$store.dispatch('chooseApplication', application)
    }
  }
}
</script>
