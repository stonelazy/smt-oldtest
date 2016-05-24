//$Id$
/**
 * 
 */
package com.server.cache.redis;

/**
 * @author sudharsan-2598
 *
 */
public class RedisException extends Exception
{
	private int errorCode;
	private String errorDesc;
	
	public RedisException(String errorDesc)
	{
		this.errorDesc="Redis Error: " + errorDesc;
	}
	
	public RedisException (int errorCode, String errorDesc)
	{
		this.errorCode = errorCode;
		this.errorDesc = "Redis Error: " + errorDesc;
	}
	
	public int getErrorCode()
	{
		return this.errorCode;
	}
	
	public String getErrorDesc()
	{
		return this.errorDesc;
	}
	
}
