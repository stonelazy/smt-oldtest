//$Id$
/**
 * 
 */

package com.share.client.action;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.server.util.CommonUtil;
import com.share.client.bean.Accounts;
import com.share.constants.StrutsConstants;
import com.share.services.AccountsServices;
import com.share.util.AccountsUtil;

/**
 * @author sudharsan-2598
 *
 */
public class AccountsAction
{
	private String mode;
	private String action;
	private static Logger logger = Logger.getLogger(AccountsAction.class.getName());

	HttpServletRequest request = ServletActionContext.getRequest();

	public String execute()
	{
		logger.info("in the execute class");
		String actionResult = "none";
		if ("authInfo".equals(mode))
		{
			logger.info("mode>>> " + mode);
			
			if ("check".equals(action))
			{
				JSONObject userAccount = AccountsServices.getUser(request);

				if(CommonUtil.isValid(userAccount))
				{
					logger.info("useraccount print>> "  + userAccount);
					
					CommonUtil.writeResponse(userAccount);
				}
			}
		}
		return actionResult;
	}

	public String getNotifyInfo()
	{
		Accounts currentUser = AccountsUtil.getCurrentUser();
		request.setAttribute("currentUser", currentUser);
		return StrutsConstants.VIEW;
	}
	
	
	public String getMode()
	{
		return mode;
	}

	public void setMode(String mode)
	{
		this.mode = mode;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}
}
