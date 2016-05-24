//$Id$
/**
 * 
 */
package com.share.bean;

import org.json.JSONException;
import org.json.JSONObject;

import com.share.constants.APIConstants;
import com.share.constants.SymbolConstants;

import sun.security.action.GetBooleanSecurityPropertyAction;

/**
 * @author sudharsan-2598
 *
 */
public class Passenger
{

	/*
	 * 				passengers: -[-{
					booking_status: "D2,25,GN",
					current_status: "CNF",
					no: 1,
					coach_position: 0
				}],

	 */
	
	private String currentStatus;
	private int coachPosition = 0;
	private String bookingStatus;
	private long passengerId;
	private String identifyPassenger;
	
	public String getIdentifyPassenger()
	{
		return this.identifyPassenger;
	}
	
	
	public long getPassengerId()
	{
		return this.passengerId;
	}
	
	public void setPassengerId(long passengerId)
	{
		this.passengerId=passengerId;
	}
	
	public String getCurrentStatus()
	{
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus)
	{
		this.currentStatus = currentStatus;
	}
	public int getCoachPosition()
	{
		return coachPosition;
	}
	public void setCoachPosition(int coachPosition)
	{
		this.coachPosition = coachPosition;
	}
	
	public String getBookingStatus()
	{
		return bookingStatus;
	}
	
	public void setBookingStatus(String bookingStatus)
	{
		this.bookingStatus = bookingStatus;
		
		if(bookingStatus.contains(SymbolConstants.COMMA))
		{
			String[] bookingStatusArr = bookingStatus.split(SymbolConstants.COMMA);
			this.identifyPassenger = bookingStatusArr[0] + SymbolConstants.HYPHEN + bookingStatusArr[1];
		}
	}
	
	@Override
	public String toString()
	{
		JSONObject printJson = new JSONObject();
		try
		{
			printJson.put(APIConstants.CURRENT_STATUS, getCurrentStatus());
			printJson.put(APIConstants.COACH_POSITION, getCoachPosition());
			printJson.put(APIConstants.BOOKING_STATUS, getBookingStatus());
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append(APIConstants.CURRENT_STATUS);
		builder.append(":");
		builder.append(getCurrentStatus());
		builder.append(" ,");
		builder.append(APIConstants.COACH_POSITION);
		builder.append(":");
		builder.append(getCoachPosition());
		builder.append(" ,");
		builder.append(APIConstants.BOOKING_STATUS);
		builder.append(":");
		builder.append(getBookingStatus());
		return builder.toString();
	}
	
	public void initialize(JSONObject json) throws JSONException
	{
		if(!json.isNull(APIConstants.BOOKING_STATUS))
		{
			this.setBookingStatus(json.getString(APIConstants.BOOKING_STATUS));
		}
		
		if(!json.isNull(APIConstants.CURRENT_STATUS))
		{
			this.setCurrentStatus(json.getString(APIConstants.CURRENT_STATUS));
		}
		
		if(!json.isNull(APIConstants.COACH_POSITION))
		{
			this.setCoachPosition(json.getInt(APIConstants.COACH_POSITION));
		}
		
		this.passengerId = System.nanoTime();
	}
	
	
}
