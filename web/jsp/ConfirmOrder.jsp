<%@ page contentType="text/html; charset=UTF-8"
	import="org.json.JSONObject,org.json.JSONArray,com.share.bean.PNR,com.share.util.TrainUtil,com.share.bean.Passenger"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<body>

	<!-- {
"passengers":
[
{"0":["D3,33,WL1","CNF"]},
{"1":["D4,15,WL","CNF"]},
{"2":["D2,25,GN","CNF"]},
{"18":["D2,25,GN","CNF"]}
],
"notify":3,
"extraCount":2
}
 -->

Thanks for sharing the ticket with us !! 
These are your order details..

	<table class="table table-bordered table-condensed table-responsive">
		<tbody>
			<tr>
				<th class="text-center" >Current Status</th>
				<th class="text-center" >Booking status</th>
			</tr>
		</tbody>
		<tbody>

 <% 
 JSONObject info = (JSONObject) request.getAttribute("response"); 
 JSONArray passengers = info.getJSONArray("passengers");
  PNR pnr = (PNR) request.getAttribute("pnr");
 for (int i=0;i<passengers.length();i++)
 {
	 JSONArray seat = passengers.getJSONArray(i);
	 %>
			<tr>
				<td><%=seat.get(1)%></td>
				<td><%=seat.get(0)%></td>
			</tr>
	<%
 }
 %>
 	   </tbody>
	</table>
<p>

 From station <%=pnr.getFromStation().getName() %>
<br/> 
 To Station <%=pnr.getToStation().getName() %>
 <br/>
 Total passengers allowed in this PNR <%=pnr.getTotalPassengersCount() %>
<br/> 
 Total seats you are sharing <%=info.getInt("extraCount") %>
 <br/>
 
 
<% if(info.getInt("mediate") == 1) { %>
<div class="text-danger">
We will coordinate with the matching buyer and get you ticket, although this is not a recommended practise.
</div>
<%} else { %>
<div class="text-success">
We will share your contact info with the matching buyer 
</div>
<%} %>
</p>
</body>
</html>