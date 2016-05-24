//$Id$
/**
 * 
 */

package com.server.test;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang3.time.StopWatch;

import snaq.db.ConnectionPoolManager;
import snaq.db.DBPoolDataSource;

/**
 * @author sudharsan-2598
 *
 */
public class JDBCTest
{
	private static Logger logger = Logger.getLogger(JDBCTest.class.getName());

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/";
    
	public static void main(String[] args)
	{
		postgresMethod1();
//		mysqlmethod1();
//		mysqlmethod2();
	}

	/**
	 * 
	 */
	public static void postgresMethod1()
	{
		StopWatch watch = new StopWatch();
		watch.start();
		ConnectionPoolManager cpm = null;
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        File serverHome = (new File(path)).getParentFile();
        File propsFile = new File(serverHome, "/conf/dbpool.properties");
        
		try
		{
			cpm = ConnectionPoolManager.getInstance(propsFile);
		} catch (IOException ex)
		{
			ex.printStackTrace();
		}

		long timeout = 2000; // 2 second timeout
		for(int i=0;i<2;i++)
		{
			try(Connection con = cpm.getConnection("poolname"))//, timeout)) 
			{
				if (con != null)
				{
					Statement stmt = con.createStatement();

					stmt.execute("set search_path to share");
					
					ResultSet rs = stmt.executeQuery("select password from users where users_id=201;");
					List<String> dbList = new ArrayList<>();

					while (rs.next())
					{
						String db = rs.getString(1);
//						byte[] a= rs.getBytes(1);
//						logger.info("password>> " + new String(a));
						dbList.add(db);
					}
					logger.info("dblist>>" + dbList);
				}
				else
				{
					logger.info("do something else (timeout occurred)");
				}
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		}
		logger.info("took>> " + watch.getTime());
			
	
	}

	/**
	 * 
	 */
	public static void mysqlmethod2()
	{
		StopWatch watch = new StopWatch();
		watch.start();
		
		Connection conn = null;
		Statement stmt = null;
		try
		{
		    // STEP 2: Register JDBC driver
		    Class.forName("com.mysql.jdbc.Driver");

		    // STEP 3: Open a connection
		    System.out.println("Connecting to a selected database...");
		    conn = DriverManager.getConnection(DB_URL, "root", "");
		    System.out.println("Connected database successfully...");

		    // STEP 4: Execute a query
		    System.out.println("Deleting database...");
		    stmt = conn.createStatement();

		    ResultSet rs = stmt.executeQuery("show databases;");
		    List<String> dbList = new ArrayList<>();
		    
		    logger.info("dblist>>" + dbList);
		} catch (SQLException se)
		{
		    // Handle errors for JDBC
		    se.printStackTrace();
		} catch (Exception e)
		{
		    // Handle errors for Class.forName
		    e.printStackTrace();
		} finally
		{
		    // finally block used to close resources
		    try
		    {
			if(stmt != null) conn.close();
		    } catch (SQLException se)
		    {
		    }// do nothing
		    try
		    {
			if(conn != null) conn.close();
		    } catch (SQLException se)
		    {
			se.printStackTrace();
		    }// end finally try
		}// end try
		System.out.println("Goodbye! >> " + watch.getTime());
		
	}

	/**
	 * 
	 */
	public static void mysqlmethod1()
	{

		StopWatch watch = new StopWatch();
		watch.start();
		ConnectionPoolManager cpm = null;
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        File serverHome = (new File(path)).getParentFile();
        File propsFile = new File(serverHome, "/conf/dbpool.properties");
        
		try
		{
			cpm = ConnectionPoolManager.getInstance(propsFile);
		} catch (IOException ex)
		{
			ex.printStackTrace();
		}

		long timeout = 2000; // 2 second timeout
		for(int i=0;i<5;i++)
		{
			try(Connection con = cpm.getConnection("poolname", timeout)) 
			{
				if (con != null)
				{
					Statement stmt = con.createStatement();

					ResultSet rs = stmt.executeQuery("show databases;");
					List<String> dbList = new ArrayList<>();

					while (rs.next())
					{
						String db = rs.getString(1);
						dbList.add(db);
					}
					logger.info("dblist>>" + dbList);
				}
				else
				{
					logger.info("do something else (timeout occurred)");
				}
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		}
		logger.info("took>> " + watch.getTime());
			
	}
}
