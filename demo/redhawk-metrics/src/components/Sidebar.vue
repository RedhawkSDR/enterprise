<template>
  <div :class="sidebarClasses"
    style="background-image: url(static/img/sidebar-3.jpg)">
    <md-toolbar class="logo">
      <h2 class="md-title">REDHAWK Metrics</h2>
      <!--
      <md-button @click="getAvailable" class="md-icon-button md-raised">
        <md-icon>
          refresh
        </md-icon>
      </md-button>
      -->
    </md-toolbar>
    <div class="sidebar-wrapper">
      <ul :class="navClasses">
        <!--By default vue-router adds an active class to each route link. This way the links are colored when clicked-->
        <router-link
          v-for="(link,index) in options"
          :to="link.path"
          tag="li"
          :ref="link.name">
          <a>
            <!--<i v-if="link.icon" class="material-icons">{{link.icon}}</i>-->

            <p>{{link.name}}
            </p>
          </a>
        </router-link>
      </ul>
    </div>
    <!--
    <div class="sidebar-wrapper">
      <md-list>
        <md-list-item
        v-for="(option, index) in options"
        @click="displayOption(option)">
          {{ option }}
        </md-list-item>
      </md-list>
    </div>
    -->
    <div class="sidebar-background" style="background-image: url(static/img/sidebar-3.jpg)"></div>
  </div>
    <!--
    <div>
      <md-list>
        <md-list-item>
          <span> Applications </span>
          <md-list-expand>
            <md-list-item
            v-for="(application, index) in available.APPLICATION"
            v-bind:key="application"
            v-bind:index="index"
            v-bind:application="application"
            @click="showApplication(application)">
            {{ application }}
          </md-list-item>
        </md-list-expand>
      </md-list-item>
    </md-list>
  </div>
  <div>
    <md-list>
      <md-list-item>
        <span> Devices </span>
        <md-list-expand>
          <md-list-item
          v-for="(device, index) in available.GPP"
          v-bind:key="device"
          v-bind:index="index"
          v-bind:device="device"
          @click="showGPP(device)">
          {{ device }}
        </md-list-item>
      </md-list-expand>
    </md-list-item>
  </md-list>
</div>
<div>
  <md-list class="custom-list md-triple-line">
    <md-list-item>
      <span> Ports </span>
      <md-list-expand>
        <md-list-item
        v-for="(port, index) in available.PORT"
        v-bind:key="port"
        v-bind:index="index"
        v-bind:port="port"
        @click="showPort(port)">
        <div class="md-list-text-container">
          <span><b>Port:</b> {{ port.PORT }}</span>
          <span><b>Comp:</b> {{ port.COMPONENT }}</span>
          <span><b>App:</b> {{ port.APP }}</span>
        </div>
        <md-divider></md-divider>
      </md-list-item>
    </md-list-expand>
  </md-list-item>
</md-list>
</div>
</div>
-->
<!--
<div>
<md-button class="md-primary md-raised" v-on:click="getAvailable">Available</md-button>
<tree-view :data="available" :options="{maxDepth: 7, rootObjectKey: 'metrics'}"></tree-view>
</div>
-->
</template>

<script>
export default {
  name: 'availablemetrics',
  props: {
      type: {
        type: String,
        default: 'sidebar',
        validator: (value) => {
          let acceptedValues = ['sidebar', 'navbar']
          return acceptedValues.indexOf(value) !== -1
        }
      },
      backgroundColor: {
        type: String,
        default: 'purple',
        validator: (value) => {
          let acceptedValues = ['purple', 'black', 'darkblue']
          return acceptedValues.indexOf(value) !== -1
        }
      },
      activeColor: {
        type: String,
        default: 'success',
        validator: (value) => {
          let acceptedValues = ['primary', 'info', 'success', 'warning', 'danger']
          return acceptedValues.indexOf(value) !== -1
        }
      },
      sidebarLinks: {
        type: Array,
        default: () => []
      }
  },
  data() {
    return {
      options: [
        {
          name: 'Configuration',
          path: '/configuration'
        },
        {
          name: 'Application',
          path: '/appmetrics'
        },
        {
          name: 'GPP',
          path: '/gppmetrics'
        },
        {
          name: 'PORT',
          path: '/portstats'
        }
      ]
    }
  },
  computed: {
    sidebarClasses () {
            if (this.type === 'sidebar') {
              return 'sidebar'
            } else {
              return 'collapse navbar-collapse off-canvas-sidebar'
            }
    },
    navClasses () {
      if (this.type === 'sidebar') {
        return 'nav'
      } else {
        return 'nav navbar-nav'
      }
    },
    available(){
      return this.$store.getters.available
    },
    port(){

    }
  },
  methods: {
    getAvailable: function(){
      this.$store.dispatch("getAvailableMetrics")
    },
    test(){
      console.log("HELLO")
    },
    displayOption(option){
      console.log("Option "+option)
      if(option == "Configuration"){
        this.$store.dispatch('configurationView')
      }else if(option == "Application"){
        this.$store.dispatch("getAvailableMetrics", 'application')
      }else if(option == "GPP"){
        this.$store.dispatch("getAvailableMetrics", 'gpp')
      }else if(option == "Ports"){
        this.$store.dispatch("getAvailableMetrics", "port")
      }
    },
    showApplication(application){
      this.$store.dispatch('appMetricsView', application)
    },
    showGPP(device){
      this.$store.dispatch('gppMetricsView', device)
    },
    showPort(port){
      this.$store.dispatch('portMetricsView', port)
    }
  }
}
</script>
