package test.dk.alexandra.organicity.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import dk.alexandra.organicity.model.ckan.CkanResponse;
import dk.alexandra.organicity.service.AakDataService;

public class TestAakDataService {
	@Test
	public void testService() throws IOException {
		AakDataService s = new AakDataService();
		s.getEntities();
	}

	@Test
	public void testCkanService() throws IOException {
		AakDataService s = new AakDataService();
		CkanResponse response= s.getCkanEntities();
		assertTrue("succes was not set", response.success);
		assertTrue("result was not set", response.result!=null);
		assertTrue("fields was not set", response.result.fields!=null);
		assertEquals("fields had wrong length", response.result.fields.size(), 4);
		assertEquals("results had wrong length", response.result.records.size(), 172); //This may change in the future...
	}
}
