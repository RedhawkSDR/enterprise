import Vue from 'vue'
import Router from 'vue-router'
import RedhawkMetrics from '@/RedhawkMetrics'
import Hello from '@/components/Hello'
import DomainConfig from '@/components/DomainConfig'
import AppMetrics from '@/components/ApplicationMetrics'
import GPPMetrics from '@/components/GPPMetrics'
import PortStatistics from '@/components/PortStatistics'


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
      component: RedhawkMetrics,
      redirect: '/configuration',
      children: [
        {
          path: 'configuration',
          name: 'Domain Configuration',
          component: DomainConfig
        },
        {
          path: 'appmetrics',
          name: 'Application Metrics',
          component: AppMetrics
        },
        {
          path: 'gppmetrics',
          name: 'GPP Metrics',
          component: GPPMetrics
        },
        {
          path: 'portstats',
          name: 'Port Statistics',
          component: PortStatistics
        }
      ]
    }
  ]
})
