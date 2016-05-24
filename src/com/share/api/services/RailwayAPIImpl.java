//$Id$
/**
 * 
 */
package com.share.api.services;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.BooleanUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.server.cache.redis.RedisConstants;
import com.server.cache.redis.RedisHandler;
import com.server.util.CommonUtil;
import com.server.util.HTTPProtocol;
import com.share.bean.PNR;
import com.share.bean.Train;
import com.share.constants.APIConstants;
import com.share.constants.CredentialsConstants;
import com.share.constants.SMTConstants;
import com.share.constants.SymbolConstants;
import com.share.util.FactoryConverter;

import redis.clients.jedis.Jedis;

/**
 * @author sudharsan-2598
 *
 */
public class RailwayAPIImpl implements API
{
	private static Logger logger = Logger.getLogger(RailwayAPIImpl.class.getName());
	
	@Override
	public JSONObject getPnrStatus(long pnrNumber)
	{
		JSONObject pnrObj = null;
		
		try
		{
			StringBuilder url= new StringBuilder();
			url.append(APIConstants.PNR_STATUS);
			url.append(SymbolConstants.BACK_SLASH);
			url.append(APIConstants.PNR);
			url.append(SymbolConstants.BACK_SLASH);
			url.append(String.valueOf(pnrNumber));
			url.append(SymbolConstants.BACK_SLASH);
			url.append(APIConstants.API_KEY);
//			url.append(SymbolConstants.BACK_SLASH);
//			url.append(CredentialsConstants.RAILWAY_API_KEY);
			
			String key = url.toString();
			pnrObj = checkAndGetFromRedis(key);
		}
		catch(Exception e)
		{
			logger.log(Level.WARNING,"Exception Occured",e);
		}
		return pnrObj;
	}

	
	private JSONObject checkAndGetFromRedis(String key)
	{
		JSONObject apiObject=null;
		Jedis jedis = null;
		String apiResponse=null;
		try
		{
			if(SMTConstants.IS_REDIS_UP)
			{
				jedis = RedisHandler.getResource(RedisConstants.POOL_1);
				
				if(jedis.exists(key))
				{
					logger.info("served from redis for>> " + key);
					String data = jedis.get(key);
					
					if(CommonUtil.isValid(data))
					{
						logger.info("data for>> "  + key + " is served from redis");
						apiObject = new JSONObject(data);
					}
				}	
			}
			else
			{
				logger.info("Redis is not configured, so cannot query it up");
			}
			
			if(apiObject == null)
			{
				logger.info("data not found in redis for key>> " + key);
				StringBuilder url = new StringBuilder(key);
				url.insert(0, getConnectingURL());
				url.append(SymbolConstants.BACK_SLASH);
				url.append(CredentialsConstants.RAILWAY_API_KEY);
				
				apiResponse = HTTPProtocol.getRequest(url.toString(), null, null);
				apiObject = new JSONObject(apiResponse);
				
				if((!(apiObject.isNull(APIConstants.ERROR) || BooleanUtils.toBoolean(apiObject.get(APIConstants.ERROR).toString()))) || (!(apiObject.isNull(APIConstants.RESPONSE_CODE) || apiObject.getInt(APIConstants.RESPONSE_CODE) > 300)))
				{
//					String redisKey = url.substring(0,url.lastIndexOf(SymbolConstants.BACK_SLASH));
					logger.info("was a success response, so cached in NoSQL for pasd key>> " + key);
					jedis.set(key,apiResponse);
				}
				else
				{
					logger.info("somethin is wrong with the data received >> " + apiObject);
				}
			}
		}
		catch(JSONException je)
		{
			logger.info("json exception, api response>> " + apiResponse);
			logger.log(Level.WARNING,"Exception Occured",je);
		}
		catch(Exception e)
		{
			logger.log(Level.WARNING,"Exception Occured",e);
		}
		finally
		{
			RedisHandler.quit(jedis, RedisConstants.POOL_1);
		}
		return apiObject;
	}

	private String getConnectingURL()
	{
		StringBuilder url= new StringBuilder();
		url.append(APIConstants.RAILWAY_API_URL);
		url.append(SymbolConstants.BACK_SLASH);
		return url.toString();
	}


	public static void main(String[] args)
	{
		StringBuilder url= new StringBuilder();
		url.append(APIConstants.PNR_STATUS);
		url.append(SymbolConstants.BACK_SLASH);
		url.append(APIConstants.PNR);
		url.append(SymbolConstants.BACK_SLASH);
		url.append(String.valueOf("123123"));
		url.append(SymbolConstants.BACK_SLASH);
		url.append(APIConstants.API_KEY);
		url.append(SymbolConstants.BACK_SLASH);
		url.append(CredentialsConstants.RAILWAY_API_KEY);

		
		logger.info("url split at>> " + url.substring(0,url.lastIndexOf(SymbolConstants.BACK_SLASH)));
		
	}
	
	@Override
	public PNR getPnrObject(long pnrNumber)
	{
		PNR pnr =null;
		try
		{
			pnr = new PNR();
			JSONObject json = getPnrStatus(pnrNumber);
			FactoryConverter.getPnrObject(pnr,json);
		}
		catch(Exception e)
		{
			logger.log(Level.WARNING,"Exception Occured",e);
			pnr =null;
		}
		return pnr;
	}
	
	public List<Train> getTrainsBetween(String fromStz, String toStz, String date)
	{
		List<Train> trainsList = new ArrayList<>();
//		http://api.railwayapi.com/between/source/<station code>/dest/<station code>/date/dd-mm/apikey/<apikey>/
		try
		{
			StringBuilder url= new StringBuilder();
			
			url.append(APIConstants.BETWEEN);
			url.append(SymbolConstants.BACK_SLASH);
			url.append(APIConstants.SOURCE);
			url.append(SymbolConstants.BACK_SLASH);
			url.append(fromStz);
			url.append(SymbolConstants.BACK_SLASH);
			
			url.append(APIConstants.DEST);
			url.append(SymbolConstants.BACK_SLASH);
			url.append(String.valueOf(toStz));
			url.append(SymbolConstants.BACK_SLASH);
			
			url.append(APIConstants.DATE);
			url.append(SymbolConstants.BACK_SLASH);
			url.append(date.substring(0,date.lastIndexOf(SymbolConstants.HYPHEN)));
			url.append(SymbolConstants.BACK_SLASH);
			
			url.append(APIConstants.API_KEY);
			
			String key = url.toString();
			JSONObject trains  = checkAndGetFromRedis(key);
			
			if(trains != null && trains.getInt(APIConstants.RESPONSE_CODE) < 300)
			{
				JSONArray trainArray = trains.getJSONArray(APIConstants.TRAIN);
				
				for(int i=trainArray.length()-1;i>0;i--)
				{
					JSONObject trainJson = trainArray.getJSONObject(i);
					Train train = FactoryConverter.getTrainObject(trainJson);
					trainsList.add(train);
					logger.fine("train obj>> " + train);
				}
			}
		}
		catch(Exception e)
		{
			logger.log(Level.WARNING,"Exception Occured",e);
		}
		return trainsList;
	}
}