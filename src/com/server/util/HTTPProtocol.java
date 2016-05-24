//$Id$
/**
 * 
 */

package com.server.util;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

/**
 * @author sudharsan-2598
 *
 */
public class HTTPProtocol
{
	private static Logger logger = Logger.getLogger(HTTPProtocol.class.getName());

	public static Object postRequest(String connUrl, List<NameValuePair> nvPairList, float level) throws Exception
	{
		logger.info("connectingURL>>" + connUrl);
		logger.fine("nvpairlist>> " + nvPairList);

		PostMethod post = new PostMethod(connUrl);

		NameValuePair[] nvpair = new NameValuePair[nvPairList.size()];

		for (int i = nvPairList.size() - 1; i >= 0; i--)
		{
			nvpair[i] = nvPairList.get(i);
			logger.fine("nvpair>> " + nvpair[i]);
		}
		post.addParameters(nvpair);

		HttpClient httpClient = new HttpClient();
		HttpConnectionManagerParams connectionParams = getConnectionParams(level);

		httpClient.getHttpConnectionManager().setParams(connectionParams);
		logger.config("going to execute the post method");// No I18N
		int statusCode = httpClient.executeMethod(post);

		String response = post.getResponseBodyAsString();
		logger.finer("post response>> " + response + " statusCode>> " + statusCode);// No I18N

		return response;
	}

	public static String getRequest(String connectingURL, List<Header> headerList, List<NameValuePair> nvPairList) throws IOException
	{
		logger.info("connectingURL>> " + connectingURL);
		logger.fine("headerlist> " + headerList);
		GetMethod get = new GetMethod(connectingURL);

		if (headerList != null)
		{
			for (int i = headerList.size() - 1; i >= 0; i--)
			{
				get.setRequestHeader(headerList.get(i));
				logger.finest("req header>> " + i + " >> " + headerList.get(i));
			}
		}

		if (nvPairList != null)
		{
			NameValuePair[] nvpair = new NameValuePair[nvPairList.size()];

			for (int i = nvPairList.size() - 1; i >= 0; i--)
			{
				nvpair[i] = nvPairList.get(i);
				logger.fine("nvpair>> " + nvpair[i]);
			}
			get.setQueryString(nvpair);
		}

		HttpClient httpClient = new HttpClient();
		HttpConnectionManagerParams connectionParams = getConnectionParams(1);
		httpClient.getHttpConnectionManager().setParams(connectionParams);
		logger.config("going to execute the get method");// No I18N

		httpClient.executeMethod(get);
		String response = get.getResponseBodyAsString();

		logger.finer("get method response>> " + response);// No I18N
		return response;
	}

	/**
	 * @param timeOut
	 * @return
	 */
	private static HttpConnectionManagerParams getConnectionParams(float level)
	{
		HttpConnectionManagerParams connectionParams = new HttpConnectionManagerParams();
		connectionParams.setConnectionTimeout(Double.valueOf(30 * 1000 * level).intValue());
		connectionParams.setSoTimeout(Double.valueOf(60 * 1000 * level).intValue());
		return connectionParams;
	}
}
