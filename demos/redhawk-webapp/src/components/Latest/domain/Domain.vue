<template>
<div class="content">
  <properties
    :bgColor="green"
    :id="domainName"
    :properties="properties" />
</div>
</template>

<script>
import Properties from '../properties/Properties.vue'

export default {
  name: 'domain',
  mounted(){
    this.$store.dispatch('selectDomainProperties', this.domainName)
  },
  components: {
    'properties' : Properties
  },
  computed: {
    domainName(){
      return this.$store.getters.domainName
    },
    properties(){
      var tempProperties = this.$store.getters.domainProperties

      /*
      * Disabling DMD_FILE,DOMAIN_NAME,DB_URL since they're command line properties and
      * and unchangeable
      */
      console.log("HELLO "+tempProperties.length)
      tempProperties.forEach(function(prop){
        if(prop.name=="DMD_FILE" || prop.name=="DB_URL" || prop.name=="DOMAIN_NAME"){
            console.log("HELLO Making edits for "+prop.name)
            prop.mode = "READONLY"
        }
        console.log("HELLO "+prop)
      })
      //for(i=0; i<tempProperties.length; i++){
      //  console.log("HELLO ")
        //var propName = tempProperties[i].name
        //if(propName=="DMD_FILE" || propName=="DB_URL" || propName=="DOMAIN_NAME"){
        //  tempProperties[i].mode = "READ_ONLY"
        //}
      //}

      return tempProperties
    }
  }
}
</script>
