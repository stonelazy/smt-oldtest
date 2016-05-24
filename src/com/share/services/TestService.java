//$Id$
/**
 * 
 */
package com.share.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.server.cache.redis.RedisConstants;
import com.server.cache.redis.RedisHandler;
import com.server.cache.redis.RedisUtil;

import redis.clients.jedis.Jedis;

/**
 * @author sudharsan-2598
 *
 */
public class TestService
{
	private static Logger logger = Logger.getLogger(TestService.class.getName());

	public static void checkRedis()
	{
		try
		{
			RedisUtil.set("ping1","uh yeah responding");
			logger.info("get>> " + RedisUtil.get("ping1"));
			
			Jedis jedis = RedisHandler.getResource(RedisConstants.POOL_1);
			logger.info("in the db>> " + jedis.getDB() + " dbsize>> " + jedis.dbSize());
//			logger.info("asking>>> " + jedis.asking());
		}
		catch(Exception e)
		{
			logger.log(Level.INFO, "Exception Occured", e);
			logger.warning("redis not working");
		}
		
	}
	
}
