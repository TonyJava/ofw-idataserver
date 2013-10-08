package com.htong.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {
	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		String time = sdf.format(c.getTime());
		return time;
	}

}
