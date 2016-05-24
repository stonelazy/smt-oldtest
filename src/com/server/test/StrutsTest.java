//$Id$
/**
 * 
 */

package com.server.test;

import java.util.logging.Logger;

import com.server.conf.AppProperties;

/**
 * @author sudharsan-2598
 *
 */
public class StrutsTest
{
	private static Logger logger = Logger.getLogger(StrutsTest.class.getName());

	public String execute()
	{
		String success = "success";
		logger.info("server location>> " + Thread.currentThread().getContextClassLoader().getResource("").getPath());
		logger.info("prop loaded>> " + AppProperties.get("app.home"));
		return success;
	}
}
