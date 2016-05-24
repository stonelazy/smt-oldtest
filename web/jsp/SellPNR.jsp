<%@ page language="java" contentType="text/html; charset=UTF-8" import="org.json.JSONObject,org.json.JSONArray,com.share.bean.PNR,java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<div id="sellPnrHome"  class="text-center free-div">
	
			<div id="breadCrumb" class="breadcrumb" style="display: none;">
<!-- 				<a href="#" id="checkStatusCrumb" onclick="startSellingProcess()" class="active" disabled="disabled">Check Status</a> -->
<!-- 				<a href="#" id="pickSeatsCrumb" onclick="sellPnrStatusSuccess($('#pickSeatsData').html())">Pick Seats</a> -->
<!-- 				<a href="#" id="notificationCrumb" onclick="pickSeatsSuccess($('#notificationData').html())">Notification</a> -->
<!-- 				<a href="#" id="confirmationCrumb" onclick="notificationSuccess($('#confirmationData').html())">Confirmation</a> -->

				<a href="#" id="checkStatusCrumb" class="active">Check Status</a>
				<a href="#" id="pickSeatsCrumb" >Pick Seats</a>
				<a href="#" id="notificationCrumb" >Customise</a>
				<a href="#" id="confirmationCrumb">Confirmation</a>
			</div>
	<div id="continueSelling1" onclick="sellPnrclick('continueSelling')" class="btn btn-default btn-primary pull-right collapse"> Continue </div>
	
		<div id="sellPnrList">
			<%		
			if(request.getAttribute( "sellPnrArray") !=null)
          	{ 
				JSONArray sellPnrArray = (JSONArray) request.getAttribute("sellPnrArray");
			    if(sellPnrArray.length() == 0)
			    {
			    	%>
						<h5>You haven't shared any tickets with us.. Start sharing now by entering the PNR </h5>
			    	<%
			    }
			    else
			    {
			    	%>
				<table  class="table table-bordered table-condensed table-responsive" >
				<tbody>
					<tr>
						<th class="text-center" >PNR</th>
						<th class="text-center" >Train name</th>
						<th class="text-center" >From Station</th>
						<th class="text-center" >To Station</th>
						<th class="text-center" >DOJ</th>
						<th class="text-center" >Sharing seat count</th>
					</tr>
				</tbody>
				
				<tbody>
					<%
			    	
			    	for(int i=sellPnrArray.length()-1;i>=0;i--)
			    	{
			    		JSONObject sellPnr = sellPnrArray.getJSONObject(i);
			  			%>
			  			<tr>
			  			<td><%=sellPnr.get("PNR_NUMBER") %> </td>
			  			<td><%=sellPnr.get("TRAIN_NUMBER") %> </td>
			  			<td><%=sellPnr.get("FROM_STZ") %> </td>
			  			<td><%=sellPnr.get("TO_STZ") %> </td>
			  			<td><%=new Date(sellPnr.getLong("DOJ")) %> </td>
			  			<td><%=sellPnr.get("EXTRA_PASSENGER") %> </td>
                        <td> 
                           <img id="<%=sellPnr.get("PNR_NUMBER") %>" style="cursor: pointer;" onclick="sellPnrclick('delSharedPnr',this)"  src="images/delete_icon.gif" height="15" width="15" />
                           </td>
                       </tr>
			  			<% 		
			    	}
					%>
					
				</tbody>	
				</table>
					<%
			    }
        	  
          	}
			else
			{
				%>
					<h5>You haven't shared any tickets with us.. Start sharing now by entering the PNR1 </h5>
	    		<%
			}
        %>

		</div>
		
		<div id="enterPnrBox" >
		
		<h5>Enter the PNR of ticket you would like to Sell.. </h5>
			<input type="text" id="pnrNumber" value="4631174536">
			 <br /> <br />
	
			<button type="submit" class="btn btn-default" onclick="sellPnrclick('getPnrStatus',pnrNumber)" id="getPnrStatus">Sell PNR</button>
			<br/>
		</div>
		
		<div id="actionResult"> </div>
		<div id="pickSeatsData"> </div>
		<div id="notificationData"> </div>
		<div id="confirmationData"> </div>
		
		<div id="sellErrorMsg" class="text-warning" > </div>
<!-- 		<div id="sellThis" onclick="sellPnrclick('startSelling')" class="btn btn-default btn-primary collapse"> Sell this </div> -->
		<div id="continueSelling" onclick="sellPnrclick('continueSelling')" class="btn btn-default btn-primary pull-right collapse"> Continue </div>
		<div id="orderConfirmation" onclick="sellPnrclick('orderConfirm')" class="btn btn-default btn-primary collapse"> Confirm Order </div>
		<br /><br />
	</div>
</body>
</html>
