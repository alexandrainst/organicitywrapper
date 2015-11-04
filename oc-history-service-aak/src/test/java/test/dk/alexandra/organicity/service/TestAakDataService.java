package test.dk.alexandra.organicity.service;

import java.io.IOException;

import org.junit.Test;

import dk.alexandra.organicity.service.AakDataService;

public class TestAakDataService {
	@Test
	public void testService() throws IOException {
		AakDataService s = new AakDataService();
		s.getEntities();
	}
}
