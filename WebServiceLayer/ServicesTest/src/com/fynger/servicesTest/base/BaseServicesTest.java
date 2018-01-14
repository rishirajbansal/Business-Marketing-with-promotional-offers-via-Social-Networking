/**
 * 
 */
package com.fynger.servicesTest.base;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.junit.After;
import org.junit.Before;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * @author Rishi
 *
 */
public class BaseServicesTest {
	
	public WebResource service = null;

	@Before
	public void setUp() throws Exception {
		
		ClientConfig config = new DefaultClientConfig();
	    Client client = Client.create(config);
	    
	    service = client.resource(getBaseURI());
	}

	@After
	public void tearDown() throws Exception {
	}

	/*@Test
	public void test() {
		fail("Not yet implemented");
	}*/
	
	private URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8088/FYNGERWEB/services").build();
	}

}
