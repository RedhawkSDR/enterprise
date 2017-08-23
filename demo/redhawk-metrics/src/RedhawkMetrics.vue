<template>
  <h1>Hello World</h1>
  <div>
    <tree-view :data="sampleData" :options="{maxDepth: 3}"></tree-view>
  </div>
</template>

<script>
export default {
  name: 'redhawkmetrics',
  data(){
    return {
      sampleData: {"glossary":{"title":"example glossary","GlossDiv":{"title":"S","GlossList":{"GlossEntry":{"ID":"SGML","SortAs":"SGML","GlossTerm":"Standard Generalized Markup Language","Acronym":"SGML","Abbrev":"ISO 8879:1986","GlossDef":{"para":"A meta-markup language, used to create markup languages such as DocBook.","GlossSeeAlso":["GML","XML"]},"GlossSee":"markup"}}}}}
    }
  }
}
</script>
