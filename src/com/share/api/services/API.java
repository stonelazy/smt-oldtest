//$Id$
/**
 * 
 */
package com.share.api.services;

import java.util.List;

import org.json.JSONObject;

import com.share.bean.PNR;
import com.share.bean.Train;

/**
 * @author sudharsan-2598
 *
 */

public interface API{
	JSONObject getPnrStatus(long pnrNumber);
	PNR getPnrObject(long pnrNumber);
	List<Train> getTrainsBetween(String fromStz, String toStz, String date);
}