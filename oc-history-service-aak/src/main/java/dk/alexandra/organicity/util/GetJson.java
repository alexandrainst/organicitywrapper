package dk.alexandra.organicity.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;

public class GetJson {
  //String sURL = "http://freegeoip.net/json/"; //just a string

  public static JsonParser get(String sURL) throws IOException {
    // Connect to the URL using java's native library
    URL url = new URL(sURL);
    HttpURLConnection request = (HttpURLConnection) url.openConnection();
    request.connect();

    // Convert to a JSON object to print data
    JsonFactory jfactory = new JsonFactory();
    JsonParser jParser = jfactory.createJsonParser((InputStream) request.getContent());
    return jParser;
  }
  //parser example
/*  Let's consider following POJO:

    public class Foo {
      public String foo;
    }

  and sample JSON stream of:

    String json = [{\"foo\": \"bar\"},{\"foo\": \"biz\"}]";

  while there are convenient ways to work on this with databinding (see ObjectReader.readValues() for details), you can easily use streaming to iterate over stream, bind individual elements as well:

    org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper()
    JsonFactory f = new JsonFactory();
    JsonParser jp = f.createJsonParser(json);
    // advance stream to START_ARRAY first:
    jp.nextToken();
    // and then each time, advance to opening START_OBJECT
    while (jp.nextToken() == JsonToken.START_OBJECT)) {
      Foo foobar = mapper.readValue(jp, Foo.class);
      // process
      // after binding, stream points to closing END_OBJECT
    }
*/
}
