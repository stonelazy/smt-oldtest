//$Id$
/**
 * 
 */
package com.server.cache.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import com.server.conf.AppProperties;
import com.server.constants.GeneralConstants;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author sudharsan-2598
 *
 */
public class RedisHandler
{
	private static final Logger logger = Logger.getLogger(RedisHandler.class.getName());
	  private static Map<String, JedisPool> poolNameAndPool = new HashMap<>();
	    private static Map<String, String> poolNameAndIp = new HashMap<>();
	    private static final String PORT = "6379";
	    private static String defaulIP = "redis";
	    private static String defaultPoolName = "redispool";
	    private static int defaultMaxActiveConn = 8;
	    private static int defaultReadTimeOut = 2000;

	private static void init(String poolName, String ip, int port, int maxConnections, int readTimeOut)
	{
		synchronized (RedisHandler.class)
		{
//			poolName = poolName + GeneralConstants.MACHINE_IP;
			
			if(!isPoolExist(poolName))
			{
//				addPoolName(poolName, ip);
				JedisPoolConfig jedisConfig = getJedisPoolConfig(maxConnections,readTimeOut);
				int dbName = getDB(poolName);
				JedisPool pool = new JedisPool(jedisConfig, ip, port, readTimeOut, null, dbName);
				poolNameAndPool.put(poolName, pool);
				poolNameAndIp.put(poolName, ip);
				logger.info("jedis pool has been initialized successfully for ip>> " +ip + " poolname>>  " + poolName);
			}
		}
	}

	public static Jedis getResource(String poolName) throws RedisException
	{
		if(poolNameAndPool.containsKey(poolName))
		{
			Object poolObj = poolNameAndPool.get(poolName);
			
			if(poolObj != null)
			{
				JedisPool pool = (JedisPool)poolObj;
				return pool.getResource();
			}
			logger.info("poolobj is null.. donno wat to do.. for now just removed from the map");
			poolNameAndPool.remove(poolName);
			throw new RedisException("pool " + poolName + " not initialized");
		}
		else
		{
			logger.info("poolname is not in the map, not yet initialized");
			throw new RedisException("pool " + poolName + " not initialized");
		}
	}
	
	public static void quit(Jedis jedis, String poolName)
	{
		if(jedis != null)
		{
			JedisPool pool = poolNameAndPool.get(poolName);
			if(pool!= null)
			{
				pool.returnResource(jedis);
			}
			else
			{
				jedis.quit();
				logger.warning("pool not available !! >> " + poolName);
				Exception e = new RedisException("said pool cannot be found.. so the resource was directly destroyed");
				logger.log(Level.WARNING,"Exception Occured",e);
			}
		}
	}
	
	public static void intialize(String poolName, int maxConnections, int readTimeOut)
	{
		String ip =AppProperties.get("redis.host");
		int port =Integer.valueOf(AppProperties.get("redis.port")).intValue();
		init(poolName, ip, port, maxConnections, readTimeOut);
	}
	
	public static void initialize(String poolName)
	{
		intialize(poolName,defaultMaxActiveConn, defaultReadTimeOut);
	}
	
	private static JedisPoolConfig getJedisPoolConfig(int maxConnections, int readTimeOut)
	{
		JedisPoolConfig jedisConfig = new JedisPoolConfig();
		jedisConfig.setMaxTotal(maxConnections);
		jedisConfig.setMaxWaitMillis((long)readTimeOut);
		return jedisConfig;
	}

	private static int getDB(String poolName)
	{
		int db=1;
		if(poolName.equals(defaultPoolName))
		{
			db=0;
		}
		return db;
	}


	/**
	 * @param poolName
	 * @return
	 */
	private static boolean isPoolExist(String poolName)
	{
		return poolNameAndPool.containsKey(poolName);
	}

}
