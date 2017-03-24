import Vue from 'vue'
import Router from 'vue-router'
import RedhawkWebApp from '@/RedhawkWebApp'
import About from '@/About'

Vue.use(Router)

export default new Router({
  routes: [
    {
    	path: '/',
    	name: 'RedhawkWebApp',
    	component: RedhawkWebApp
    },
    {
      path: '/about',
      name: 'About',
      component: About
    }
  ]
})
