//$Id$
/**
 * 
 */
package com.share.client.action;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.server.sql.QueryExecutor;
import com.server.sql.QueryExecutorImpl;
import com.server.sql.util.SQLUtil;
import com.share.client.bean.Accounts;
import com.share.constants.ActionConstants;
import com.share.sql.metadata.Upvote;
import com.share.util.AccountsUtil;

/**
 * @author sudharsan-2598
 *
 */
public class EntertainmentAction
{
	private static Logger logger = Logger.getLogger(EntertainmentAction.class.getName());
	
	private String action;
	private String mode;
	private String like;
	
	
	public String execute()
	{
		logger.info("mode>> " + getMode() + " action>> "+  getAction());
		
		if(getMode().equals(ActionConstants.ENTERTAINMENT))
		{
			if(getAction().equals(ActionConstants.UPVOTE))
			{
				if(Boolean.valueOf(getLike()))
				{
					logger.info("this user>> " + AccountsUtil.getCurrentUser() + " wants it developed");
					
					Accounts user = AccountsUtil.getCurrentUser();
					
					Map<String,Object> insertValues = new HashMap<>();
					insertValues.put(Upvote.USERS_ID, user == null? 0:user.getUid());
					
					String sql  = SQLUtil.formInsertQuery(insertValues, Upvote.TABLE_NAME);
					QueryExecutor executor = new QueryExecutorImpl();
					int result = executor.executeUpdate(sql);
					logger.info("upvote result>> " + result);
				}
			}
		}
		return null;
	}

	public String getAction()
	{
		return action;
	}
	public String getMode()
	{
		return mode;
	}
	public String getLike()
	{
		return like;
	}
	public void setAction(String action)
	{
		this.action = action;
	}
	public void setMode(String mode)
	{
		this.mode = mode;
	}
	public void setLike(String like)
	{
		this.like = like;
	}
	
//	
//	like : true,
//	action : 'upvote',
//	mode : 'entertainment'
		
	
}
