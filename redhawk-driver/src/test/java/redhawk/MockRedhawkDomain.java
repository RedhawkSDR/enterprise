/*
 * This file is protected by Copyright. Please refer to the COPYRIGHT file
 * distributed with this source distribution.
 *
 * This file is part of REDHAWK __REDHAWK_PROJECT__.
 *
 * REDHAWK __REDHAWK_PROJECT__ is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * REDHAWK __REDHAWK_PROJECT__ is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package redhawk;

import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.AlreadyBound;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;

import CF.DomainManager;
import CF.DomainManagerPOATie;
import redhawk.mock.MockDomainManager;

public class MockRedhawkDomain {

    private Properties connectionProperties = new Properties();
	private static final String CORBA_NAME_SERVICE = "ORBInitRef.NameService";
	private static final int DEFAULT_NAMESERVICE_PORT = 2809;
	private static final String DEFAULT_HOSTNAME = "127.0.0.1";
    private ORB orb;
    private static final String DEFAULT_DOMAIN_NAME = "TEST_REDHAWK_DEV";
	
    private DomainManager domainManager;
    
    
	public MockRedhawkDomain() {
        connectionProperties.put("com.sun.CORBA.transport.ORBUseNIOSelectToWait", "false");
        System.setProperty("org.omg.CORBA.ORBClass", "org.jacorb.orb.ORB");
    	connectionProperties.put("org.omg.CORBA.ORBSingletonClass", "org.jacorb.orb.ORBSingleton");
    	connectionProperties.put("org.omg.PortableInterceptor.ORBInitializerClass.standard_init", "org.jacorb.orb.standardInterceptors.IORInterceptorInitializer");
    	connectionProperties.put("jacorb.config.dir", System.getProperty("jacorb.config.dir",""));
    	connectionProperties.put("jacorb.retries", 1);
    	connectionProperties.put(CORBA_NAME_SERVICE, "corbaname::" + DEFAULT_HOSTNAME + ":" + DEFAULT_NAMESERVICE_PORT);
	}
	
	
	public void start() {
        try {
        	orb = (ORB) ORB.init((String[])null, connectionProperties);
        	POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
        	rootPOA.the_POAManager().activate();   
        	
        	DomainManagerPOATie domainManagerTie = new DomainManagerPOATie(new MockDomainManager());
        	domainManager = domainManagerTie._this(orb);
        	
            NamingContextExt ctx = NamingContextExtHelper.narrow(orb.resolve_initial_references("NameService"));
        	
            NameComponent domainContainer[] = ctx.to_name(DEFAULT_DOMAIN_NAME);
            NamingContextExt domainContainerContext =  NamingContextExtHelper.narrow(ctx.bind_new_context(domainContainer));
            System.out.println("New naming context, DEFAULT_DOMAIN_NAME, added!");
            
            NameComponent name3[] = ctx.to_name(DEFAULT_DOMAIN_NAME);
            domainContainerContext.rebind(name3, domainManager);
            System.out.println("schedule rebind successful!");
        } catch(AlreadyBound | NotFound | CannotProceed | InvalidName | org.omg.CORBA.ORBPackage.InvalidName | AdapterInactive e) {
        	e.printStackTrace();
        }
	}
	
	
	public void shutdown() {
		Object objRef;
		try {
			objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			NameComponent[] domainNameComponent = null;
			domainNameComponent = ncRef.to_name(DEFAULT_DOMAIN_NAME+"/"+DEFAULT_DOMAIN_NAME);
			ncRef.unbind(domainNameComponent);

			NameComponent[] domainManagerContainer = ncRef.to_name(DEFAULT_DOMAIN_NAME);
        	ncRef.unbind(domainManagerContainer);
        	
		} catch (org.omg.CORBA.ORBPackage.InvalidName | InvalidName | NotFound | CannotProceed e) {
			e.printStackTrace();
		}
		
	}


	
	
}
