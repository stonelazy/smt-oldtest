//$Id$

package com.server.sql.util;

import static com.share.constants.SymbolConstants.COMMA;
import static com.share.constants.SymbolConstants.LEFT_PARANTHESIS;
import static com.share.constants.SymbolConstants.RIGHT_PARANTHESIS;
import static com.share.constants.SymbolConstants.DOT;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.server.util.CommonUtil;
import com.share.constants.SymbolConstants;
import com.share.sql.metadata.Sell;
import com.share.sql.metadata.Users;
import com.share.util.AccountsUtil;

import snaq.db.ConnectionPoolManager;

/**
 * @author sudharsan-2598
 *
 */
public class SQLUtil
{
	private static Logger logger = Logger.getLogger(SQLUtil.class.getName());

	public static ConnectionPoolManager cpm = null;
	public static String POOL_1 = "poolname";
	public static String APP_SCHEMA = "share";

	static
	{
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		File serverHome = (new File(path)).getParentFile();
		File propsFile = new File(serverHome, "/conf/dbpool.properties");
		try
		{
			cpm = ConnectionPoolManager.getInstance(propsFile);
			logger.info("conn pool manager initialzied");
		} catch (Exception e)
		{
			logger.log(Level.WARNING, "Exception Occured", e);
		}
	}

	public static void switchToAppSchema(String schema) throws SQLException
	{
		try (Connection con = cpm.getConnection(POOL_1))
		{
			Statement stmt = con.createStatement();
			stmt.execute("set search_path to " + APP_SCHEMA);
		}
	}

	public static Connection getSqlConnection()
	{
		try
		{
			if (cpm != null)
			{
				return cpm.getConnection(POOL_1);
			}
		} catch (Exception e)
		{
			logger.log(Level.WARNING, "Exception Occured", e);
		}
		return null;
	}

	public static String formInsertQuery(Map<String, Object> params, String tableName)
	{
		StringBuilder query = new StringBuilder("INSERT INTO ");

		if (params != null)
		{
			Iterator<String> columns = params.keySet().iterator();
			StringBuilder colNames = new StringBuilder(LEFT_PARANTHESIS);
			StringBuilder values = new StringBuilder(LEFT_PARANTHESIS);

			while (columns.hasNext())
			{
				String column = columns.next();
				colNames.append(column);
				Object value = params.get(column);

				appendTheValueInBuilder(values, value, column);

				values.append(COMMA);
				colNames.append(COMMA);
			}
			colNames.deleteCharAt(colNames.length() - 1);
			colNames.append(RIGHT_PARANTHESIS);
			values.deleteCharAt(values.length() - 1);
			values.append(RIGHT_PARANTHESIS);

			query.append(tableName);
			query.append(" ");
			query.append(colNames);
			query.append(" ");
			query.append("VALUES");
			query.append(" ");
			query.append(values);
		}
		return query.toString();
	}
 
	public static String escapeSQL(String s)
	{
		int length = s.length();
		int newLength = length;
		// first check for characters that might
		// be dangerous and calculate a length
		// of the string that has escapes.
		for (int i = 0; i < length; i++)
		{
			char c = s.charAt(i);
			switch (c)
			{
				case '\\' :
				case '\"' :
				case '\'' :
				case '\0' :
				{
					newLength += 1;
				}
					break;
			}
		}
		if (length == newLength)
		{
			// nothing to escape in the string
			return s;
		}
		StringBuffer sb = new StringBuffer(newLength);
		for (int i = 0; i < length; i++)
		{
			char c = s.charAt(i);
			switch (c)
			{
				case '\\' :
				{
					sb.append("\\\\");
				}
					break;
				case '\"' :
				{
					sb.append("\\\"");
				}
					break;
				case '\'' :
				{
					sb.append("\\\'");
				}
					break;
				case '/' :
				{
					sb.append("\\/");
				}
					break;
				case '\0' :
				{
					sb.append("\\0");
				}
					break;
				default :
				{
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}
	
	public static void main(String[] args)
	{
		Map<String, Object> criteriaParams = new HashMap<>();
		List<Object> pnrList = new ArrayList<>();
		pnrList.add(12313123L);
		pnrList.add(123133L);

		criteriaParams.put(Sell.USERS_ID, 123123L);
		criteriaParams.put(Sell.DOJ, pnrList);

		
		logger.info("formcrieria>> " + formCriteria(criteriaParams));
		
		
		List<String> selectColumns = new ArrayList<>();
		selectColumns.add(Sell.DOJ);
		selectColumns.add(Sell.EXTRA_PASSENGER);
		selectColumns.add(Sell.FROM_STZ);
		selectColumns.add(Sell.TO_STZ);
		selectColumns.add(Sell.TRAIN_NUMBER);

		Map<String, Object> updateParams = new HashMap<>();

		updateParams.put(Users.MOBILE, 123123123L);
		updateParams.put(Users.POSTAL, "chinna kadai street, trichy \n 62002");

		Map<String, List<Object>> queryValues = new HashMap<>();

		queryValues.put(Sell.PNR, pnrList);

		String sql = SQLUtil.formInCritieraQuery(queryValues, Sell.TABLE_NAME);

		logger.info(" sql>> " + sql);

	}	 

	public static String formCriteria(Map<String, Object> criteriaParams)
	{
		String criteriaString = null;
		if (criteriaParams != null)
		{
			Iterator<String> keys = criteriaParams.keySet().iterator();
			StringBuilder criteria = new StringBuilder(" WHERE ");

			if (keys.hasNext())
			{
				criteria.append(LEFT_PARANTHESIS);
			}
			
			while (keys.hasNext())
			{
				String column = keys.next();
				criteria.append(column);

				Object value = criteriaParams.get(column);
				
				if(value instanceof List)
				{
					criteria.append(" IN ");
				}
				else if(value instanceof Map || value instanceof HashMap)
				{
					logger.info("yes! a map indeed");
					Map<String,Object> queryConstants = (Map<String, Object>) value;
					String querySymbol = queryConstants.keySet().iterator().next();
					criteria.append(" ");
					criteria.append(querySymbol);
					criteria.append(" ");
					value = queryConstants.get(querySymbol);
				}
				else
				{
					criteria.append(" ");
					criteria.append(SymbolConstants.EQUALTO);
					criteria.append(" ");
				}
				appendTheValueInBuilder(criteria, value, column);
				
				criteria.append(" ");
				if (keys.hasNext())
				{
					criteria.append(" AND ");
				}
				else
				{
					criteria.append(RIGHT_PARANTHESIS);
				}
			}
			criteriaString = criteria.toString();
		}
		return criteriaString;
	}

	private static void appendTheValueInBuilder(StringBuilder builder, Object value, String column)
	{
		if (CommonUtil.isValid(value))
		{
			if (value instanceof String)
			{
				builder.append("\'");
				builder.append(String.valueOf(value).trim());
				builder.append("\'");
			}
			else if (value instanceof Number || value instanceof Boolean)
			{
				builder.append(value);
			}
			else if(value instanceof List)
			{
				builder.append(SymbolConstants.LEFT_PARANTHESIS);
			
				List<Object> valueList = (List<Object>) value;
				
				for (int i = valueList.size() - 1; i >= 0; i--)
				{
				    logger.info("infinite loop check>> " + i);
					appendTheValueInBuilder(builder, valueList.get(i), column);
					builder.append(COMMA);
				}
				builder.deleteCharAt(builder.length() - 1);
				
				builder.append(SymbolConstants.RIGHT_PARANTHESIS);
			}
			else
			{
				logger.info("what the hell is this>> " + value.getClass() + " and>> " + value);
				builder.append(value);
			}
		}
		else
		{
			logger.info("null for column>> " + column);
		}

	}
	
	public static String formSelectQuery(String tableName, List<String> columnsList, Map<String, Object> criteriaParams)
	{
		StringBuilder query = new StringBuilder("SELECT ");

		if (columnsList != null)
		{
			StringBuilder selectCols = new StringBuilder();

			for (int i = columnsList.size() - 1; i >= 0; i--)
			{
				selectCols.append(columnsList.get(i));
				selectCols.append(COMMA);
			}
			selectCols.deleteCharAt(selectCols.length() - 1);
			selectCols.append(" FROM ");
			selectCols.append(tableName);
			// selectCols.append(" WHERE ");
			query.append(selectCols);
		}

		if (criteriaParams != null)
		{
			String criteria = formCriteria(criteriaParams);
			query.append(" ");
			query.append(criteria);
		}

		return query.toString();
	}

	public static String formUpdateQuery(String tableName, Map<String, Object> updateValues, Map<String, Object> criteriaParams)
	{
		StringBuilder query = new StringBuilder("UPDATE ");
		query.append(tableName);
		query.append(" SET ");

		if (updateValues != null)
		{
			Iterator<String> columns = updateValues.keySet().iterator();

			while (columns.hasNext())
			{
				String column = columns.next();
				Object value = updateValues.get(column);
				query.append(column);
				query.append(SymbolConstants.EQUALTO);
				appendTheValueInBuilder(query, value, column);
				query.append(COMMA);
			}
			query.deleteCharAt(query.length() - 1);
		}
		String criteria = formCriteria(criteriaParams);
		query.append(criteria);
		
		logger.info("update query>> " + query);
		
		return query.toString();
	}

	public static String formInCritieraQuery(Map<String, List<Object>> queryValues, String tableName)
	{
		StringBuilder query = new StringBuilder("WHERE ");

		Iterator<String> columns = queryValues.keySet().iterator();

		while (columns.hasNext())
		{
			String column = columns.next();
			List<Object> valueList = queryValues.get(column);
			query.append(column);
			query.append(" IN ");
			query.append(SymbolConstants.LEFT_PARANTHESIS);

			for (int i = valueList.size() - 1; i >= 0; i--)
			{
				appendTheValueInBuilder(query, valueList.get(i), column);
				query.append(COMMA);
			}
			query.deleteCharAt(query.length() - 1);
			
			query.append(SymbolConstants.RIGHT_PARANTHESIS);
		}

		return query.toString();
	}
}
