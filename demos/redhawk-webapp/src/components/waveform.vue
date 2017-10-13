<template>
<div v-on:click="fillComponents">

</div>
</template>

<script>
export default {
  name: 'waveform',
  props: ['waveform'],
  computed: {
    rhwaveform: function(){
      return this.waveform
    }
  }
}
</script>
