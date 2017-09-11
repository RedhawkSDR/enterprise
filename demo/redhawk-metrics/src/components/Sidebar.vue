<template>
  <div :class="sidebarClasses"
    style="background-image: url(static/img/sidebar-3.jpg)">
    <div class="logo">
      <img src="static/img/logo/RedHawk_Logo.png"
       style="width:50px;height:50px; float: left;" />
      <h4>REDHAWK Metrics</h4>
    </div>
    <div class="sidebar-wrapper">
      <!-- TODO: You can do this exact same thing with md-list-->
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
    <div class="sidebar-background" style="background-image: url(static/img/sidebar-3.jpg)"></div>
  </div>
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
          path: '/portstatistics'
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
    }
  }
}
</script>
