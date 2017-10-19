import Vue from 'vue'
import Router from 'vue-router'
import RedhawkWebApp from '@/RedhawkWebApp'
import About from '@/About'
import RHApplication from '@/components/RHApplicationView'
import RedhawkExplorer from '@/RedhawkExplorer'
import Configuration from 'components/Latest/configuration/Configuration'

//Waveform Views
import Waveforms from 'components/Latest/waveforms/WaveformsView'
import AvailableWaveforms from 'components/Latest/waveforms/AvailableWaveforms'
import WaveformLaunch from 'components/Latest/waveforms/WaveformLaunch'

//Aplication Related Views
import ApplicationsView from 'components/Latest/applications/ApplicationsView'
import Applications from 'components/Latest/applications/Applications'
import Application from 'components/Latest/applications/Application'
import Component from 'components/Latest/component/Component'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      component: RedhawkExplorer,
      children: [
        {
          path: 'configuration',
          name: 'REDHAWK Explorer Configuration',
          component: Configuration
        },
        {
          path: 'waveforms',
          name: 'Waveforms',
          component: Waveforms,
          redirect: '/waveforms',
          children: [
            {
              path: '/',
              name: 'Available Waveforms',
              component: AvailableWaveforms
            },
            {
              path: ':waveformname',
              name: 'Launch Waveform',
              component: WaveformLaunch
            }
          ]
        },
        {
          path: 'applications',
          name: 'Applications',
          component: ApplicationsView,
          redirect: '/applications',
          children: [
            {
              path: '/',
              name: 'Applications',
              component: Applications
            },
            {
              path: ':applicationName',
              name: 'Application',
              component: Application
            },
            {
              path: ':applicationName/components/:componentName',
              name: 'Component',
              component: Component
            }
          ]
        }
      ]
    },
    {
    	path: '/old/',
    	name: 'RedhawkWebApp',
    	component: RedhawkWebApp
    },
    {
      path: '/old/about',
      name: 'About',
      component: About
    },
    {
      path: '/old/application',
      name: 'RedhawkApplication',
      component: RHApplication
    }
  ]
})
