//$Id$
/**
 * 
 */
package com.share.constants;

import org.json.JSONArray;

import com.server.conf.AppProperties;

/**
 * @author sudharsan-2598
 *
 */
public class SMTConstants
{
 public static final String APP_HOME=null;
 public static final String CLASSES_PATH=Thread.currentThread().getContextClassLoader().getResource("").getPath();
 public static final Boolean IS_REDIS_UP=Boolean.valueOf(AppProperties.get("use.redis"));
 public static final Boolean IS_NEW_SIGNUP_ALLOWED=Boolean.valueOf(AppProperties.get("IS_NEW_SIGNUP_ALLOWED"));
 public static JSONArray STATION_INFO=null;
 
}
