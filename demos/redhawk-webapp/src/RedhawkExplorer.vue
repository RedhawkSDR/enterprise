<template>
  <div class="wrapper">
    <sidebar/>
    <div class="main-panel">
      <dashboardcontent/>
    </div>
  </div>
</template>

<script>
import Sidebar from 'components/Latest/Sidebar.vue'
import Content from 'components/Latest/Content.vue'

export default {
  name: 'redhawkexplorer',
  components: {
    'sidebar' : Sidebar,
    'dashboardcontent' : Content
  }
}
</script>

<style>
</style>
