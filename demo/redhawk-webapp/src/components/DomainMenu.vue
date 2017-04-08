<template>
  <md-menu md-align-trigger>
    <md-button md-menu-trigger>{{ config.name }}</md-button>

    <md-menu-content>
      <md-menu-item @click.native="viewDomain(index)">View</md-menu-item>
      <md-menu-item @click.native="editDomainConfig(index)">Edit</md-menu-item>
      <md-menu-item @click.native="deleteDomainConfig(index)">Delete</md-menu-item>
    </md-menu-content>
  </md-menu>
</template>

<script>
export default {
  name: 'domainmenu',
  props: ['config', 'index'],
  methods: {
    editDomainConfig: function(data){
			this.$store.dispatch('editDomainConfig', data)
		},
		deleteDomainConfig(data){
      this.$store.dispatch('resetDomain')
			this.$store.dispatch('deleteDomainConfig', data)
		},
		viewDomain: function(data){
      this.$store.dispatch('resetDomain')
			this.$store.dispatch('viewDomainConfig', data)
      //this.$store.dispatch('getWaveformsAvailable', data)
		}
  }
}
</script>
