//$Id$
/**
 * 
 */
package com.share.client.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author sudharsan-2598
 *
 */
public class Accounts
{
	private String email;
	private String cookie;
	private long mobile;
	private boolean isMale;
	private String name;
	private long uid;
	private JSONObject json;
	private String postalAddress;
	
	public void initialize(JSONObject userAccount) throws JSONException
	{
		setUid(userAccount.getLong("USERS_ID"));
		setEmail(userAccount.getString("EMAIL").trim());
		setMale(userAccount.getBoolean("IS_MALE"));
		
		if(!userAccount.isNull("NAME"))
		{
			setName(userAccount.getString("NAME"));
		}
		
		if(!userAccount.isNull("COOKIE"))
		{
			setCookie(userAccount.getString("COOKIE"));
		}
		
		if(!userAccount.isNull("MOBILE"))
		{
			setMobile(userAccount.getLong("MOBILE"));
		}
		this.json = userAccount;
	}
	
	
	
	public Accounts()
	{
		
	}

	public JSONObject getJSONObject()
	{
		return this.json;
	}
	
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getCookie()
	{
		return cookie;
	}
	public void setCookie(String cookie)
	{
		this.cookie = cookie;
	}
	public long getMobile()
	{
		return mobile;
	}
	public void setMobile(long mobile)
	{
		this.mobile = mobile;
	}
	public boolean isMale()
	{
		return isMale;
	}
	public void setMale(boolean isMale)
	{
		this.isMale = isMale;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public long getUid()
	{
		return uid;
	}
	public void setUid(long uid)
	{
		this.uid = uid;
	}
	
	public String getPostalAddress()
	{
		return postalAddress;
	}

	public void setPostalAddress(String postalAddress)
	{
		this.postalAddress = postalAddress;
	}



	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("email: ");
		builder.append(email);
		builder.append(" cookie: ");
		builder.append(cookie);
		builder.append(" mobile: ");
		builder.append(mobile);
		builder.append(" isMale: ");
		builder.append(isMale);
		builder.append(" name: ");
		builder.append(name);
		builder.append(" uid: ");
		builder.append(uid);
		return builder.toString();
	}
}
