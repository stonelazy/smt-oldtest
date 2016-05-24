//$Id$
/**
 * 
 */
package com.server.sql;

import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.postgresql.util.PSQLException;

import com.server.sql.util.SQLUtil;
import com.share.error.MessageConstants;

/**
 * @author sudharsan-2598
 *
 */
public class QueryExecutorImpl implements QueryExecutor
{
	
	private static Logger logger = Logger.getLogger(QueryExecutorImpl.class.getName());

	@Override
	public int executeUpdate(String sql)
	{
		try (Connection con = SQLUtil.getSqlConnection())
		{
			Statement stmt = con.createStatement();
			stmt.execute("set search_path to share");

			stmt = con.createStatement();
			
			int status = stmt.executeUpdate(sql);
			logger.info("insert status>> " + status);
		} catch (PSQLException e)
		{
			if (e.getMessage().contains("duplicate key "))
			{
				return MessageConstants.DUPLICATE_RESOURCE;
			}
			logger.log(Level.WARNING, "Exception Occured >> " + e.getMessage(), e);
			return MessageConstants.UNKNOWN_ERROR;
		} catch (Exception e)
		{
			logger.log(Level.WARNING, "Exception Occured", e);
			return MessageConstants.UNKNOWN_ERROR;
		}
		return MessageConstants.OP_SUCCESS;
	}

}
