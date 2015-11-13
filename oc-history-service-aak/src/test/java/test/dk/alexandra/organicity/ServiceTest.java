package test.dk.alexandra.organicity;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dk.alexandra.organicity.controller.Main;
import dk.alexandra.organicity.model.organicity.Device;
import dk.alexandra.organicity.service.AakDataService;

public class ServiceTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        // uncomment the following line if you want to enable
        // support for JSON in the client (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml and Main.startServer())
        // --
        // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

        target = c.target(Main.BASE_URI);
        AakDataService.USE_DEMO_DATA = false;
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void testGetEntitites() throws JsonParseException, JsonMappingException, IOException {
      InputStream in = target.path("v1/entities").request().get(InputStream.class);
      ObjectMapper m = new ObjectMapper();
      List<Device> devices = m.readValue(in, new TypeReference<List<Device>>() { });
      assertTrue(devices!=null);
    }
    @Test
    public void testGetEntititesPrintOutput() throws JsonParseException, JsonMappingException, IOException {
      System.out.println(target.path("v1/entities").request().get(String.class));
    }
}
