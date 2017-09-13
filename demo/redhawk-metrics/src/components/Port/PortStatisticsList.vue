<template>
  <div class="card">
    <div class="card-header" data-background-color="red">
      <h4 class="title">Ports</h4>
      <p class="category">List of Ports with metrics available</p>
    </div>
    <div class="card-content">
      <md-list>
        <md-list-item
          class="md-triple-line"
          v-for="(port, index) in availableMetrics"
          v-bind:key="port">
          <router-link :to="port.link">
            <div class="md-list-text-container">
              <span><b>Port:</b> {{ port.name }}</span>
              <span><b>Comp:</b> {{ port.component }}</span>
              <span><b>App:</b> {{ port.app }}</span>
            </div>
            <md-divider></md-divider>
          </router-link>
        </md-list-item>
      </md-list>
        </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'portstatisticslist',
  computed: {
    availableMetrics(){
      /*
      * Reformat the returned list so you can dynamically popute
      * to link in router.
      */
      var myList = this.$store.getters.available.PORT
      var linkList = []

      var i;
      for(i = 0; i< myList.length; i++){
        console.log(myList[i])
        var tmp = new Object()
        tmp.name = myList[i].PORT
        tmp.component = myList[i].COMPONENT
        tmp.app = myList[i].APP
        tmp.link = 'applications/'+tmp.app+'/components/'
            +tmp.component+'/ports/'+tmp.name

        linkList.push(tmp)
      }

      return linkList
    }
  }
}
</script>
