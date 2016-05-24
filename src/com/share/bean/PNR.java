//$Id$
/**
 * 
 */

package com.share.bean;

import java.util.Date;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.share.util.TrainUtil;

/**
 * @author sudharsan-2598
 *
 */

/*
 * for different quotas - http://www.indianrail.gov.in/quota_Code.html http://checkpnrstatusirctc.in/2014/07/railway-codes-pnr-status.html
 * 
 * coach code - http://checkpnrstatusirctc.in/2014/07/coach-codes-used-by-irctc-and-its.html
 * 
 */
public class PNR
{
	private static Logger logger = Logger.getLogger(PNR.class.getName());
	
	private JSONArray passengers;
	private int totalPassengersCount;
	private int trainNumber;
	private Date doj;
	private Station fromStation;
	private Station toStation;
	private long pnrNumber;
	private boolean isChartPrepared;
	private Date trainStartDate;
	private int responseCode;
	
	private String class_1;
	/*
	 * First class Air-Conditioned (AC) (Code:1A). The Executive class in Shatabdi type trains is also treated as Ist AC. AC 2-tier sleeper (Code:2A) First class (Code:FC) AC 3 Tier (Code:3A) 3 E - AC 3 Tier Economy AC chair Car (Code:CC) Sleeper Class (Code:SL) Second Sitting (Code:2S)
	 */

	private String trainName;
	private boolean isError;
	private Boolean isSellable = null;

	public boolean isSellable()
	{
		if (isSellable == null)
		{
			this.isSellable = !TrainUtil.getSellableSeats(this).isEmpty();
		}
		return this.isSellable;
	}

	public void setSellable(boolean sellable)
	{
		this.isSellable = sellable;
	}

	public JSONArray getPassengers()
	{
		return passengers;
	}

	public int getResponseCode()
	{
		return this.responseCode;
	}
	
	public void addPassenger(Passenger passenger)
	{
		if (this.passengers == null)
		{
			this.passengers = new JSONArray();
		}
		this.passengers.put(passenger);
	}

	public void setPassengers(JSONArray passengers) throws JSONException
	{
		for (int i = passengers.length() - 1; i >= 0; i--)
		{
			Passenger passenger = new Passenger();
			passenger.initialize(passengers.getJSONObject(i));
			addPassenger(passenger);
		}
		
		Passenger passenger = new Passenger();
		 passenger.setCurrentStatus("CNF");
		 passenger.setCoachPosition(2);
		 passenger.setBookingStatus("D4,15,WL");
		addPassenger(passenger);
		
		Passenger passenge1r = new Passenger();
		passenge1r.setCurrentStatus("CNF");
		passenge1r.setCoachPosition(3);
		passenge1r.setBookingStatus("D3,33,WL1");
		addPassenger(passenge1r);
	}
	public int getTotalPassengersCount()
	{
		return totalPassengersCount;
	}
	public void setTotalPassengersCount(int totalPassengersCount)
	{
		this.totalPassengersCount = totalPassengersCount;
	}
	public int getTrainNumber()
	{
		return trainNumber;
	}
	public void setTrainNumber(int trainNumber)
	{
		this.trainNumber = trainNumber;
	}
	public Date getDoj()
	{
		return doj;
	}
	public void setDoj(Date doj)
	{
		this.doj = doj;
	}
	public Station getFromStation()
	{
		return fromStation;
	}
	public void setFromStation(JSONObject fromStation) throws JSONException
	{
		logger.info("from station>> " + fromStation);
		Station station = new Station();
		station.initialize(fromStation);
		this.fromStation = station;
	}
	public Station getToStation()
	{
		return toStation;
	}
	public void setToStation(JSONObject toStation) throws JSONException
	{
		Station station = new Station();
		station.initialize(toStation);
		this.toStation = station;
	}
	public long getPnrNumber()
	{
		return pnrNumber;
	}
	public void setPnrNumber(long pnrNumber)
	{
		this.pnrNumber = pnrNumber;
	}
	public boolean isChartPrepared()
	{
		return isChartPrepared;
//		return true;
	}
	public void setChartPrepared(boolean isChartPrepared)
	{
		this.isChartPrepared = isChartPrepared;
	}
	public Date getTrainStartDate()
	{
		return trainStartDate;
	}
	public void setTrainStartDate(Date trainStartDate)
	{
		this.trainStartDate = trainStartDate;
	}
	public String getClass_1()
	{
		return class_1;
	}
	public void setClass_1(String class_1)
	{
		this.class_1 = class_1;
	}
	public String getTrainName()
	{
		return trainName;
	}
	public void setTrainName(String trainName)
	{
		this.trainName = trainName;
	}
	public boolean isError()
	{
		return isError;
	}
	public void setError(boolean isError)
	{
		this.isError = isError;
	}

	public void setResponseCode(int intValue)
	{
		this.responseCode = intValue;
	}
	

}
