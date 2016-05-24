//$Id$
/**
 * 
 */

package com.server.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

/**
 * @author sudharsan-2598
 *
 */
public class DateUtil
{
	private static Logger logger = Logger.getLogger(DateUtil.class.getName());

	public static void main(String[] args)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");// No I18N
		Date date = null;
		try
		{
			date = formatter.parse("25-07-2016");
			logger.info("date>> " + date);

			DateFormat targetFormat = new SimpleDateFormat("dd MM yyyy");
			String formattedDate = targetFormat.format(date); // 20120821
			logger.info("formatted date>> " + formattedDate);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
	}

	public static Date formatDate(String dateString)
	{
		Date date = null;
		SimpleDateFormat formatter = null;
		try
		{
			formatter = new SimpleDateFormat("dd-MM-yyyy");// No I18N
			date = formatter.parse(dateString);
		} catch (ParseException e4)
		{

			// dateString="Sat, 11 Oct 2014 12:29:52 +0530";
			// Refer RFC822 for DateFormat
			formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");// No I18N
			try
			{
				date = formatter.parse(dateString);
				logger.fine(date.toString());
			} catch (ParseException e)
			{
				logger.info("Exception occured!! Sender hasnt followed RFC Standard in forming Date.. Trying with new format (Case Handled");
				// 2 Sep 2015 16:01:19 +0900
				formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss Z");// No I18N
				try
				{
					date = formatter.parse(dateString);
				} catch (ParseException e1)
				{
					logger.info("Sender hasn't followed RFC standard in forming date.. trying with 2nd format");
					formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");// No I18N
					try
					{
						// Tue Sep 01 00:03:01 PDT 2015
						date = formatter.parse(dateString);
					} catch (ParseException pe)
					{
						logger.warning("new case in date!! >> " + dateString);
					}
				}
			}

		}
		return date;
	}

	@SuppressWarnings("deprecation")
	public static Date formatDate(JSONObject dateJson)
	{
		/*
		 * { month: 7, day: 24, year: 2016 }
		 */
		Date date = null;
		try
		{
			if (!dateJson.isNull("day"))
			{
				date = new Date();
				date.setDate(dateJson.getInt("day"));
			}
			if (!dateJson.isNull("month"))
			{
				date.setDate(dateJson.getInt("month"));
			}
			if (!dateJson.isNull("year"))
			{
				date.setDate(dateJson.getInt("year"));
			}
		} catch (Exception e)
		{
			logger.log(Level.WARNING, "Exception Occured", e);
		}
		return date;
	}

	public static long getDaysInMilis(int days)
	{
		return days * 24 * 60 * 60 * 1000L;
	}

	public static long getHoursInMilis(int hours)
	{
		return hours * 60 * 60 * 1000L;
	}

	public static long getMinutesInMillis(long minutes)
	{
		return minutes * 60 * 1000L;
	}

	public static long getMillisInDays(long millis)
	{
		long days = millis / (1000L * 60 * 60 * 24);
		return days;
	}

	public static long getmillisInHours(long millis)
	{
		long hours = millis / (1000L * 60 * 60);
		return hours;
	}

	public static Long parseDateToMillis(String dateStr)
	{
		if (CommonUtil.isValid(dateStr))
		{
			try
			{
				Date date = new Date(dateStr);
				if (date != null)
				{
					return date.getTime();
				}
			} catch (Exception e)
			{
				Date date = formatDate(dateStr);
				return date.getTime();
			}
		}
		return null;
	}
}
