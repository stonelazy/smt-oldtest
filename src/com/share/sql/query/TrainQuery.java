//$Id$
/**
 * 
 */
package com.share.sql.query;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author sudharsan-2598
 *
 */
public interface TrainQuery
{

	int addTicket(int trainNumber, long pnrNumber, String fromStationCode, String toStationCode, Date doj, int extraPassenger, long uid);

	JSONArray getSharedTickets(long uid);

	int addShareablePassenger(long pnr, String identifyPassenger, long uid, String currentStatus, String bookingStatus);

	int updateSellPnr(long pnrNumber, int notifyStatus, int extraPassengerCount, int mediate);

	JSONObject getSellPnrJson(long pnr);

	JSONArray getAllStationInfo();

}
