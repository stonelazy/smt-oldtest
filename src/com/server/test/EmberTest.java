//$Id$
/**
 * 
 */
package com.server.test;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.server.util.CommonUtil;

/**
 * @author sudharsan-2598
 *
 */
public class EmberTest
{
	private String mode;
	private boolean different;
	private JSONArray piedata = getpiejson(false);
	
	public String execute() throws JSONException{
		
		if(mode.equals("pie"))
		{
			if(different)
			{
				CommonUtil.writeResponse(getpiejson(true).toString());
			}
			CommonUtil.writeResponse(getpiejson(false).toString());
		}
		else if(mode.equals("routepie"))
		{
			JSONArray series = new JSONArray();
			JSONArray actualdata = getpiejson(true);
			JSONObject dataObj = new JSONObject();
			dataObj.put("data",actualdata );
			series.put(dataObj);
			CommonUtil.writeResponse(series.toString());
		}
		return "none";
	}
	
	
	public String getMode()
	{
		return mode;
	}


	public void setMode(String mode)
	{
		this.mode = mode;
	}


	public boolean isDifferent()
	{
		return different;
	}


	public void setDifferent(boolean different)
	{
		this.different = different;
	}


	public JSONArray getPiedata()
	{
		return piedata;
	}


	public void setPiedata(JSONArray piedata)
	{
		this.piedata = piedata;
	}


	public static JSONArray getpiejson(boolean different)
	{
		JSONArray total = new JSONArray();
		JSONArray keyvalue = new JSONArray();
		keyvalue.put("browser");
		keyvalue.put(172 + new Random().nextInt(390));
		total.put(keyvalue);
		keyvalue = new JSONArray();
		keyvalue.put("unknown");
		keyvalue.put(187+ new Random().nextInt(390));
		total.put(keyvalue);
		return total;
	}
}
