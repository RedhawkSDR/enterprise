import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import RedhawkWebApp from '@/RedhawkWebApp'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/vueinfo',
      name: 'Hello',
      component: Hello
    },
    {
    	path: '/',
    	name: 'RedhawkWebApp',
    	component: RedhawkWebApp
    }
  ]
})
