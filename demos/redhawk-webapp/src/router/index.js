import Vue from 'vue'
import Router from 'vue-router'
import RedhawkWebApp from '@/RedhawkWebApp'
import About from '@/About'
import RHApplication from '@/components/RHApplicationView'

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
    },
    {
      path: '/application',
      name: 'RedhawkApplication',
      component: RHApplication
    }
  ]
})
