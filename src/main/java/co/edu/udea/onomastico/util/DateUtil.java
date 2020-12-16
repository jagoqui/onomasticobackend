package co.edu.udea.onomastico.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	 
	private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 
	public static java.util.Date parseDate(String date) {
	    try {
	        return DATE_FORMAT.parse(date);
	    } catch (ParseException e) {
	        throw new IllegalArgumentException(e);
	    }
	}
	 
	public static java.util.Date parseTimestamp(String timestamp) {
	    try {
	        return DATE_TIME_FORMAT.parse(timestamp);
	    } catch (ParseException e) {
	        throw new IllegalArgumentException(e);
	    }
	}
	
	public static int getCurrentYear() {
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.MONTH);
    }

    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.DATE);
    }

    public static int getDayFromDate(Date date) {
        if (null == date) return 0;
        else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
            return Integer.parseInt(dateFormat.format(date));
        }
    }

    public static int getMonthFromDate(Date date) {
        if (null == date)return 0;
        else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
            return Integer.parseInt(dateFormat.format(date));
        }
    }
    
    public static String getCurrentDatetime() {
    	Date date = Calendar. getInstance(). getTime();
    	DateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
    	return dateFormat.format(date);
    }
	
}
