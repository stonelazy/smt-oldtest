<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="org.json.JSONObject,org.json.JSONArray,java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
	<div id="buyPnrHome" class="text-center free-div">
		<p>
<!-- 			Date Of Journey: <input type="text" id="datepicker">  -->

 		<label for="fromstz">
 			From station: </label>
		<input type="text" id="fromstz" />
		<br/><br/>
		
		<label for="tostz">
 			To station: </label>
		<input type="text" id="tostz" />
		<br/><br/>
		
		<label for="datepicker">
 		Date Of Journey: </label>
		 <input type="text" id="datepicker"> 
		<br/><br/>
		
		<div id="buyPnrSearch" onclick="buyTrainTkt('buyPnrSearch')" class="btn btn-default btn-primary"> Search </div>
		
		</p>
	</div>
</body>


<script>
	
	$(document).ready(function (){
		buyTrainTkt('getStationInfo')
	});

	$(function() {
		$("#datepicker").datepicker({
			minDate : 0,
			maxDate : "+3M +10D",
		});
		$( "#datepicker" ).datepicker( "option", "dateFormat", "dd-mm-yy");
	});

	var sinfo = [];
	var stinfo = {};
	stinfo.label="TPJ Tiruchirapalli Junction";
	stinfo.value="TPJ";
	sinfo[0]=stinfo;
	stinfo = {};
	stinfo.label="DICK you are dick";
	stinfo.value="DICK";
	sinfo[1]=stinfo;
	
	$("#fromstz").autocomplete({
// 	    source: [{
// 	        label: "Tom Smith",
// 	        value: "1234"},
// 	    {
// 	        label: "Tommy Smith",
// 	        value: "12321"}],

 		source: JSON.parse(stationInfo),
// 		source: sinfo,
	    minLength: 1,
	    select: function(event, ui) {
	        event.preventDefault();
	        $("#fromstz").val(ui.item.label);
	        $("#fromstz").val(ui.item.label);
	    },
	    focus: function(event, ui) {
	        event.preventDefault();
	        $("#fromstz").val(ui.item.label);
	    }
	});
	
	$("#tostz").autocomplete({
 		source: JSON.parse(stationInfo),
	    minLength: 1,
	    select: function(event, ui) {
	        event.preventDefault();
	        $("#tostz").val(ui.item.label);
	        $("#tostz").val(ui.item.label);
	    },
	    focus: function(event, ui) {
	        event.preventDefault();
	        $("#tostz").val(ui.item.label);
	    }
	});
	// http://jqueryui.com/datepicker/#date-range use this for date range
</script>

</html>