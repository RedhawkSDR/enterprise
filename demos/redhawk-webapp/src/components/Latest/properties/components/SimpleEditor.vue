<template>
    <md-input-container v-if="!('enumerations' in property)">
      <label v-if="property.name!=null">{{ property.name }}</label>
      <label v-else>{{ property.id }}</label>
      <md-input v-model="property.value" :disabled="property.mode=='READONLY'"></md-input>
    </md-input-container>
    <md-input-container v-else>
      <label v-if="property.name!=null" for="enumvalue">{{ property.name }}</label>
      <label v-else for="enumvalue">{{ property.id }}</label>
      <md-select name="enumvalue" id="enumvalue" v-model="enumvalue">
        <md-option
          v-for="(enumeration, index) in enumerations"
          :value="enumeration.value">{{ enumeration.label }}</md-option>
      </md-select>
    </md-input-container>
</template>

<script>
export default{
  name: 'simpleprop',
  props: ['property'],
  mounted(){
    if('enumerations' in this.property){
      console.log("FOUND ENUMERATIONS!!!!!! "+this.property.id)
      this.enumerations = this.property.enumerations.enumerations
      this.enumvalue = this.property.value
    }else{
      console.log("No enumerations for "+this.property.id)
    }
  },
  data() {
    return {
      enumerations: [],
      enumvalue: ''
    }
  }
}
</script>
