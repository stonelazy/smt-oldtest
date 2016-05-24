<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<!-- 	<div id="pnrHome" align="center" style="margin-top: 40px"> -->
	<div id="pnrHome"  class="text-center free-div">
		<h5>Enter your PNR Number</h5>
		<input type="text" id="pnrNumber" value="4631174536"> <br /> <br />

		<button type="submit" class="btn btn-default" onclick="pnrStatusClick('getPnrStatus',pnrNumber)" id="getPnrStatus">Get Status</button>
		
		<div id="pnrResult">
		</div>
		
		<br />
		<div id="sellFromBuy"  class="btn btn-default btn-primary collapse" > Do you want to Share this ticket ? </div>
		
	</div>
</body>
</html>