//$Id$
/**
 * 
 */
package com.share.services;

import java.util.logging.Logger;

import com.server.cache.redis.RedisConstants;
import com.server.cache.redis.RedisHandler;
import com.server.conf.AppProperties;

/**
 * @author sudharsan-2598
 *
 */
public class AppStartup
{
	private static Logger logger = Logger.getLogger(AppStartup.class.getName());

	public static void handleServerStartup()
	{
		logger.info("handlestartup in appstartup");
		new AppProperties();
		
		if(Boolean.valueOf(AppProperties.get("use.redis")))
		{
			RedisHandler.intialize(RedisConstants.POOL_1, 4, 2000);
		}

		TestService.checkRedis();
		
	}
}
