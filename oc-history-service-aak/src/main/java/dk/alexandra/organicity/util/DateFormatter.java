package dk.alexandra.organicity.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateFormatter {
  public static String getFormattedDate(Date d) {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
    df.setTimeZone(TimeZone.getTimeZone("UTC"));  //Zulu time - e.g. corresponds to the 'Z' literal above
    return df.format(d);
  }
	public static String getCurrentTime() {
		return getFormattedDate(new Date());
	}
	
	public static String formatToOrganicityDate(String d) throws ParseException {
	  SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	  return getFormattedDate(dateFormatLocal.parse(d));
	}
}
