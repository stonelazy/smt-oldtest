
package com.share.filters;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.server.util.CommonUtil;
import com.share.client.bean.Accounts;
import com.share.error.MessageConstants;
import com.share.services.AccountsServices;
import com.share.util.AccountsUtil;
import snaq.db.ConnectionPoolManager;

//description = "All those requests that will require user authentication will be passed thro this.", urlPatterns = {"/AuthenticationFilter"})
public class AuthenticationFilter implements Filter
{
	private static Logger logger = Logger.getLogger(AuthenticationFilter.class.getName());
	public static ConnectionPoolManager cpm = null;
    private static Pattern skipPattern = null;
    private static Pattern optionalPattern = null;

    public AuthenticationFilter()
	{
		
	}

	public void destroy()
	{

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		logger.info("in the authentication filter, requri>> " + httpRequest.getRequestURI());
		
		try
		{ 
			JSONObject userAccount = AccountsServices.getUser(httpRequest);
			if(userAccount != null)
			{
				Accounts accounts = new Accounts();
				accounts.initialize(userAccount);
				AccountsUtil.setCurrentUser(accounts);
				logger.info("current threadlocal set>> " + AccountsUtil.getCurrentUser().getUid());
				
			}
			else if(optionalPattern.matcher(httpRequest.getRequestURI()).matches() || httpRequest.getRequestURI().contains("optauth"))
			{
				logger.info("useraccount null.. so not nota a valid user");

//				response.getOutputStream().println("nope, not a valid user");
				//nope, not a valid user
//				return;
			}
			else
			{
				logger.info("this request is not authenticated>> " + httpRequest.getRequestURI());
				response.getOutputStream().println(String.valueOf(MessageConstants.getError(400)));
				httpResponse.setStatus(400, "NOT AUTHORIZED");
				return;
			}
		} catch (NumberFormatException e)
		{
			logger.info("cookie la kai vechitan :/ ");
			response.getOutputStream().println("Don't you dare touch my coookie !!");
			return;
		} catch (JSONException e)
		{
			logger.log(Level.WARNING,"Exception Occured",e);
			httpResponse.getOutputStream().println(String.valueOf(MessageConstants.getError(500)));
			httpResponse.setStatus(500);
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException
	{
		String skip = fConfig.getInitParameter("skip");
        if(CommonUtil.isValid(skip))
        {
            skipPattern = Pattern.compile(skip);
        }
        
        String optional = fConfig.getInitParameter("optional");
        if(CommonUtil.isValid(optional))
        {
        	optionalPattern = Pattern.compile(optional);
        	logger.info("check match>> " + optional + "   >>"+ optionalPattern.matcher("/share/optauth/entertainment.do").matches());
        }
	}
}
