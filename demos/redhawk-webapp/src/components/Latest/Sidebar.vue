<template>
  <div :class="sidebarClasses"
       :data-color="backgroundColor"
       data-image="static/img/sidebar-1.jpg"
       style="background-image: url(static/img/sidebar-2.jpg)"
       :data-active-color="activeColor">

    <div class="logo">
      <h4>REDHAWK Explorer</h4>
    </div>
    <div class="sidebar-wrapper">

      <slot>

      </slot>
      <ul :class="navClasses">
        <!--By default vue-router adds an active class to each route link. This way the links are colored when clicked-->
        <router-link v-for="(link,index) in sidebarLinks" :to="link.path" tag="li" :ref="link.name">
          <a>
            <i v-if="link.icon" class="material-icons">{{link.icon}}</i>

            <p>{{link.name}}
            </p>
          </a>
        </router-link>
      </ul>
    </div>
    <div class="sidebar-background" style="background-image: url(static/img/sidebar-2.jpg)"></div>
  </div>
</template>
<script>
  export default {
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
      /**
       * Styles to animate the arrow near the current active sidebar link
       * @returns {{transform: string}}
       */
      arrowMovePx () {
        return this.linkHeight * this.activeLinkIndex
      }
    },
    data () {
      return {
        linkHeight: 60,
        activeLinkIndex: 0,

        windowWidth: 0,
        isWindows: false,
        hasAutoHeight: false,
        sidebarLinks: [
          {
            name: 'Configuration',
            path: '/configuration'
          },
          {
            name: 'Domain',
            path: '/domain'
          },
          {
            name: 'Device Managers',
            path: '/devicemanagers'
          },
          {
            name: 'Waveforms',
            path: '/waveforms'
          },
          {
            name: 'Applications',
            path: '/applications'
          }
        ]
      }
    },
    methods: {
      findActiveLink () {
        this.sidebarLinks.find((element, index) => {
          let found = element.path === this.$route.path
          if (found) {
            this.activeLinkIndex = index
          }
          return found
        })
      }
    },
    mounted () {
      this.findActiveLink()
    },
    watch: {
      $route: function (newRoute, oldRoute) {
        this.findActiveLink()
      }
    }
  }

</script>
<style>

</style>
