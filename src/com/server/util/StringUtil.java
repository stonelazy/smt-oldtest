//$Id$
package com.server.util;

/**
 * @author sudharsan-2598
 *
 */

public class StringUtil
{
	public static String htmlEscape(String s)
	{
		if (s == null)
		{
			return null;
		}
		else
		{
			StringBuilder html = new StringBuilder(s.length() + 50);
			char[] arr = s.toCharArray();
			int len = arr.length;

			for (int i = 0; i < len; ++i)
			{
				char c = arr[i];
				switch (c)
				{
					case '\"' :
						html.append("&quot;");
						break;
					case '&' :
						html.append("&amp;");
						break;
					case '\'' :
						html.append("&#39;");
						break;
					case '<' :
						html.append("&lt;");
						break;
					case '>' :
						html.append("&gt;");
						break;
					default :
						html.append(c);
				}
			}

			return html.toString();
		}
	}
}
