<template>
<div>
  <div>
  <!-- Add Event Channel Name and Way to close view -->
  <md-table v-once>
    <md-table-header>
      <md-table-row>
        <md-table-head>Timestamp</md-table-head>
        <md-table-head>Message Body</md-table-head>
      </md-table-row>
    </md-table-header>

    <md-table-body>
      <!--<md-table-row v-for="(row, index) in messages"-->
    </md-table-body>
  </md-table>

  <md-button class="md-raised">Subscribe</md-button>
  </div>
  <div>
    <h1>Registrants</h1>
  </div>
</div>
</template>
