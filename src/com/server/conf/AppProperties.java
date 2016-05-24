//$Id$
/**
 * 
 */

package com.server.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.server.util.CommonUtil;

/**
 * @author sudharsan-2598
 *
 */
public class AppProperties
{
	private static Logger logger = Logger.getLogger(AppProperties.class.getName());

	public static Properties props = new Properties();
	// private static String fileName;

	static
	{
		loadProps();
	}

	public static void main(String[] args)
	{
		logger.info("prop loaded>> " + AppProperties.get("app.home"));

	}

	public AppProperties()
	{
		// this.fileName=fileName;
	}

	private static void loadProps()
	{
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		logger.info("server path>> " + path);
		File serverHome = (new File(path)).getParentFile();
		File propsFile = new File(serverHome, "/conf/shareapp.props");
		if (propsFile.exists())
		{
			FileInputStream fis = null;

			try
			{
				fis = new FileInputStream(propsFile);
				props.load(fis);
			} catch (Exception e)
			{
				logger.log(Level.SEVERE, "Exception while loading properties from >>" + propsFile.getAbsolutePath(), e);
			} finally
			{
				try
				{
					if (fis != null)
					{
						fis.close();
					}
				} catch (IOException e)
				{
					logger.log(Level.SEVERE, "Exception while closing the input stream ", e);
				}

			}
		}
		else
		{
			logger.info("property file is missing " + propsFile.getAbsolutePath());
		}
	}

	public static String get(String key, String defaultValue)
	{
		String s2 = get(key);
		return s2 != null ? s2 : defaultValue;
	}

	public static String get(String keyName)
	{
		if (props != null)
		{
			return props.getProperty(keyName);
		}
		return null;
	}
}
