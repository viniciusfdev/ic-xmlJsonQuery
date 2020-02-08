package util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Evandrino Barros, Jônatas Tonholo
 * @version 2.0
 */
public class DateUtils {
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

    /**
     * Returns the time now in format @DATE_FORMAT_NOW
     * @return
     */
    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }
}
