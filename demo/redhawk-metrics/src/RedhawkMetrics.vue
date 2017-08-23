<template>
  <div>
    <h1>Hello World</h1>
    <button v-on:click="available">Available</button>
    <tree-view :data="sampleData" max-depth="7"></tree-view>
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
  methods: {
    available: function(){
      console.log("Making it to available")
      console.log(this.$store)
      this.$store.dispatch("getAvailableMetrics")
    }
  }
}
</script>
