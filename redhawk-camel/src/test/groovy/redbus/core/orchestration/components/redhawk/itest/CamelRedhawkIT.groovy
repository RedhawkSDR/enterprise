package redbus.core.orchestration.components.redhawk.itest

import org.apache.camel.CamelContext
import org.jboss.arquillian.container.test.api.Deployment
import org.jboss.arquillian.junit.Arquillian
import org.jboss.arquillian.osgi.StartLevelAware
import org.jboss.shrinkwrap.api.spec.JavaArchive
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.osgi.framework.Bundle
import redbus.core.orchestration.components.redhawk.RedhawkComponent
import redbus.itest.RedbusTestSupport

@RunWith(Arquillian)
class CamelRedhawkIT extends RedbusTestSupport {
    def camelRedhawkBundle

    @StartLevelAware(autostart = true, startLevel = 100)
    @Deployment(name = 'CamelRedhawkIT')
    public static JavaArchive deployment() {
        createJavaArchive 'CamelRedhawkIT'
    }

    @Before
    public void before() {
        camelRedhawkBundle = waitBundleState 'redbus.core.orchestration.components.camel-redhawk', Bundle.ACTIVE
    }

    @Test
    def 'test that the camel-redhawk service is loaded'() {
        setup: 'Get the camel context so that we can get the redhawk type converter component'
        def camelContext = getOsgiService CamelContext, 'camel.context.name=redbus-orchestration-context', 30000

        expect: 'The component is a RedhawkComponent instance'
        camelContext.getComponent 'redhawk', RedhawkComponent
    }
}
