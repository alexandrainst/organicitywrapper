package test.dk.alexandra.organicity.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.junit.Test;

import dk.alexandra.organicity.model.ckan.CkanResponse;
import dk.alexandra.organicity.model.organicity.Device;
import dk.alexandra.organicity.model.organicity.DeviceData;
import dk.alexandra.organicity.service.AakCkanService;
import dk.alexandra.organicity.service.AakDataService;

public class TestAakDataService {
  
  	@Test
	public void testService() throws IOException, ParseException {
		AakDataService.USE_DEMO_DATA = true;
		AakDataService s = AakDataService.createService();
		List<Device> devices = s.getEntities();
		assertTrue("list was empty", devices!=null);
		assertEquals(2, devices.size());
		Device d = devices.get(0);
		assertTrue(d.provider!=null);
    assertTrue(d.data!=null);
    assertTrue(d.data.attributes!=null);
	}

	 @Test
	  public void testServiceOutput() throws IOException, ParseException {
		 AakDataService.USE_DEMO_DATA = true;	
	    AakDataService s = AakDataService.createService();
      DeviceData data = s.getLastMeasurement("");
	    assertTrue(data!=null);
	    assertTrue(data.attributes!=null);
	  }

	@Test
	public void testCkanService() throws IOException {
		AakCkanService s = new AakCkanService();
		CkanResponse response= s.getCkanEntities();
		assertTrue("succes was not set", response.success);
		assertTrue("result was not set", response.result!=null);
		assertTrue("fields was not set", response.result.fields!=null);
		assertEquals("fields had wrong length", 4, response.result.fields.size());
		assertTrue("results had unexpected length " + response.result.records.size(), response.result.records.size()>100 ); //This may change in the future...
	}
}
