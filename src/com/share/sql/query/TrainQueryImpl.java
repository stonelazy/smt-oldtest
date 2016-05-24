//$Id$
/**
 * 
 */

package com.share.sql.query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.postgresql.util.PSQLException;

import com.server.sql.util.SQLUtil;
import com.share.constants.SMTConstants;
import com.share.error.MessageConstants;
import com.share.sql.metadata.Passengers;
import com.share.sql.metadata.Sell;
import com.share.sql.metadata.StationInfo;

import static com.share.constants.SymbolConstants.DOT;
import static com.share.constants.SymbolConstants.EQUALTO;

/**
 * @author sudharsan-2598
 *
 */
public class TrainQueryImpl implements TrainQuery
{
	private static Logger logger = Logger.getLogger(TrainQueryImpl.class.getName());

	@Override
	public int addTicket(int trainNumber, long pnrNumber, String fromStationCode, String toStationCode, Date doj, int extraPassenger, long uid)
	{
		try (Connection con = SQLUtil.getSqlConnection())
		{
			Statement stmt = con.createStatement();
			stmt.execute("set search_path to share");

			stmt = con.createStatement();

			Map<String, Object> params = new HashMap<>();
			params.put(Sell.TRAIN_NUMBER, trainNumber);
			params.put(Sell.PNR, pnrNumber);
			params.put(Sell.FROM_STZ, fromStationCode);
			params.put(Sell.TO_STZ, toStationCode);
			params.put(Sell.DOJ, doj.getTime());
			params.put(Sell.EXTRA_PASSENGER, extraPassenger);
			params.put(Sell.USERS_ID, uid);

			String sql = SQLUtil.formInsertQuery(params, Sell.TABLE_NAME);

			logger.info("insert query >> " + sql);
			int result = stmt.executeUpdate(sql);

			logger.info("result of insert query>> " + result);
			return MessageConstants.OP_SUCCESS;
		} catch (PSQLException e)
		{
			// rg.postgresql.util.PSQLException: ERROR: duplicate key value violates unique constraint "sell_pkey"
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

	}

	@Override
	public JSONArray getSharedTickets(long uid)
	{
		JSONArray rows = new JSONArray();

		try (Connection con = SQLUtil.getSqlConnection())
		{
			Statement stmt = con.createStatement();
			stmt.execute("set search_path to share");

			stmt = con.createStatement();

			Map<String, Object> criteriaParams = new HashMap<>();
			criteriaParams.put(Sell.USERS_ID, uid);
			
/*			pnr_number      | bigint       | not null
			 train_number    | bigint       | 
			 from_stz        | character(4) | 
			 to_stz          | character(4) | 
			 doj             | bigint       | 
			 extra_passenger | smallint     | 
			 users_id        | bigint       | 
			 notify          | smallint     | 
			 mediate         | integer      | 
			 status          | integer      | default 0
*/
			List<String> selectColumns = new ArrayList<>();
			selectColumns.add(Sell.PNR);
			selectColumns.add(Sell.TRAIN_NUMBER);
			selectColumns.add(Sell.FROM_STZ);
			selectColumns.add(Sell.TO_STZ);
			selectColumns.add(Sell.DOJ);
			selectColumns.add(Sell.EXTRA_PASSENGER);
			selectColumns.add(Sell.USERS_ID);
			selectColumns.add(Sell.NOTIFY);
			selectColumns.add(Sell.MEDIATE);
			selectColumns.add(Sell.STATUS);
			
			String sql = SQLUtil.formSelectQuery(Sell.TABLE_NAME, selectColumns, criteriaParams);
			logger.info("view all shared tickets sql>> "  +sql);
			ResultSet result = stmt.executeQuery(sql);
			ResultSetMetaData metadata = result.getMetaData();

			while (result.next())
			{
				JSONObject row = new JSONObject();
				for (int i = metadata.getColumnCount(); i > 0; i--)
				{
					String colName = metadata.getColumnLabel(i);
					Object value = result.getObject(i);
					row.put(colName.toUpperCase(), value);
				}
				rows.put(row);
			}

			logger.info("insert query>>>>" + result);
			return rows;

		} catch (Exception e)
		{
			logger.log(Level.WARNING, "Exception Occured", e);
			return rows;
		}
	}

	@Override
	public int addShareablePassenger(long pnr, String identifyPassenger, long uid, String currentStatus, String bookingStatus)
	{
		Map<String, Object> insertValues = new HashMap<>();
		insertValues.put(Passengers.PNR_ID, pnr);
		insertValues.put(Passengers.IDENTIFY_PASSENGER, identifyPassenger);
		insertValues.put(Passengers.USERS_ID, uid);
		insertValues.put(Passengers.CURRENT_STATUS, currentStatus);
		insertValues.put(Passengers.BOOKING_STATUS, bookingStatus);
		String sql = SQLUtil.formInsertQuery(insertValues, Passengers.TABLE_NAME);

		logger.info("insert passengers query>> " + sql);

		try (Connection con = SQLUtil.getSqlConnection())
		{
			Statement stmt = con.createStatement();
			stmt.execute("set search_path to share");

			stmt = con.createStatement();
			
			int status = stmt.executeUpdate(sql);
			logger.info("insert status>> " + status);
		} catch (PSQLException e)
		{
			// rg.postgresql.util.PSQLException: ERROR: duplicate key value violates unique constraint "sell_pkey"
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
		return 0;

	}

	@Override
	public int updateSellPnr(long pnrNumber, int notifyStatus, int extraPassengerCount, int mediate)
	{
		Map<String,Object> updateParams = new HashMap<>();
		Map<String,Object> criteriaParams = new HashMap<>();
		
		updateParams.put(Sell.NOTIFY,notifyStatus);
		updateParams.put(Sell.EXTRA_PASSENGER,extraPassengerCount);
		updateParams.put(Sell.MEDIATE,mediate);
		
		criteriaParams.put(Sell.PNR,pnrNumber);
		
		String updateQuery = SQLUtil.formUpdateQuery(Sell.TABLE_NAME, updateParams, criteriaParams);
		
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
			return MessageConstants.UNKNOWN_ERROR;
		}
		
		logger.info("get update sql>> " + updateQuery);
		return MessageConstants.OP_SUCCESS;
	}

	@Override
	public JSONObject getSellPnrJson(long pnr)
	{
		 //select * from sell left join passengers on sell.pnr_number=passengers.pnr_number where sell.pnr_number=4631174536;
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT * FROM ");
		sqlBuilder.append(Sell.TABLE_NAME);
		sqlBuilder.append(" left join ");
		sqlBuilder.append(Passengers.TABLE_NAME);
		sqlBuilder.append(" on ");
		sqlBuilder.append(Sell.TABLE_NAME);
		sqlBuilder.append(DOT);
		sqlBuilder.append(Sell.PNR);
		sqlBuilder.append(EQUALTO );
		sqlBuilder.append(Passengers.TABLE_NAME);
		sqlBuilder.append(DOT);
		sqlBuilder.append(Passengers.PNR_ID);
		sqlBuilder.append(" where ");
		sqlBuilder.append(Sell.TABLE_NAME);
		sqlBuilder.append(DOT);
		sqlBuilder.append(Sell.PNR);
		sqlBuilder.append(EQUALTO);
		sqlBuilder.append(pnr);

		String sql = sqlBuilder.toString();
		logger.info("join pnr and passengers sql >> " +sql);
		JSONObject output = null;
		try (Connection con = SQLUtil.getSqlConnection())
		{
			Statement stmt = con.createStatement();
			stmt.execute("set search_path to share");

			stmt = con.createStatement();
			
			ResultSet rows= stmt.executeQuery(sql);
			output = new JSONObject();
			JSONArray passengers = new JSONArray();
			int seats = 0;
		
			while(rows.next())
			{
				JSONObject seat = new JSONObject();
				JSONArray passenger = new JSONArray();
				seat.put(String.valueOf(seats), passenger);
				passengers.put(passenger);
				
				passenger.put(rows.getString(Passengers.BOOKING_STATUS).trim());
				passenger.put(rows.getString(Passengers.CURRENT_STATUS).trim());
				passenger.put(rows.getString(Passengers.PASSENGERS_ID));
				
				++seats;
				
				if(rows.isFirst())
				{
					output.put("extraCount", rows.getInt(Sell.EXTRA_PASSENGER));
					output.put("notify", rows.getInt(Sell.NOTIFY));
					output.put("mediate", rows.getInt(Sell.MEDIATE));
				}
				
			}
			output.put("passengers", passengers);
		} catch (Exception e)
		{
			logger.log(Level.WARNING, "Exception Occured", e);
		}
		return output;
	}

	@Override
	public JSONArray getAllStationInfo()
	{
		JSONArray stationsInfo = new JSONArray();

		// source: [{
		// label: "Tom Smith",
		// value: "1234"},
		// {
		// label: "Tommy Smith",
		// value: "12321"}],

		synchronized (TrainQueryImpl.class)
		{
			if (SMTConstants.STATION_INFO == null)
			{
				String sql = "SELECT * FROM " + StationInfo.TABLE_NAME;

				try (Connection con = SQLUtil.getSqlConnection())
				{
					Statement stmt = con.createStatement();
					stmt.execute("set search_path to share");

					stmt = con.createStatement();
					ResultSet rows = stmt.executeQuery(sql);

					while (rows.next())
					{
						JSONObject stationInfo = new JSONObject();
						stationInfo.put("label", rows.getString(StationInfo.NAME).trim() + "-" + rows.getString(StationInfo.CODE).trim());
						stationInfo.put("value", rows.getString(StationInfo.CODE).trim());
						stationsInfo.put(stationInfo);
					}
				} catch (Exception e)
				{
					logger.log(Level.WARNING, "Exception Occured", e);
				} finally
				{
					if (stationsInfo.length() > 0)
					{
						SMTConstants.STATION_INFO = stationsInfo;
					}
				}
			}
			else
			{
				stationsInfo = SMTConstants.STATION_INFO;
			}
		}
		return stationsInfo;
	}

}
