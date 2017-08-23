import Vue from 'vue'
import Router from 'vue-router'
import RedhawkMetrics from '@/RedhawkMetrics'
import Hello from '@/components/Hello'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/Hello',
      name: 'Hello',
      component: Hello
    },
    {
      path: '/',
      name: 'RedhawkMetrics',
      component: RedhawkMetrics
    }
  ]
})
