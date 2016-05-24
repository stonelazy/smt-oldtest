//$Id$
/**
 * 
 */

package com.share.bean;

import org.json.JSONException;
import org.json.JSONObject;

import com.share.constants.APIConstants;

/**
 * @author sudharsan-2598
 *
 */
public class Station
{
	private String code;
	private String name;

	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code = code;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}

	public void initialize(JSONObject json) throws JSONException
	{
		if (!json.isNull(APIConstants.CODE))
		{
			this.setCode(json.getString(APIConstants.CODE));
		}
		if (!json.isNull(APIConstants.NAME))
		{
			this.setName(json.getString(APIConstants.NAME));
		}
	}
}
