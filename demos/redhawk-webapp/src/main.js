// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
//require('./libs/bluefile-debug.js')
//require('./libs/sigplot.plugins-debug.js')
//require('./libs/sigplot-debug.js')

import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import VueMaterial from 'vue-material'
import 'vue-material/dist/vue-material.css'

Vue.config.productionTip = false
Vue.use(VueMaterial)
/*
Vue.material.registerTheme('default',{
  primary: 'red',
  accent: 'black',
  warn: 'deep-orange',
  background: 'white'
})
*/

/* eslint-disable no-new */
new Vue({
  el: '#app',
  store,
  router,
  template: '<App/>',
  components: { App }
})
