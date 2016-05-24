//$Id$
/**
 * 
 */
package com.share.sql.metadata;

/**
 * @author sudharsan-2598
 *
 */
public class StationInfo
{
	//http://www.indianrailways.gov.in/railwayboard/uploads/directorate/coaching/pdf/Station_code.pdf
	/*
	  CREATE TABLE StationInfo
	  ( 
	  CODE character(4) PRIMARY KEY, 
	  NAME text
	  ); 
	 */
	public static final String TABLE_NAME = "StationInfo";
	public static final String CODE = "CODE";
	public static final String NAME = "NAME";
}