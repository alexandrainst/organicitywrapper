package dk.aarhus.organicity.controller;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple memory based, synchronized cache of strings.
 * @author neerbek
 *
 */
public class SimpleCache {
	public static class Element {
		public final Object value;
		public final long timestamp;
		//changes to state must only happen from SimpleCache instance
		private long validUntil=0;
		private boolean isBeginUpdated;
		
		public Element(Object value) {
			this.isBeginUpdated = false;
			this.value = value;
			this.timestamp = System.currentTimeMillis();
		}
		
		public boolean isValid() {
			return (System.currentTimeMillis()<validUntil);
		}

		private void updateValidTime(long period) {
			validUntil = timestamp + period;
		}
		
	}
	
	private Map<String, Element> elements = new HashMap<String, Element>();
	private static long VALID_PERIODE = 120000; //2 minutes 
	public void addElement(String key, Element e) {
		synchronized (this) {
			elements.put(key, e);
			e.updateValidTime(VALID_PERIODE);
		}
	}
	
	public Element getElement(String key) {
		synchronized (this) {
			return elements.get(key);
		}
	}
	
	/**
	 * Marks element as being updated. Returns true if this call marked it (only the thread receiving true should update, ow. you run update more than once)
	 * @param e
	 * @return
	 */
	public boolean doUpdate(Element e) {
		synchronized (this) {
			if (!e.isBeginUpdated) {
				e.isBeginUpdated = true;
				return true;
			}
		}	
		return false;
	}
	
	private static SimpleCache simpleCache = new SimpleCache();
	public static SimpleCache getCache() {
		return simpleCache;
	}
}
