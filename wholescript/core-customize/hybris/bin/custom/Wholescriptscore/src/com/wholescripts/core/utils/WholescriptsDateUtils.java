
package com.wholescripts.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class WholescriptsDateUtils {
	
	 private final static String UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	 
    
    public static String convertDateInUTCFormat(final Date date) {
   	 
   	 final SimpleDateFormat sdf = new SimpleDateFormat(UTC_FORMAT);
   	 final TimeZone utc = TimeZone.getTimeZone("UTC");
   	 sdf.setTimeZone(utc);
   	 return sdf.format(date);
    }
    
    
}
