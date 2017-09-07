// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import TreeView from "vue-json-tree-view"
import VueMaterial from 'vue-material'
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

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})
