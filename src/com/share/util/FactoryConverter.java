//$Id$
/**
 * 
 */

package com.share.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import com.server.util.CommonUtil;
import com.server.util.DateUtil;
import com.share.bean.Day;
import com.share.bean.PNR;
import com.share.bean.ReservationClass;
import com.share.bean.Station;
import com.share.bean.Train;
import com.share.constants.APIConstants;

/**
 * @author sudharsan-2598
 *
 */
public class FactoryConverter
{
	private static Logger logger = Logger.getLogger(FactoryConverter.class.getName());

	public static void getPnrObject(PNR pnr, JSONObject json)
	{
		if (pnr != null)
		{
			Iterator<String> keys = json.keys();

			/*
			{
				pnr: "4631174536",
				from_station: -{
					name: "TIRUCHCHIRAPALI",
					code: "TPJ"
				},
				passengers: -[-{
					booking_status: "D2,25,GN",
					current_status: "CNF",
					no: 1,
					coach_position: 0
				}],
				total_passengers: 1,
				class: "2S",
				boarding_point: -{
					name: "TIRUCHCHIRAPALI",
					code: "TPJ"
				},
				chart_prepared: "N",
				train_start_date: -{
					month: 7,
					day: 24,
					year: 2016
				},
				doj: "25-7-2016",
				reservation_upto: -{
					name: "KULITALAI",
					code: "KLT"
				},
				response_code: 200,
				error: false,
				train_num: "16859",
				train_name: "MS MANGALORE EX",
				to_station: -{
					name: "KULITALAI",
					code: "KLT"
				}
			}
				 */
			
			try
			{
				boolean isError = json.isNull(APIConstants.ERROR) ? false: json.getBoolean(APIConstants.ERROR);

				if(!isError)
				{
					while (keys.hasNext())
					{
						String key = keys.next();
						Object value = json.get(key);
						logger.info("key>> " + key + " value>> " + value);
						if(CommonUtil.isValid(value))
						{
							if(key.equalsIgnoreCase(APIConstants.PNR))
							{
								pnr.setPnrNumber(Long.parseLong(value.toString()));
							}
							else if(key.equalsIgnoreCase(APIConstants.FROM_STATION))
							{
								pnr.setFromStation(new JSONObject(value.toString()));
							}
							else if(key.equalsIgnoreCase(APIConstants.PASSENGERS))
							{
								pnr.setPassengers(new JSONArray(value.toString()));
							}
							else if(key.equals(APIConstants.TOTAL_PASSENGERS))
							{
								pnr.setTotalPassengersCount(Integer.parseInt(value.toString()));
							}
							else if(key.equalsIgnoreCase(APIConstants.class_1))
							{
								pnr.setClass_1(value.toString());
							}
							else if(key.equalsIgnoreCase(APIConstants.CHART_PREPARED))
							{
								pnr.setChartPrepared(value.toString().equals("Y"));
							}
							else if(key.equalsIgnoreCase(APIConstants.TRAIN_START_DATE))
							{
								pnr.setTrainStartDate(DateUtil.formatDate(new JSONObject(value.toString())));
							}
							else if(key.equalsIgnoreCase(APIConstants.DOJ))
							{
								pnr.setDoj(DateUtil.formatDate(value.toString()));
							}
							else if(key.equals(APIConstants.RESPONSE_CODE))
							{
								pnr.setResponseCode(Integer.valueOf(value.toString()).intValue());
							}
							else if(key.equals(APIConstants.TRAIN_NUM))
							{
								pnr.setTrainNumber(Integer.valueOf(value.toString()));
							}
							else if(key.equals(APIConstants.TRAIN_NAME))
							{
								pnr.setTrainName(value.toString());
							}
							else if(key.equals(APIConstants.TO_STATION))
							{
								pnr.setToStation(new JSONObject(value.toString()));
							}
						}
					}
				}
				else
				{
					logger.info("it was an empty response from railapi..");
					pnr.setError(true);
				}
			} catch (Exception e)
			{
				logger.log(Level.WARNING, "Exception Occured", e);
			}

		}
		else
		{
			getPnrObject(new PNR(), json);
		}
	}
	
	public static Train getTrainObject(JSONObject trainJson)
	{
		Iterator<String> keys = trainJson.keys();
		Train train = new Train();
		try
		{
			boolean isError = (!(trainJson.isNull(APIConstants.ERROR) || trainJson.getBoolean(APIConstants.ERROR))) || (!(trainJson.isNull(APIConstants.RESPONSE_CODE) || trainJson.getInt(APIConstants.RESPONSE_CODE) < 300));

			if (!isError)
			{
				while (keys.hasNext())
				{
					String key = keys.next();
					Object value = trainJson.get(key);

					logger.info("trainJson key>> " + key + " trainJson value>> " + value);

					if (CommonUtil.isValid(value))
					{
						if (key.equalsIgnoreCase(APIConstants.NUMBER))
						{
							train.setTrainNumber(Long.parseLong(value.toString()));
						}
						else if (key.equalsIgnoreCase(APIConstants.FROM))
						{
							Station station = new Station();
							JSONObject fromJson = (JSONObject) value;
							station.setCode(fromJson.getString(APIConstants.CODE));
							station.setCode(fromJson.getString(APIConstants.NAME));
							train.setFrom(station);
						}
						else if (key.equalsIgnoreCase(APIConstants.TO))
						{
							Station station = new Station();
							JSONObject toJson = (JSONObject) value;
							station.setCode(toJson.getString(APIConstants.CODE));
							station.setCode(toJson.getString(APIConstants.NAME));
							train.setTo(station);
						}
						else if (key.equals(APIConstants.SRC_DEPARTURE_TIME))
						{
							train.setSrc_departure_time(value.toString());
						}
						else if (key.equalsIgnoreCase(APIConstants.DEST_ARRIVAL_TIME))
						{
							train.setDest_arrival_time(value.toString());
						}
						else if (key.equalsIgnoreCase(APIConstants.TRAVEL_TIME))
						{
							train.setTravelTime(value.toString());
						}
						else if (key.equalsIgnoreCase(APIConstants.NAME))
						{
							train.setTrainName(value.toString());
						}
						else if (key.equalsIgnoreCase(APIConstants.CLASSES))
						{
							JSONArray classes = (JSONArray) value;
							List<ReservationClass> reservationClass = new ArrayList<>();

							for (int i = classes.length() - 1; i >= 0; i--)
							{
								ReservationClass resvn = new ReservationClass();
								JSONObject claz = classes.getJSONObject(i);
								resvn.setAvailable(claz.getString(APIConstants.AVAILABLE));
								resvn.setClassCode(claz.getString(APIConstants.CLASS_CODE));
								reservationClass.add(resvn);
							}
							train.setClasses(reservationClass);
						}
						else if (key.equals(APIConstants.DAYS))
						{
							JSONArray daysArr = (JSONArray) value;
							List<Day> days = new ArrayList<>();

							for (int i = daysArr.length() - 1; i >= 0; i--)
							{
								Day day = new Day();
								JSONObject dayJson = daysArr.getJSONObject(i);
								day.setDay(dayJson.getString(APIConstants.DAY_CODE));
								day.setRunning(dayJson.getString(APIConstants.RUNS));
								days.add(day);
							}
							train.setDays(days);
						}
					}
				}
			}
			else
			{
				logger.info("it was an empty response from railapi..");
				train.setError(true);
			}
		} catch (Exception e)
		{
			logger.log(Level.WARNING, "Exception Occured", e);
		}
		return train;
		
		/*
        "no": 1,
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
                "day-code": "SUN",
                "runs": "Y"
            }
        ]
    }
	*/
	}
}
