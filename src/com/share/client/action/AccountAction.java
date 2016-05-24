//$Id$
/**
 * 
 */
package com.share.client.action;

import java.sql.ResultSet;

import com.share.services.AccountsServices;

/**
 * @author sudharsan-2598
 *
 */
public class AccountAction
{
	public String getAcccountDetails()
	{
		ResultSet  set = AccountsServices.getAccountDetails();
		
		
		return null;
 	}
}
