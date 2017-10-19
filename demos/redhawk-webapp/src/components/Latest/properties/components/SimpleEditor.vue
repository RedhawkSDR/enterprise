<template>
    <md-input-container v-if="!('enumerations' in property)">
      <label>{{ property.id }}</label>
      <md-input v-model="property.value" :disabled="property.mode=='READONLY'"></md-input>
    </md-input-container>
    <md-input-container v-else>
      <label for="enumvalue">{{ property.id }}</label>
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
