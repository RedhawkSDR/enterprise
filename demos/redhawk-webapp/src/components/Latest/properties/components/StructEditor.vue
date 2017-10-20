<template>
<div>
  <md-button @click="edit" class="md-icon-button md-raised"><md-icon>edit</md-icon></md-button><span>{{ property.id }}</span>
</div>
</template>

<script>
export default {
  name: 'struct',
  props: ['property'],
  methods: {
    edit(){
      console.log("Do something")
    }
  }
}
</script>
