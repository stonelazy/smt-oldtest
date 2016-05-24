//$Id$
/**
 * 
 */

package com.server.constants;

import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.server.conf.AppProperties;

/**
 * @author sudharsan-2598
 *
 */
public class GeneralConstants
{
	private static Logger logger = Logger.getLogger(GeneralConstants.class.getName());

	public static String MACHINE_IP = null;
	public static final String AUTH_COOKIE_NAME = AppProperties.get("AUTH_COOKIE_NAME","ASS-DONTSTEAL");
	
	
	static
	{
		try
		{
			MACHINE_IP ="localhost";// InetAddress.getLocalHost().getHostName();
			
		} catch (Exception e)
		{
			logger.log(Level.WARNING, "Exception Occured", e);
		}
	}
}
