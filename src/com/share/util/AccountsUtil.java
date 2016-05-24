//$Id$
/**
 * 
 */
package com.share.util;

import com.share.client.bean.Accounts;

/**
 * @author sudharsan-2598
 *
 */
public class AccountsUtil
{
    static final ThreadLocal<Accounts> ACCOUNTS_THREAD_LOCAL = new ThreadLocal<>();
    
    public static Accounts getCurrentUser()
    {
    	return ACCOUNTS_THREAD_LOCAL.get();
    }
    
    public static void setCurrentUser(Accounts accounts)
    {
    	ACCOUNTS_THREAD_LOCAL.set(accounts);
    }
}
