//$Id$
/**
 * 
 */
package com.share.bean;

import org.apache.commons.lang3.BooleanUtils;

/**
 * @author sudharsan-2598
 *
 */
public class Day
{
//	 {
//  "day-code": "MON",
//  "runs": "N"
//},
	
	private String day;
	private boolean isRunning;
	
	public String getDay()
	{
		return day;
	}
	public void setDay(String day)
	{
		this.day = day;
	}
	public boolean isRunning()
	{
		return isRunning;
	}
	public void setRunning(boolean isRunning)
	{
		this.isRunning = isRunning;
	}
	
	public void setRunning(String running)
	{
		this.isRunning = BooleanUtils.toBoolean(running);
	}
}
