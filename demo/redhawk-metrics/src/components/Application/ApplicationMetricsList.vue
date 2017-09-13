<template>
  <div class="card">
    <div class="card-header" data-background-color="red">
      <h4 class="title">Applications</h4>
      <p class="category">List of applications with metrics available</p>
    </div>
    <div class="card-content">
        <md-list>
              <md-list-item
                v-for="(application, index) in reformatedMetricList"
                v-bind:key="application">
                <!-- TODO Make that a variable -->
                <router-link :to="application.link">{{ application.name }}</router-link>
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
    },
    reformatedMetricList(){
      var myList = this.$store.getters.available.APPLICATION
      var linkList = []

      var i;
      for(i = 0; i< myList.length; i++){
        console.log(myList[i])
        var tmp = new Object()
        tmp.name = myList[i]
        tmp.link = '/appmetrics/'+tmp.name
        linkList.push(tmp)
      }

      return linkList
    }
  }
}
</script>
