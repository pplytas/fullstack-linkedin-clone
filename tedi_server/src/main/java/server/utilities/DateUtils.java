package server.utilities;

import java.util.Date;

public class DateUtils {

	public static boolean greaterEqualThanCurrent(Date d) {
		if (d == null || d.compareTo(new Date()) >= 0) {
			return true;
		}
		return false;
	}
	
	public static boolean lessEqualThanCurrent(Date d) {
		if (d == null) {
			return false;
		}
		if (d.compareTo(new Date()) <= 0) {
			return true;
		}
		return false;
	}
	
}
