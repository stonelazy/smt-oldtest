//$Id$
/**
 * 
 */
package com.share.error;

/**
 * @author sudharsan-2598
 *
 */

public class ShareException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	private int errorCode;
	private String message;
	
	public ShareException(){
		this.message = null;
	}
	
	public ShareException(int errorCode, String message){
		this.setErrorCode(errorCode);
		this.setMessage(message);
	}

	public int getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(int errorCode)
	{
		this.errorCode = errorCode;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
}
