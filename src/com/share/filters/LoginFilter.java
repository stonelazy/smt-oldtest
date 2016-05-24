
package com.share.filters;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.server.constants.GeneralConstants;
import com.server.util.CommonUtil;
import com.share.client.bean.Accounts;
import com.share.constants.SMTConstants;
import com.share.error.MessageConstants;
import com.share.services.AccountsServices;
import com.share.sql.metadata.Users;
import com.share.sql.query.UserDetailsQuery;
import com.share.sql.query.UserDetailsQueryImpl;
import com.share.util.AccountsUtil;

public class LoginFilter implements Filter
{
	private static Logger logger = Logger.getLogger(AuthenticationFilter.class.getName());

	/**
	 * Default constructor.
	 */
	public LoginFilter()
	{

	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy()
	{

	}

	public void doFilter(ServletRequest request1, ServletResponse response1, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest request = (HttpServletRequest) request1;
		
		logger.info("loginfilter incoming request>> " + request.getRequestURI());
		
		String emailAddress = request.getParameter("emailAddress");
		String password = request.getParameter("password");

		JSONObject responseJson = new JSONObject();
		HttpServletResponse response = (HttpServletResponse) response1;
		
		if(CommonUtil.isValid(emailAddress) && CommonUtil.isValid(password))
		{
			emailAddress = emailAddress.toLowerCase().trim();
			logger.info("email111> " + emailAddress + " pass>> " + password);

			int status = AccountsServices.signinUser(emailAddress,password);
			
			try
			{
				if(status == AccountsServices.VALID_USER)
				{
//					String message = "";
					responseJson = MessageConstants.generateJsonForMsg(200, "Just a sec.. :)");
				}
				else if(status == AccountsServices.NEW_USER)
				{
					if(SMTConstants.IS_NEW_SIGNUP_ALLOWED)
					{
						status = AccountsServices.signupNewUser(emailAddress, password);
						
						if(status == MessageConstants.OP_SUCCESS)
						{
							responseJson = MessageConstants.generateJsonForMsg(200, "Success! your account created :) Now, that was your fifth sec :P ");
						}
						else if (status == MessageConstants.INVALID_PARAM)
						{
							responseJson = MessageConstants.generateJsonForMsg(401, "Like we already told you, your email should be a valid one");
						}
						else
						{
							responseJson = MessageConstants.generateJsonForMsg(501, "Error occured while tryin to create an account for ya..");
						}
					}
					else
					{
						responseJson = MessageConstants.generateJsonForMsg(501, "Uh oh! we are sorry.. our server is under maintenance, can not really create a new account now");
					}
				}
				else if (status == AccountsServices.INVALID_PASSWORD)
				{
					responseJson = MessageConstants.generateJsonForMsg(501, "your pwd is incorrect");
				}
				else
				{
					responseJson = MessageConstants.generateJsonForMsg(501, "I don't know.. something is wrong.. pls try later");
				}
			}
			catch(Exception e)
			{
				logger.log(Level.WARNING,"Exception Occured",e);
			}
			finally
			{
				logger.info("status>> " + status);
				
				if(!responseJson.isNull("code"))
				{
					try
					{
						if(responseJson.getInt("code") < 300)
						{
							UserDetailsQuery query = new UserDetailsQueryImpl();
							JSONObject userAcccount = null;
							try
							{
								userAcccount = query.getUserDetails(emailAddress);
								
								Cookie cookie = new Cookie(GeneralConstants.AUTH_COOKIE_NAME, userAcccount.getString(Users.COOKIE));
						        cookie.setMaxAge(120 * 120);
						        response.addCookie(cookie); 
						        logger.info("cookie added>> " + cookie);
						        
								Accounts accounts = new Accounts();
								accounts.initialize(userAcccount);
								AccountsUtil.setCurrentUser(accounts);
								logger.info("current threadlocal set>> " + responseJson);
								logger.info("checking theadlocal>> " + AccountsUtil.getCurrentUser().getEmail() + " *");
								
								responseJson.put("data", userAcccount);
							}
							catch(Exception e)
							{
								logger.log(Level.WARNING, "Exception Occured " + userAcccount,e);
							}
						}
						else
						{
							logger.info("errorcode>> " + responseJson);
						}
					} catch (JSONException ex)
					{
						logger.log(Level.WARNING,"Exception Occured",ex);
					}
				}
				else
				{
					logger.info("code in the response json is null");
				}
			}
		}
		else
		{
			responseJson  = MessageConstants.generateJsonForMsg(401, "Ha ha! nice try bro.. ");
		}
		
		PrintWriter out = response.getWriter();
		out.println(responseJson.toString());
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException
	{

	}

}
