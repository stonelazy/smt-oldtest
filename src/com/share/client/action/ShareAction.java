//$Id$
/**
 * 
 */

package com.share.client.action;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.share.constants.ActionConstants;
import com.share.constants.StrutsConstants;

/**
 * @author sudharsan-2598
 *
 */
public class ShareAction
{
	private static Logger logger = Logger.getLogger(ShareAction.class.getName());
	private String mode;
	private String action;
	private String email;
	private String userName;
	private String message;

	HttpServletRequest request = ServletActionContext.getRequest();

	public String feedback()
	{
		logger.info("in1 the ShareAction/feedback class with mode>> " + getMode() + " and action>> " + getAction());

		String actionResult = StrutsConstants.NONE;

		if (getMode().equals(ActionConstants.FEEDBACK))
		{
			logger.info("mode>>> " + getMode());

			if (getAction().equals(ActionConstants.ADD))
			{
				logger.info("message>> " + getMessage());
			}
		}
		return actionResult;
	}

	public String getMode()
	{
		return mode;
	}

	public String getAction()
	{
		return action;
	}

	public String getEmail()
	{
		return email;
	}

	public String getUserName()
	{
		return userName;
	}

	public String getMessage()
	{
		return message;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public void setMode(String mode)
	{
		this.mode = mode;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

}
