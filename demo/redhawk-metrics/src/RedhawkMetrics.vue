<template>
  <div>
    <md-toolbar>
      REDHAWK Metrics
    </md-toolbar>
    <button v-on:click="getAvailable">Available</button>
    <tree-view :data="available" :options="{maxDepth: 7, rootObjectKey: 'metrics'}"></tree-view>
  </div>
</template>

<script>
export default {
  name: 'redhawkmetrics',
  data(){
    return {
      sampleData: {"glossary":{"title":"example glossary","GlossDiv":{"title":"S","GlossList":{"GlossEntry":{"ID":"SGML","SortAs":"SGML","GlossTerm":"Standard Generalized Markup Language","Acronym":"SGML","Abbrev":"ISO 8879:1986","GlossDef":{"para":"A meta-markup language, used to create markup languages such as DocBook.","GlossSeeAlso":["GML","XML"]},"GlossSee":"markup"}}}}}
    }
  },
  computed: {
    available(){
      return this.$store.getters.available
    }
  },
  methods: {
    getAvailable: function(){
      console.log("Making it to available")
      this.$store.dispatch("getAvailableMetrics")
    }
  }
}
</script>


<style>
/* Root node should not be indented */
.tree-view-item-root {
  margin-left: 0;
}
</style>
