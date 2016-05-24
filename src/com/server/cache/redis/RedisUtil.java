//$Id$
package com.server.cache.redis;

import org.json.JSONObject;

import redis.clients.jedis.Jedis;

/**
 * @author sudharsan-2598
 *
 */
public class RedisUtil
{
	public static boolean isKeyExist(String key) throws RedisException
	{
		boolean isExist;
		
		Jedis jedis = RedisHandler.getResource(RedisConstants.POOL_1);
		isExist = jedis.exists(key);
		RedisHandler.quit(jedis, RedisConstants.POOL_1);
		return isExist;
	}

	/**
	 * @param string
	 * @return
	 * @throws RedisException 
	 */
	public static Object get(String key) throws RedisException
	{
		Jedis jedis = RedisHandler.getResource(RedisConstants.POOL_1);
		Object value = jedis.get(key);
		RedisHandler.quit(jedis, RedisConstants.POOL_1);
		return value;
	}

	/**
	 * @param pnrObj
	 * @throws RedisException 
	 */
	public static void set(String key, String value) throws RedisException
	{
		Jedis jedis = RedisHandler.getResource(RedisConstants.POOL_1);
		jedis.set(key, String.valueOf(value));
		RedisHandler.quit(jedis, RedisConstants.POOL_1);
	}
}
