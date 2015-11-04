package dk.alexandra.organicity.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {
	public static String getCurrentTime() {
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ").format(new Date());
	}
}
