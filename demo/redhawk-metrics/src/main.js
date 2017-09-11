// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import TreeView from "vue-json-tree-view"
import VueMaterial from 'vue-material'
import Chartist from 'chartist'
import 'vue-material/dist/vue-material.css'
import 'bootstrap/dist/css/bootstrap.css'

// Plugins
import GlobalComponents from './globalComponents'
import GlobalDirectives from './globalDirectives'
import GlobalMixins from './globalMixins'

Vue.config.productionTip = false

Vue.use(VueMaterial)
Vue.use(GlobalComponents)
Vue.use(GlobalDirectives)
Vue.use(GlobalMixins)

//Import styling
import './assets/sass/material-dashboard.scss'

// global library setup
Object.defineProperty(Vue.prototype, '$Chartist', {
  get () {
    return this.$root.Chartist
  }
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App },
  data: {
      Chartist: Chartist
  }
})
