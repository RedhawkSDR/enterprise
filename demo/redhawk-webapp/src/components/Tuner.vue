<template>
<md-list-item>
  <span>
    {{ tuner["FRONTEND::tuner_status::tuner_type"] }} : {{ tuner["FRONTEND::tuner_status::allocation_id_csv"]}}
    <md-button>
      DEALLOCATE
    </md-button>
  </span>
  <!--
  <span v-else>
    {{ tuner["FRONTEND::tuner_status::tuner_type"] }}
    <md-button>
      ALLOCATE
    </md-button>
  </span>
-->
</md-list-item>
</template>

<script>
export default{
  name: 'tuner',
  props: ['tuner', 'type']
}
</script>
