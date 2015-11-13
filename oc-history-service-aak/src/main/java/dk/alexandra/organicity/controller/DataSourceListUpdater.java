package dk.alexandra.organicity.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dk.alexandra.organicity.model.organicity.Device;
import dk.alexandra.organicity.service.AakDataService;

public class DataSourceListUpdater implements Runnable {

	@Override
	public void run() {
		System.out.println("starting to load source updates");
		AakDataService service = AakDataService.createService();
		List<Device> devices;
		try {
			devices = service.getEntities();
			update(devices);
		} catch (IOException e) {
			System.out.println("Entities update failed");
			e.printStackTrace();
			update(null);
		} catch (ParseException e) {
			System.out.println("Entities update failed");
			e.printStackTrace();
			update(null);
		}		
		System.out.println("complete: starting to load source updates");
	}

	public static void update(List<Device> devices) {
		SimpleCache.Element cache = null;
		
		Object res = devices;
		if (devices==null) {
			cache = SimpleCache.getCache().getElement(DataSourceController.DATA_SOURCE_CACHE_KEY);
			if (cache!=null)
				res = cache.value; //use previous value
			else
				res = new ArrayList<Device>(); //fallback - no sources to return
		}
		cache = new SimpleCache.Element(res);
		SimpleCache.getCache().addElement(DataSourceController.DATA_SOURCE_CACHE_KEY, cache);
	}

	public static void runUpdater() {
		ExecutorService executor = Executors.newFixedThreadPool(1);
		Runnable worker = new DataSourceListUpdater();
		executor.execute(worker);
	}
}
