import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'

import RedhawkExplorer from '../RedhawkExplorer'

import Settings from '@/components/settings/Settings'
import Domain from '@/components/domain/Domain'

//Waveform
import WaveformsView from '@/components/waveforms/WaveformsView'
import WaveformCatalog from '@/components/waveforms/WaveformCatalog'
import WaveformLaunch from '@/components/waveforms/WaveformLaunch'

//Applications
import ApplicationsView from '@/components/applications/ApplicationsView'
import ApplicationsList from '@/components/applications/ApplicationsList'
import Application from '@/components/applications/Application'
import Component from '@/components/component/Component'

//Device Managers
import DeviceManagersView from '@/components/devicemanagers/DeviceManagersView'
import DeviceManagers from '@/components/devicemanagers/DeviceManagers'
import DeviceManager from '@/components/devicemanagers/DeviceManager'
import Device from '@/components/device/Device'
import DeviceAllocation from '@/components/device/components/Allocate'
import DeviceDeallocation from '@/components/device/components/Deallocate'


//Port
import Port from '@/components/port/Port'


Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Redhawk Web Explorer',
      component: RedhawkExplorer,
      children: [
        {
          path: 'settings',
          name: 'REDHAWK Settings',
          component: Settings
        },
        {
          path: 'domain',
          name: 'Domain',
          component: Domain
        },
        {
          path: 'waveforms',
          name: "Waveforms",
          component: WaveformsView,
          redirect: 'waveforms',
          children: [
            {
              path: '/',
              name: 'Waveform Catalog',
              component: WaveformCatalog
            },
            {
              path: ':waveformname',
              name: 'Waveform Launch',
              component: WaveformLaunch
            }
          ]
        },
        {
          path: 'applications',
          name: 'Applications',
          component: ApplicationsView,
          redirect: 'applications',
          children: [
            {
              path: '/',
              name: 'Applications',
              component: ApplicationsList
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
            },
            {
              path: ':applicationName/components/:componentName/ports/:portName',
              name: 'Port',
              component: Port
            }
          ]
        },
        {
          path: 'devicemanagers',
          name: 'Device Managers',
          component: DeviceManagersView,
          redirect: '/devicemanagers',
          children: [
            {
              path: '/',
              name: 'Device Managers',
              component: DeviceManagers
            },
            {
              path: ':devicemanagerLabel',
              name: 'Device Manager',
              component: DeviceManager
            },
            {
              path: ':devicemanagerLabel/devices/:deviceLabel',
              name: 'Device',
              component: Device
            },
            {
              path: ':devicemanagerLabel/devices/:deviceLabel/allocate',
              name: 'Device Allocation',
              component: DeviceAllocation
            },
            {
              path: ':devicemanagerLabel/devices/:deviceLabel/deallocate',
              name: 'Device Deallocation',
              component: DeviceDeallocation
            },
            {
              path: ':devicemanagerLabel/devices/:deviceLabel/ports/:portName',
              name: 'Port',
              component: Port
            }
          ]
        }
      ]
    }
  ]
})
