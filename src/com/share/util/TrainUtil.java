//$Id$
/**
 * 
 */

package com.share.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;

import com.server.constants.RegexConstants;
import com.share.bean.PNR;
import com.share.bean.Passenger;
import com.share.error.MessageConstants;

/**
 * @author sudharsan-2598
 *
 */
public class TrainUtil
{
	private static Logger logger = Logger.getLogger(TrainUtil.class.getName());
	
	
	public static Map<Integer, Passenger> getSellableSeats(PNR pnr)
	{
		Map<Integer, Passenger> sellable = new HashMap<>();

		try
		{
			if (pnr.isError() || pnr.isChartPrepared())
			{
				logger.info("pnr is not sellable, pnr>> " + pnr.isError() + " chart status>> " + pnr.isChartPrepared());
			}
			else
			{
				JSONArray passengers = pnr.getPassengers();

				logger.info("passengers count>> " + passengers);

				for (int i = 0, j = 0; i < pnr.getTotalPassengersCount(); i++)
				{
					try
					{
						String currentStatus = ((Passenger) passengers.get(i)).getCurrentStatus();

						if (isPassengerStatusSellable(currentStatus))
						{
							logger.info("regex constant matched to>> " + currentStatus + " and so added in sellable map");
							sellable.put(++j, (Passenger) passengers.get(i));
						}
						else
						{
							logger.info("regex constant matched to>> " + currentStatus + " and so discarding");
						}
					} catch (JSONException e)
					{
						logger.info("problem with passenger count>> " + passengers.get(i));
						logger.log(Level.WARNING, "Exception Occured", e);
					}
				}
			}
		} catch (Exception e)
		{
			logger.log(Level.WARNING, "Exception Occured", e);
		} finally
		{
			logger.info("is sellable>> " + !sellable.isEmpty());
			pnr.setSellable(!sellable.isEmpty());
		}
		return sellable;
	}
	
	public static boolean isPassengerStatusSellable(String currentStatus)
	{
		return !RegexConstants.INVALID_TICKET_PATTERN.matcher(currentStatus).matches();
	}
	
	public static int isTicketSellable(PNR pnr)
	{
		int response = MessageConstants.NO_RESPONSE;
		try
		{
			if (pnr.isError())
			{
				logger.info("pnr is not sellable, pnr>> " + pnr.isError() );
				response = MessageConstants.TEMP_ERROR;
			}
			else if(pnr.isChartPrepared())
			{
				logger.info("rejected coz chart is prepared..");
				response = MessageConstants.CHART_PREPARED;
			}
			else
			{
				long doj = pnr.getDoj().getTime();
				
				if (doj < System.currentTimeMillis())
				{
					response = MessageConstants.EXPIRED_JOURNEY;
					logger.info("bastard.. trying to sell an expired ticket");
				}
				
				if (response == MessageConstants.NO_RESPONSE)
				{
					JSONArray passengers = pnr.getPassengers();

					logger.info("passengers count>> " + passengers);

					for (int i = 0; i < pnr.getTotalPassengersCount(); i++)
					{
						try
						{
							String currentStatus = ((Passenger) passengers.get(i)).getCurrentStatus();

							if (RegexConstants.INVALID_TICKET_PATTERN.matcher(currentStatus).matches())
							{
								logger.info("regex constant matched to>> " + currentStatus + " and so discarding");
							}
							else
							{
								logger.info("regex constant matched to>> " + currentStatus + " and so added in sellable map");
								response = MessageConstants.OP_SUCCESS;
								break;
							}
						} catch (JSONException e)
						{
							logger.info("problem with passenger count>> " + passengers.get(i));
							logger.log(Level.WARNING, "Exception Occured", e);
						}
					}
				}
			}
		} catch (Exception e)
		{
			logger.log(Level.WARNING, "Exception Occured", e);
		} finally
		{
			logger.info("is sellable>> " + response);
		}
		return response;
	
	}
	
	
}
