package dk.alexandra.organicity;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("organicity")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String defaultView() {
        return "This api has two calls: /entities/{id}/readings and /entities. \n\n"+
        		"/entities has the following parameters: latStart (default null), latEnd (default null), longStart (default null), longEnd (default null), limit (default 100) ";
    }
    
    
    @GET
    @Path("/entities")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllCustomers() {
      return "---Customer List---";
    }
    
    @GET
    @Path("/entities/{id}/readings")
    @Produces(MediaType.TEXT_PLAIN)
    public long getCustomer(@PathParam("id") long id) {
      return id;
    }
}
