package com.guangyi.finddoctor.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.net.ParseException;

public class WeekTool {
	public static String getWeek(String pTime) {

		String Week = "";

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {

			try {
				c.setTime(format.parse(pTime));
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			Week += "��";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 2) {
			Week += "һ";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 3) {
			Week += "��";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 4) {
			Week += "��";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 5) {
			Week += "��";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 6) {
			Week += "��";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 7) {
			Week += "��";
		}

		return Week;
	}

}
