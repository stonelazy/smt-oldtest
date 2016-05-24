//$Id$
/**
 * 
 */

package com.share.sql.metadata;

/**
 * @author sudharsan-2598
 *
 */
public class Passengers
{
	/*
	  CREATE TABLE Passengers
	  ( 
	  PASSENGERS_ID BIGSERIAL PRIMARY KEY, 
	  CURRENT_STATUS text, 
	  IDENTIFY_PASSENGER text,
	  BOOKING_STATUS text,
	  PNR_NUMBER BIGINT,
	  USERS_ID BIGINT
	  ); 
	  alter sequence passengers_passengers_id_seq start 13000 cache 2;
	 */
	public static final String TABLE_NAME = "Passengers";
	public static final String PASSENGERS_ID = "PASSENGERS_ID";
	public static final String CURRENT_STATUS = "CURRENT_STATUS";
	public static final String IDENTIFY_PASSENGER = "IDENTIFY_PASSENGER";
//	public static final String COACH_POSITION = "COACH_POSITION";
	public static final String BOOKING_STATUS = "BOOKING_STATUS";
	public static final String PNR_ID = Sell.PNR;
	public static final String USERS_ID = Users.USERS_ID;
}
