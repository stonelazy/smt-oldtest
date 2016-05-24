//$Id$
package com.share.sql.query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.server.sql.util.SQLUtil;
import com.share.sql.metadata.Users;

/**
 * @author sudharsan-2598
 *
 */
public class UserDetailsQueryImpl implements UserDetailsQuery
{
	private static Logger logger = Logger.getLogger(UserDetailsQueryImpl.class.getName());

	@Override
	public JSONObject getUserDetails(String email) throws JSONException, SQLException
	{
		JSONObject userAccount = null;
	
		try (Connection con = SQLUtil.getSqlConnection())// , timeout))
		{
			Statement stmt = con.createStatement();
			stmt.execute("set search_path to share");

			String sql = "Select * from Users WHERE Email = \'" + SQLUtil.escapeSQL(email) + "\'"; // No I18N

			logger.info("select query>>>>" + sql);

			ResultSet result = stmt.executeQuery(sql);
			userAccount = getJsonOfUserTable(result);
		}
		finally
		{
			logger.fine("useraccount> " + userAccount);
		}
		return userAccount;
	}

	/**
	 * @param userAccount
	 * @throws JSONException 
	 * @throws SQLException 
	 */
	private JSONObject getJsonOfUserTable(ResultSet result) throws SQLException, JSONException
	{
		JSONObject userAccount = null;
		
		if (result.next())
		{
			userAccount = new JSONObject();
			userAccount.put("COOKIE", result.getObject("COOKIE"));
			userAccount.put("USERS_ID", result.getObject("USERS_ID"));
			userAccount.put("NAME", result.getObject("NAME"));
			userAccount.put("EMAIL", result.getObject("EMAIL"));
			userAccount.put("MOBILE", result.getObject("MOBILE"));
			userAccount.put("IS_MALE", result.getObject("IS_MALE"));
			userAccount.put("AGE", result.getObject("AGE"));
			userAccount.put("COOKIE", result.getObject("COOKIE"));
			byte[] pwd = result.getBytes(Users.PASSWORD);
			userAccount.put("PASSWORD", new String(pwd));
		}
		return userAccount;
	}

	@Override
	public JSONObject getUserDetails(UUID cookie) throws JSONException, SQLException
	{
		JSONObject userAccount = null;
		
		try (Connection con = SQLUtil.getSqlConnection())// , timeout))
		{
			Statement stmt = con.createStatement();
			stmt.execute("set search_path to share");

			String sql = "Select * from " + Users.TABLE_NAME + " WHERE " + Users.COOKIE+"= \'" + cookie.toString() + "\'"; // No I18N
			logger.info("select query>>>>" + sql);

			ResultSet result = stmt.executeQuery(sql);
			userAccount = getJsonOfUserTable(result);
		}
		finally
		{
			logger.fine("useraccount> " + userAccount);
		}
		return userAccount;
	}

	
	
	
}


