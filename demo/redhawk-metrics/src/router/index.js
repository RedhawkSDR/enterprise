import Vue from 'vue'
import Router from 'vue-router'
import RedhawkMetrics from '@/RedhawkMetrics'
import DomainConfig from '@/components/DomainConfig'

//APP Components
import AppMetrics from '@/components/Application/ApplicationMetrics'
import AppMetricsList from '@/components/Application/ApplicationMetricsList'
import AppMetricsView from '@/components/Application/ApplicationMetricsView'

//PORT Components
import PortStatistics from '@/components/Port/PortStatistics'
import PortStatisticsList from '@/components/Port/PortStatisticsList'
import PortStatisticsView from '@/components/Port/PortStatisticsView'

//GPP Components
import GPPMetrics from '@/components/GPP/GPPMetrics'
import GPPMetricsList from '@/components/GPP/GPPMetricsList'
import GPPMetricsView from '@/components/GPP/GPPMetricsView'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'RedhawkMetrics',
      component: RedhawkMetrics,
      children: [
        {
          path: 'configuration',
          name: 'Domain Configuration',
          component: DomainConfig
        },
        {
          path: 'appmetrics',
          name: 'Application Metrics',
          component: AppMetrics,
          redirect: '/appmetrics/list',
          children: [
            {
              path: 'list',
              name: 'App Metric List',
              component: AppMetricsList
            },
            {
              path: ':appName',
              name: 'App Metrics',
              component: AppMetricsView
            }
          ]
        },
        {
          path: 'gppmetrics',
          name: 'GPP Metrics',
          component: GPPMetrics,
          redirect: '/gppmetrics/list',
          children: [
            {
              path: 'list',
              name: 'GPP Metrics List',
              component: GPPMetricsList
            },
            {
              path: ':gppName',
              name: 'GPP Metrics',
              component: GPPMetricsView
            }
          ]
        },
        {
          path: 'portstatistics',
          name: 'Port Statistics',
          component: PortStatistics,
          redirect: '/portstatistics/list',
          children: [
            {
              path: 'list',
              name: 'Port Statistics List',
              component: PortStatisticsList
            },
            {
              path: 'applications/:application/components/:component/ports/:port',
              name: 'Port Statistics View',
              component: PortStatisticsView
            }
          ]
        }
      ]
    }
  ]
})
