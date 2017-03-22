import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import RedhawkWebApp from '@/RedhawkWebApp'
import Plot from '@/components/Plot'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/vueinfo',
      name: 'Hello',
      component: Hello
    },
    {
      path: '/plot',
      name: 'plot',
      component: Plot
    },
    {
    	path: '/',
    	name: 'RedhawkWebApp',
    	component: RedhawkWebApp
    }
  ]
})
