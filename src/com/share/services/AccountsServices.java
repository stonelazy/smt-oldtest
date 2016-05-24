//$Id$
/**
 * 
 */

package com.share.services;

import java.security.SignatureException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;

import com.server.constants.RegexConstants;
import com.server.sql.util.SQLUtil;
import com.server.util.CommonUtil;
import com.server.util.SecurityUtil;
import com.share.error.MessageConstants;
import com.share.sql.metadata.Users;
import com.share.sql.query.UserDetailsQuery;
import com.share.sql.query.UserDetailsQueryImpl;
import com.share.util.AccountsUtil;

/**
 * @author sudharsan-2598
 *
 */
public class AccountsServices
{
	private static Logger logger = Logger.getLogger(AccountsServices.class.getName());

	public static int VALID_USER = 2;
	public static int NEW_USER = 3;

	public static int SUCCESS_THRESHOLD = 19;
	
	public static int INVALID_USER = 20;
	public static int INVALID_PASSWORD = 21;
	public static int CHEATING_USER = 22;
	
	public static ResultSet getAccountDetails()
	{
		return null;
	}
	
	/**
	 * @param email
	 * @param password
	 * @return
	 */
	public static int signinUser(String email, String password)
	{
		Matcher matcher = RegexConstants.VALID_EMAIL_PATTERN.matcher(email);

		if (matcher.find())
		{
			try (Connection con = SQLUtil.getSqlConnection())// , timeout))
			{

				logger.info("con>> " + con);
				Statement stmt = con.createStatement();
				stmt.execute("set search_path to share");

				/*
				 * PreparedStatement preStatement = null;
				 * 
				 * String sql = "Select * from Users WHERE Email = ?"; // No I18N
				 * 
				 * preStatement = con.prepareStatement(sql);
				 * 
				 * preStatement.setString(1, emailAddress); ResultSet result = preStatement.executeQuery(sql);
				 */

				String sql = "Select * from Users WHERE Email = \'" + SQLUtil.escapeSQL(email) + "\'"; // No I18N

				logger.info("select query>>>>" + sql);

				ResultSet result = stmt.executeQuery(sql);

				int i = 0;
				JSONObject userAccount = new JSONObject();
				while (result.next())
				{
					userAccount.put("COOKIE", result.getObject("COOKIE"));
					userAccount.put("USERS_ID", result.getObject("USERS_ID"));
					userAccount.put("NAME", result.getObject("NAME"));
					userAccount.put("MOBILE", result.getObject("MOBILE"));
					userAccount.put("IS_MALE", result.getObject("IS_MALE"));
					userAccount.put("AGE", result.getObject("AGE"));
					userAccount.put("COOKIE", result.getObject("COOKIE"));
					byte[] pwd = result.getBytes(Users.PASSWORD);
					userAccount.put("PASSWORD", new String(pwd));
					i++;
				}

				if (i == 0)
				{
					return NEW_USER;
				}
				else if (i == 1)
				{
					try
					{
						String digestPwd = SecurityUtil.calculateHMAC(password);
						if (SecurityUtil.verifyIfDigestIsSame(digestPwd, userAccount.getString("PASSWORD")))
						{
							return VALID_USER;
						}
						return INVALID_PASSWORD;
					} catch (SignatureException e)
					{
						logger.log(Level.INFO, "Exception Occured", e);
						return INVALID_PASSWORD;
					}
				}
				else if (i > 1)
				{
					return INVALID_USER;
				}
			} catch (SQLException | JSONException ex)
			{
				logger.log(Level.WARNING, "Exception Occured", ex);
			}
		}
		else
		{
			return MessageConstants.INVALID_PARAM;
		}
		return MessageConstants.UNKNOWN_ERROR;
	}

	public static int signupNewUser(String email, String password)
	{
		Matcher matcher = RegexConstants.VALID_EMAIL_PATTERN.matcher(email);

		if (matcher.find())
		{
			try (Connection con = SQLUtil.getSqlConnection())// , timeout))
			{
				Statement stmt = con.createStatement();
				stmt.execute("set search_path to share");

				stmt = con.createStatement();

				email = SQLUtil.escapeSQL(email).toLowerCase().trim();
				String hashedPassword = SecurityUtil.calculateHMAC(password);

				String sql = "INSERT INTO " + Users.TABLE_NAME + " (" + Users.EMAIL + " ," + Users.PASSWORD + " ) VALUES (\'" + email + "\',\' " + SQLUtil.escapeSQL(hashedPassword) + "\')"; // No I18N

				logger.info("insert query >> " + sql);
				int result = stmt.executeUpdate(sql);

				logger.info("insert query>>>>" + result);
				return MessageConstants.OP_SUCCESS;
			} catch (Exception e)
			{
				logger.log(Level.WARNING, "Exception Occured", e);
				return MessageConstants.UNKNOWN_ERROR;
			}
		}
		return MessageConstants.INVALID_PARAM;
	}
	
	public static JSONObject getUser(HttpServletRequest request)
	{
		JSONObject userAccount = null;
		try
		{
			String cookie = CommonUtil.getAuthCookieFromRequest(request);
			if(CommonUtil.isValid(cookie))
			{
				UUID uuid = UUID.fromString(cookie.trim());
				
				UserDetailsQuery query = new UserDetailsQueryImpl();
			 userAccount = query.getUserDetails(uuid);
			}
			else
			{
				logger.info("not a valid cookie>> " + cookie);
			}
		}
		catch(Exception e)
		{
			logger.log(Level.WARNING, "Exception Occured", e);
		}
		return userAccount;
	}

	public static void updateUserInfo(Long mobile, String postal, long uid)
	{
		Map<String,Object> updateParams = new HashMap<>();
		Map<String,Object> criteriaParams = new HashMap<>();
		
		if(mobile != null)
		{
			updateParams.put(Users.MOBILE, mobile);
		}
		if(postal != null)
		{
			updateParams.put(Users.POSTAL, postal);
		}
		
		criteriaParams.put(Users.USERS_ID, AccountsUtil.getCurrentUser().getUid());
		String updateQuery = SQLUtil.formUpdateQuery(Users.TABLE_NAME, updateParams, criteriaParams);
		try (Connection con = SQLUtil.getSqlConnection())
		{
			Statement stmt = con.createStatement();
			stmt.execute("set search_path to share");

			stmt = con.createStatement();
			
			int status = stmt.executeUpdate(updateQuery);
			logger.info("update status>> " + status);
		} catch (Exception e)
		{
			logger.log(Level.WARNING, "Exception Occured", e);
		}
		logger.info("get update sql>> " + updateQuery);
		
	}
}
