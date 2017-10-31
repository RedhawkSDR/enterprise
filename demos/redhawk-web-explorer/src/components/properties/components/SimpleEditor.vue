<template slot="items" scope="props">
  <tr>
    <td>
      <div v-if="props.item.name">{{props.item.name}}</div>
      <div v-else>{{props.item.id}}</div>
    </td>
    <td>
      {{ props.item.value }}
    </td>
  </tr>
</template>

<script>
export default {
  name: 'simpleeditor'
}
</script>
