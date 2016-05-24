//$Id$
/**
 * 
 */
package com.share.bean;

import java.util.logging.Logger;

import org.apache.commons.lang3.BooleanUtils;

/**
 * @author sudharsan-2598
 *
 */
public class ReservationClass
{
	public static Logger logger =  Logger.getLogger(ReservationClass.class.getName());
	private String classCode;
	private boolean available;
	
//	 {
//         "class-code": "FC",
//         "available": "N"
//     },
	
	public String getClassCode()
	{
		return classCode;
	}
	public void setClassCode(String classCode)
	{
		this.classCode = classCode;
	}
	public boolean isAvailable()
	{
		return available;
	}
	
	public void setAvailable(boolean available)
	{
		this.available = available;
	}
	
	public void setAvailable(String available)
	{
		this.available = BooleanUtils.toBoolean(available);
	}
	
	public static void main(String[] args)
	{
		logger.info("bol value>> " +BooleanUtils.toBoolean("y"));
	}
}
