<template>
  <div class="card">
    <div class="card-header" data-background-color="red">
      <h4 class="title">GPPs</h4>
      <p class="category">List of GPPs with metrics available</p>
    </div>
    <div class="card-content">
      <md-list>
        <md-list-item
          v-for="(gpp, index) in availableGPPMetrics"
          v-bind:key="gpp">
          <router-link :to="gpp.link">{{ gpp.name}}</router-link>
        </md-list-item>
      </md-list>
        </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'gppmetrics',
  computed: {
    availableGPPMetrics(){
      /*
      * Reformat the returned list so you can dynamically popute
      * to link in router.
      */
      var myList = this.$store.getters.available.GPP
      var linkList = []

      var i;
      for(i = 0; i< myList.length; i++){
        console.log(myList[i])
        var tmp = new Object()
        tmp.name = myList[i]
        tmp.link = '/gppmetrics/'+tmp.name
        linkList.push(tmp)
      }

      return linkList
    }
  }
}
</script>
