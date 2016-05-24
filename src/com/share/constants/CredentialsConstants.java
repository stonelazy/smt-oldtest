//$Id$
/**
 * 
 */
package com.share.constants;

import com.server.conf.AppProperties;

/**
 * @author sudharsan-2598
 *
 */

public class CredentialsConstants
{
	public static final String RAILWAY_API_KEY=AppProperties.get("RAILWAY_API_KEY");
	protected static final String SECRET_KEY=AppProperties.get("SECRET_KEY","5e4eb0e6-4c99-4786-ac31-b973f4408b0e");
	//b2730226@trbvn.com - rfrbi5726
}
