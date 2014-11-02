package com.familybiz.greg.battleship.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Greg Anderson
 */
public class DateParser {

	/**
	 * Converts the string form of a date to a Date.
	 */
	public static Date stringToDate(String date) {
		Date result = null;
		SimpleDateFormat dateParser = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		try {
			result = dateParser.parse(date);
		}
		catch(ParseException e) {
			Log.e("BAD DATE", "Parsing error, invalid date.");
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * Changes the string format of a string to be smaller and hopefully easier to read.
	 */
	public static String convertDateToString(Date date) {
		return date.toString();
		/*
		String sDate = date.toString();
		return sDate.substring(sDate.indexOf(' ')+1, sDate.length()-9);
		*/
	}
}
