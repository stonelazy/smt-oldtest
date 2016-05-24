<%@ page contentType="text/html; charset=UTF-8"
	import="org.json.JSONObject,org.json.JSONArray,com.share.bean.PNR"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<body>
			<%
//4631174536
				PNR pnr = (PNR) request.getAttribute("pnr");
				if (!pnr.isError())
				{
			%>
			
			 <br /> 			 
			 <br /> 
			<h3> Your Ticket status: </h3>

			<br /> 
			Train Number :
			<%=pnr.getTrainNumber()%>
			<br /> 
			Train Name :
			<%=pnr.getTrainName()%>
			<br />
			Passenger count:
			<%=pnr.getTotalPassengersCount()%>
			<br />
			From station :
			<%=pnr.getFromStation().getName()%>
			<br />
			To station :
			<%=pnr.getToStation().getName()%>
			<br />
			Date of Journey :
			<%=pnr.getDoj()%>
			<br />
			Chart status :
			<%=pnr.isChartPrepared()%>
			<div id="isSellable" class="collapse"><%=pnr.isSellable()%></div>
			<%
				}
				else
				{
			%>
			<div id="errorMessage" >
			
			</div>
			<%
				}
			%>
</body>
</html>