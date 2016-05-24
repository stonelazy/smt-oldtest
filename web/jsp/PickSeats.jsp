<%@ page contentType="text/html; charset=UTF-8"
	import="org.json.JSONObject,org.json.JSONArray,com.share.bean.PNR,com.share.util.TrainUtil,com.share.bean.Passenger"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<body>
		<%
			PNR pnr = (PNR) request.getAttribute("pnr");
		%>

		<br /> <br />
	<h3>Pick the seats you would wish to sell:</h3>

<!-- 	<br /> Train Number : -->
<%-- 	<%=pnr.getTrainNumber()%> --%>
<!-- 	<br /> Train Name : -->
<%-- 	<%=pnr.getTrainName()%> --%>
<!-- 	<br /> Total passengers allowed in this PNR: -->
<%-- 	<%=pnr.getTotalPassengersCount()%> --%>
<!-- 	<br /> From station : -->
<%-- 	<%=pnr.getFromStation().getName()%> --%>
<!-- 	<br /> To station : -->
<%-- 	<%=pnr.getToStation().getName()%> --%>
<!-- 	<br /> Date of Journey : -->
<%-- 	<%=pnr.getDoj()%> --%>

	<table  class="table table-bordered table-condensed table-responsive" >
		<tbody>
			<tr>
				<th class="text-center" >Select</th>
				<th class="text-center" >Current Status</th>
				<th class="text-center" >Coach Position</th>
			</tr>
		</tbody>
		
		<tbody>
			<%
				for (int i = 0; i < pnr.getPassengers().length(); i++)
				{
					Passenger passenger = ((Passenger) pnr.getPassengers().get(i));
					String currentStatus = passenger.getCurrentStatus();

					if (TrainUtil.isPassengerStatusSellable(currentStatus))
					{
			%>
			<tr>
				<td><input type="checkbox" name="confirmedSeats" value="<%=passenger.getIdentifyPassenger()%>"></td>
				<td><%=passenger.getCurrentStatus()%></td>
				<td><%=passenger.getIdentifyPassenger()%></td>
				<td><%=passenger.getCoachPosition()%></td>
			</tr>
			<%
				    }
					else
					{
			%>
			<tr>
				<td>
						<input type="checkbox" class="has-warning" readonly="readonly" disabled="disabled" >
				</td>
				<td> <del>
						<%=passenger.getCurrentStatus()%>
				</del></td>
				<td><del>
						<%=passenger.getCoachPosition()%>
					</del></td>
			</tr>
			<%
					}
				}
			%>
		</tbody>
	</table>

</body>
</html>