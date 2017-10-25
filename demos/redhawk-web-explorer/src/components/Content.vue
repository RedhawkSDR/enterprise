<template>
<v-content>
        <v-container fluid fill-height>
          <v-layout justify-center align-center>
            <router-view></router-view>
          </v-layout>
        </v-container>
      </v-content>
</template>

<script>
export default {
	name: 'content'
}
</script>
