//$Id$
/**
 * 
 */
package com.share.bean;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author sudharsan-2598
 *
 */
public class Train
{
	private static Logger logger = Logger.getLogger(Train.class.getName());
	
	private String trainName;
	private Long trainNumber;
	private String src_departure_time;
	private String dest_arrival_time;
	private Station from;
	private Station to;
	private List<ReservationClass> classes;
	private List<Day> days;
	private String travelTime;
	private boolean error;
	
	public boolean getIsError()
	{
		return error;
	}
	
	public Long getTrainNumber()
	{
		return trainNumber;
	}
	public void setTrainNumber(Long trainNumber)
	{
		this.trainNumber = trainNumber;
	}
	public String getSrc_departure_time()
	{
		return src_departure_time;
	}
	public void setSrc_departure_time(String src_departure_time)
	{
		this.src_departure_time = src_departure_time;
	}
	public String getDest_arrival_time()
	{
		return dest_arrival_time;
	}
	public void setDest_arrival_time(String dest_arrival_time)
	{
		this.dest_arrival_time = dest_arrival_time;
	}
	public Station getFrom()
	{
		return from;
	}
	public void setFrom(Station from)
	{
		this.from = from;
	}
	public Station getTo()
	{
		return to;
	}
	public void setTo(Station to)
	{
		this.to = to;
	}
	public List<ReservationClass> getClasses()
	{
		return classes;
	}
	public void setClasses(List<ReservationClass> classes)
	{
		this.classes = classes;
	}
	public List<Day> getDays()
	{
		return days;
	}
	public void setDays(List<Day> days)
	{
		this.days = days;
	}
	public String getTravelTime()
	{
		return travelTime;
	}
	public void setTravelTime(String travelTime)
	{
		this.travelTime = travelTime;
	}
	public String getTrainName()
	{
		return trainName;
	}
	public void setTrainName(String trainName)
	{
		this.trainName = trainName;
	}
	public void setError(boolean b)
	{
		this.error=b;
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(" trainName>> " +trainName );
		builder.append(" trainNumber>> " +trainNumber );
		builder.append(" src_departure_time>> " +src_departure_time );
		builder.append(" dest_arrival_time>> " +dest_arrival_time );
		builder.append(" from>> " +from );
		builder.append(" to>> " +to );
		builder.append(" classes>> " +classes );
		builder.append(" days>> " +days );
		builder.append(" travelTime>> " +travelTime );
		builder.append(" error>> " +error );
		return builder.toString();
	}
	
	/*
	 *    "no": 1,
            "name": "RAPTI SAGAR EXP",
            "number": "12511",
            "src_departure_time": "06:35",
            "dest_arrival_time": "03:50",
            "travel_time": "21:15",
            "from": {
                "name": "GORAKHPUR JN",
                "code": "GKP"
            },
            "to": {
                "name": "NAGPUR",
                "code": "NGP"
            },
            "classes": [
                {
                    "class-code": "FC",
                    "available": "N"
                },
                {
                    "class-code": "3E",
                    "available": "N"
                },
                {
                    "class-code": "CC",
                    "available": "N"
                },
                {
                    "class-code": "SL",
                    "available": "Y"
                },
                {
                    "class-code": "2S",
                    "available": "N"
                },
                {
                    "class-code": "2A",
                    "available": "Y"
                },
                {
                    "class-code": "3A",
                    "available": "Y"
                },
                {
                    "class-code": "1A",
                    "available": "N"
                }
            ],
            "days": [
                {
                    "day-code": "MON",
                    "runs": "N"
                },
                {
                    "day-code": "TUE",
                    "runs": "N"
                },
                {
                    "day-code": "WED",
                    "runs": "N"
                },
                {
                    "day-code": "THU",
                    "runs": "Y"
                },
                {
                    "day-code": "FRI",
                    "runs": "Y"
                },
                {
                    "day-code": "SAT",
                    "runs": "N"
                },
                {
                    "day-code": "SUN",
                    "runs": "Y"
                }
            ]
        }
	 */
}
