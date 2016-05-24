//$Id$
/**
 * 
 */

package com.share.error;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.server.util.CommonUtil;

/**
 * @author sudharsan-2598
 *
 */
public class MessageConstants
{
	private static Logger logger = Logger.getLogger(MessageConstants.class.getName());

	public static Map<Integer, JSONObject> error =null;

	static
	{
		setGeneralErrorMap();
	}
	
	public static JSONObject getError(int code)
	{
		try
		{
			JSONObject msg = error.get(code);
			if (CommonUtil.isValid(msg))
			{
				return msg;
			}
			else
			{
				JSONObject json = new JSONObject();
				json.put("code", 500);
				json.put("message", "Internal Error");
				return json;
			}
		} catch (Exception e)
		{
			logger.log(Level.WARNING, "Exception Occured", e);
			return error.get(500);
		}
	}

	public static JSONObject generateJsonForMsg(int code,String msg)
	{
		try
		{
			JSONObject json = new JSONObject();
			json.put("code", code);
			json.put("message", msg);
			return json;
		}
		catch(Exception e)
		{
			JSONObject json = new JSONObject();
			try
			{
				json.put("code", 500);
				json.put("message", "Internal Error");
			} catch (JSONException e1)
			{
				logger.log(Level.WARNING, "Exception Occured", e1);
			}
			return json;
		}
	}
	
	private static void setGeneralErrorMap()
	{
		try
		{

			if(error == null)
			{
				error = new HashMap<>();
				error.put(500, new JSONObject("{\"code\":500,\"message\":\"Internal Error\"}"));
				error.put(400, new JSONObject("{\"code\":400,\"message\":\"You are not signed in\"}"));

			}
		}
		catch(Exception e)
		{
			logger.log(Level.WARNING, "Exception Occured", e);
		}
	}
	public static final int NO_RESPONSE = -1;
	
	public static final int OP_SUCCESS = 1;

	public static final int INVALID_PARAM = 52;

	public static final int UNKNOWN_ERROR = 51;

	public static final int DEBUG_MESSAGE = 0;
	
	public static final int NO_CONFIRMED_SEAT=153;
	
	public static final int CHART_PREPARED=154;
	
	public static final int DUPLICATE_RESOURCE = 155;

	public static final int TEMP_ERROR=53;

	public static final int EXPIRED_JOURNEY = 156;
	
}
