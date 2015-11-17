package dk.aarhus.organicity.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
//import javax.json.stream.JsonGenerator;


//http://www.mkyong.com/java/jackson-streaming-api-to-read-and-write-json/
//http://www.markhneedham.com/blog/2014/04/30/jerseyjax-rs-streaming-json/

@Path("jsontest")
public class JsonTest {
  /**
   * Method handling HTTP GET requests. The returned object will be sent
   * to the client as "text/plain" media type.
   *
   * @return String that will be returned as a text/plain response.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getIt() {
    final Map<Integer, String> people  = new HashMap<>();
    people.put(1, "Michael");
    people.put(2, "Mark");

    StreamingOutput stream = new StreamingOutput() {
      @Override
      public void write(OutputStream os) throws IOException, WebApplicationException
      {
        JsonFactory jfactory = new JsonFactory();
        JsonGenerator jg = jfactory.createJsonGenerator(os, JsonEncoding.UTF8);
        jg.writeStartArray();

        for ( Map.Entry<Integer, String> person : people.entrySet()  )
        {
          jg.writeStartObject();
          jg.writeFieldName( "id" );
          jg.writeString( person.getKey().toString() );
          jg.writeFieldName( "name" );
          jg.writeString( person.getValue() );
          jg.writeEndObject();
        }
        jg.writeEndArray();

        jg.flush();
        jg.close();
      }
    };


    return Response.ok().entity( stream ).type( MediaType.APPLICATION_JSON ).build()    ;
  }

}
