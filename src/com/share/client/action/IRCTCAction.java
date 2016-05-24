//$Id$

package com.share.client.action;

import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.server.constants.RegexConstants;
import com.server.sql.QueryExecutor;
import com.server.sql.QueryExecutorImpl;
import com.server.sql.util.SQLUtil;
import com.server.util.CommonUtil;
import com.server.util.DateUtil;
import com.share.api.services.API;
import com.share.api.services.RailwayAPIImpl;
import com.share.bean.PNR;
import com.share.bean.Passenger;
import com.share.bean.Train;
import com.share.client.bean.Accounts;
import com.share.constants.ActionConstants;
import com.share.constants.StrutsConstants;
import com.share.constants.SymbolConstants;
import com.share.error.MessageConstants;
import com.share.services.AccountsServices;
import com.share.sql.metadata.Sell;
import com.share.sql.metadata.Sell.SellConstants;
import com.share.sql.query.TrainQuery;
import com.share.sql.query.TrainQueryImpl;
import com.share.util.AccountsUtil;
import com.share.util.TrainUtil;

public class IRCTCAction
{
	private static Logger logger = Logger.getLogger(IRCTCAction.class.getName());
	public String action;
	public String mode;
	public long pnrNumber;
	private String sellPnr;
	private boolean smtMediate;
	private String fromStz;
	private String toStz;
	private String doj;
	
	public String execute() throws SignatureException, IOException
	{
		String result = StrutsConstants.ERROR;
		HttpServletRequest request = ServletActionContext.getRequest();

		try
		{
			logger.info("action>> " + getAction() + " mode>> " + getMode());

			if ("pnr".equals(getMode()))
			{
				if (getAction().equals(ActionConstants.VIEW))
				{
					API aa = new RailwayAPIImpl();
					PNR pnr = aa.getPnrObject(getPnrNumber());

					if (pnr == null || pnr.isError())
					{
						JSONObject msg = MessageConstants.generateJsonForMsg(500, "Seeems to be a wrong PNR");
						CommonUtil.writeResponse(msg.toString());
						result = StrutsConstants.NONE;
					}
					else
					{
						logger.info("isthispnrsellable>> " + pnr.isSellable());
						result = StrutsConstants.VIEW;
					}
					request.setAttribute("pnr", pnr);
					// JDBCTest.postgresMethod1();
				}
			}
			else if ("sellPnr".equals(getMode()))
			{
				logger.info("inside sellpnr ");

				if (getAction().equals(ActionConstants.VIEW))
				{
					logger.info("inside view sellpnr");
					request.setAttribute("sellPnrArray", viewAllSharedTickets());
					result = StrutsConstants.VIEW;
				}
				else if (getAction().equals(ActionConstants.ADD))
				{
					long uid = AccountsUtil.getCurrentUser().getUid();
					logger.info("inside add sellpnr");
					int extraPassenger = 0;
					JSONObject response = null;

					if (CommonUtil.isValid(getPnrNumber()))
					{
						API aa = new RailwayAPIImpl();
						PNR pnr = aa.getPnrObject(getPnrNumber());
						request.setAttribute("pnr", pnr);

						if (pnr.isSellable())
						{
							TrainQuery query = new TrainQueryImpl();
							int status = query.addTicket(pnr.getTrainNumber(), pnr.getPnrNumber(), pnr.getFromStation().getCode(), pnr.getToStation().getCode(), pnr.getDoj(), extraPassenger, uid);
							logger.info("status of insert>> " + status);
							if (status == MessageConstants.OP_SUCCESS)
							{
								result = StrutsConstants.PICK_SEATS;
								logger.info("ticket successfully inserted");
							}
							else if (status == MessageConstants.DUPLICATE_RESOURCE)
							{
								// response = MessageConstants.generateJsonForMsg(425, "This ticket was already shared with us, so you can't sell !");
								request.setAttribute("sellPnrArray", viewAllSharedTickets());
								result = StrutsConstants.PICK_SEATS;
							}
							else
							{
								response = MessageConstants.generateJsonForMsg(501, "Internal Error !");
							}
						}
						else
						{
							int reason = TrainUtil.isTicketSellable(pnr);

							if (reason == MessageConstants.TEMP_ERROR)
							{
								// query it again
							}
							else if (reason == MessageConstants.OP_SUCCESS)
							{
								response = MessageConstants.generateJsonForMsg(501, "Please try again");
								// empty seats are available and shud give ui with bread crumbs

								result = StrutsConstants.VIEW;
							}
							else if (reason == MessageConstants.CHART_PREPARED)
							{
								response = MessageConstants.generateJsonForMsg(425, "Uh oh! The chart is already prepared.. you can't sell it now");
							}
							else if (reason == MessageConstants.NO_CONFIRMED_SEAT)
							{
								response = MessageConstants.generateJsonForMsg(425, "You can't share this PNR because there are no confirmed seats available");
								// no fucking confirmed seat and he wants to sell!
							}
							else if(reason == MessageConstants.EXPIRED_JOURNEY)
							{
								response = MessageConstants.generateJsonForMsg(425, "Unfortunately we dont have time travel skills, please share us a valid PNR");
							}
						}
					}
					else
					{
						logger.info("pnr number is null.. so cant proceed");
						response = MessageConstants.generateJsonForMsg(425, "Entered PNR is invalid");
					}

					if (response != null)
					{
						HttpServletResponse httpResponse = ServletActionContext.getResponse();
						logger.info("sent response>> " + response);
						httpResponse.getWriter().println(response.toString());
						httpResponse.setStatus(425);
						result = StrutsConstants.NONE;
					}
				}
				else if (getAction().equals(ActionConstants.ORDER_CONFIRM))
				{
					logger.info("inside orderconfirm");
					logger.info("getsellpnr>> " + getSellPnr());
					// {"passengers":{"1":"current_status:CNF ,coach_position:0 ,booking_status:D2,25,GN","0":"current_status:CNF ,coach_position:2 ,booking_status:WL"},
					// "status":3,"notify":[{"notifyMobile":"0"},
					// {"notifyEmail":"sudharsankp@gmail.com"}],
					// "pnrNumber":"4631174536"}
					JSONObject sellPnrObj = new JSONObject(getSellPnr());
					
					long pnr = sellPnrObj.getLong("pnrNumber");
					setPnrNumber(pnr);
					API aa = new RailwayAPIImpl();
					PNR pnrObj = aa.getPnrObject(pnr);
					JSONArray passengersArray = pnrObj.getPassengers();
					Accounts user = AccountsUtil.getCurrentUser();
					int smtMediate = 0;

					if (!sellPnrObj.isNull("smtMediate"))
					{
						smtMediate = sellPnrObj.getBoolean("smtMediate") ? 1 : 0;
					}

					JSONObject passengersJson = sellPnrObj.isNull("passengers") ? null : sellPnrObj.getJSONObject("passengers");
					
					try
					{
						int extraCount = 0;
						
						if(passengersJson != null)
						{
							do
							{
								String identifyPassenger = passengersJson.getString(String.valueOf(extraCount));
								logger.info("passenger>> " + identifyPassenger);
								logger.fine("pass count>> " + extraCount);
								String currentStatus = null;
								String bookingStatus = null;
								
								for (int i = passengersArray.length() - 1; i >= 0; i--)
								{
									Passenger temp = (Passenger) passengersArray.get(i);
									if (identifyPassenger.equalsIgnoreCase(temp.getIdentifyPassenger()))
									{
										currentStatus = temp.getCurrentStatus();
										bookingStatus = temp.getBookingStatus();
										logger.info("it is the same>> " + identifyPassenger);
										break;
									}
									else
									{
										logger.fine("not the same");
									}
								}
								
								TrainQuery query = new TrainQueryImpl();
								query.addShareablePassenger(pnr, identifyPassenger, user.getUid(), currentStatus, bookingStatus);
								++extraCount;
							} while (!passengersJson.isNull(String.valueOf(extraCount)));
						}

						Long updatedMobileTo = null;
						int notifyStatus = 0;
						String updatePostal = null;

						if (!sellPnrObj.isNull("notify"))
						{
							Sell sell = new Sell();
							SellConstants constants = sell.new SellConstants();
							JSONObject notifyJson = sellPnrObj.getJSONObject("notify");

							if (!notifyJson.isNull("notifyMobile"))
							{
								logger.info("notify mobile entered > " + notifyJson.getString("notifyMobile"));

								if (RegexConstants.MOBILE.matcher(notifyJson.getString("notifyMobile")).matches())
								{
									logger.info("thro mobile>> " + notifyJson.getString("notifyMobile"));
									constants.setMobile(true);

									if (user.getMobile() != notifyJson.getLong("notifyMobile"))
									{
										updatedMobileTo = notifyJson.getLong("notifyMobile");
									}
								}
								else
								{
									logger.info("failed in the mobile regex pattern>> " + notifyJson.getString("notifyMobile"));
								}
							}

							if (!notifyJson.isNull("notifyEmail"))
							{
								logger.info("thro notifyEmail>> " + notifyJson.get("notifyEmail"));

								logger.info("thro notifyEmail>> " + notifyJson.getString("notifyEmail"));
								constants.setEmail(true);
							}

							if (!notifyJson.isNull("notifyPostal") && CommonUtil.isValid(notifyJson.get("notifyPostal")))
							{
								logger.info("pain in the ass wants postal notify>> " + notifyJson.get("notifyPostal"));
								constants.setPostal(true);
								updatePostal = notifyJson.getString("notifyPostal");
							}
							notifyStatus = constants.getNotifyStatus();
						}

						if (updatedMobileTo != null || updatePostal != null) AccountsServices.updateUserInfo(updatedMobileTo, updatePostal, user.getUid());

						TrainQuery query = new TrainQueryImpl();
						query.updateSellPnr(pnr, notifyStatus, extraCount, smtMediate);

						logger.info("notify status>> " + notifyStatus);

						logger.info("goin to generate orderconfirmation for pnr>> " + pnr);
						JSONObject responseJson = query.getSellPnrJson(pnr);
						logger.info("response json>> " + responseJson);

						request.setAttribute("response", responseJson);
						request.setAttribute("pnr", pnrObj);

						result = StrutsConstants.CONFIRM_ORDER;
						// select * from sell left join passengers on sell.pnr_number=passengers.pnr_number where sell.pnr_number=4631174536;
					} catch (Exception e)
					{
						logger.log(Level.WARNING, "Exception Occured", e);
					}
					logger.info("selpnrojbjbj>> " + sellPnrObj);
				}
				else if (getAction().equals(ActionConstants.SMT_MEDIATE))
				{
					logger.info("gotcha smtmediate action");
					logger.info("is smt mediate>> " + isSmtMediate() + " for pnr>> " + getPnrNumber());
				}
				else if(getAction().equals(ActionConstants.DELETE))
				{
					Map<String,List<Object>> queryValues = new HashMap<>();
					
					List<Object> pnrList = new ArrayList<>();
					pnrList.add(getPnrNumber());
					queryValues.put(Sell.PNR, pnrList);
					
					String deleteCriteria = SQLUtil.formInCritieraQuery(queryValues,Sell.TABLE_NAME);
					String sql = "DELETE FROM " + Sell.TABLE_NAME + " "+ deleteCriteria;
					logger.info("delete query>> " + sql);
					QueryExecutor executor = new QueryExecutorImpl();
					int dbResult = executor.executeUpdate(sql);
					logger.info("db result>> " + dbResult);
					
					request.setAttribute("sellPnrArray", viewAllSharedTickets());
					result = StrutsConstants.VIEW;
				}
			}
			else if("buyPnr".equals(getMode()))
			{
				logger.info("buypnr mode");
				if(getAction().equals(ActionConstants.STATION_INFO))
				{
					logger.info("got stationinfo action");
					TrainQuery query = new TrainQueryImpl();
					JSONArray stationInfo = query.getAllStationInfo();
					logger.info("station info> " + stationInfo);
					CommonUtil.writeResponse(stationInfo);
					result = StrutsConstants.NONE;
				}
				else if(getAction().equals(ActionConstants.SEARCH_TRAIN))
				{
					logger.info("gona start headache");
					
//					http://api.railwayapi.com/between/source/<station code>/dest/<station code>/date/dd-mm/apikey/<apikey>/
					
					API aa = new RailwayAPIImpl();
					 
					List<Train> trainList = aa.getTrainsBetween("TPJ", "MAS", "12-05-2013");
					List<Long> trainNumber = new ArrayList<>();
					
					for(int i=trainList.size()-1;i>=0;i--)
					{
						logger.config("available trian number>> " + trainList.get(i).getTrainNumber());
						trainNumber.add(trainList.get(i).getTrainNumber());
					}
					logger.config("printlalaa of train list>> " + trainList);
					
					logger.info("changed milllis");
					Long millis = DateUtil.parseDateToMillis("12-05-2013");
					
					if(!trainNumber.isEmpty())
					{
						Map<String,Object> criteriaMap = new HashMap<>();
						criteriaMap.put(Sell.TRAIN_NUMBER,trainNumber);
						criteriaMap.put(Sell.DOJ,new HashMap<String,Object>().put(SymbolConstants.LEFT_ANGULAR_BRACKET, millis + DateUtil.getDaysInMilis(2)));
						criteriaMap.put(Sell.DOJ,new HashMap<String,Object>().put(SymbolConstants.RIGHT_ANGULAR_BRACKET, millis - DateUtil.getDaysInMilis(2)));
						
						List<String> selectCols = new ArrayList<>();
						selectCols.add(Sell.DOJ);
						selectCols.add(Sell.SELL_ID);
						
						String sql = SQLUtil.formSelectQuery(Sell.TABLE_NAME, selectCols, criteriaMap);
						logger.info("trains avaialble with us sql>> "+  sql);
						
					}
					
					logger.info("getafromastz>> " + getFromStz() + " tostz>> " + getToStz() + "  doj>> " + getDoj());
				}
				
				else
				{
					logger.info("no action matched");
					result = StrutsConstants.VIEW;
				}
			}
		} catch (Exception e)
		{
			logger.log(Level.WARNING, "Exception Occured", e);
			request.setAttribute("errorMessage", "Unknown error");
			logger.info("this is an unknown error, result>> " + result);
		}
		logger.info("sent result>> " + result);
		return result;
	}

	private Object viewAllSharedTickets()
	{
		TrainQuery query = new TrainQueryImpl();
		JSONArray json = query.getSharedTickets(AccountsUtil.getCurrentUser().getUid());
		logger.info("json>> " + json);
		return json;
	}

	public String sellPnr()
	{
		return "none";
	}

	public long getPnrNumber()
	{
		return pnrNumber;
	}

	public void setPnrNumber(long pnrNumber)
	{
		this.pnrNumber = pnrNumber;
	}

	public String getSellPnr()
	{
		return this.sellPnr;
	}

	private String getAction()
	{
		return action;
	}

	private void setAction(String action)
	{
		this.action = action;
	}

	private String getMode()
	{
		return mode;
	}

	private void setMode(String mode)
	{
		this.mode = mode;
	}

	public void setSellPnr(String sellPnr)
	{
		this.sellPnr = sellPnr;
	}

	public boolean isSmtMediate()
	{
		return smtMediate;
	}

	public void setSmtMediate(boolean smtMediate)
	{
		this.smtMediate = smtMediate;
	}

	public String getFromStz()
	{
		return fromStz;
	}

	public void setFromStz(String fromStz)
	{
		this.fromStz = fromStz;
	}

	public String getToStz()
	{
		return toStz;
	}

	public void setToStz(String toStz)
	{
		this.toStz = toStz;
	}

	public String getDoj()
	{
		return doj;
	}

	public void setDoj(String doj)
	{
		this.doj = doj;
	}

}
