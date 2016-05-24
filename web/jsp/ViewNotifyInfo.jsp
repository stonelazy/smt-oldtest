<%@ page contentType="text/html; charset=UTF-8"
	import="org.json.JSONObject,org.json.JSONArray,com.share.client.bean.Accounts,com.server.util.CommonUtil"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<body>
<%
	Accounts currentUser = (Accounts) request.getAttribute("currentUser");
//onclick="if($(this).is(':checked')) $('#mobile').show(); else $('#mobile').hide();"
%>
                   
	<div class="row">
		<div class="col-md-12 contact-cls ">
			<h4>How do we notify you once we match a buyer ?</h4>

			<div>
				<span> <input type="checkbox" class="checkbox-inline" name="notify" onclick="if($(this).is(':checked')) $(this).next().next().show(); else  $(this).next().next().hide();"> 
				<i class="fa fa-phone">  Mobile</i>&nbsp;
					<input class="collapse small form-control" type="number" id="notifyMobile" value="<%=CommonUtil.isValid(currentUser.getMobile())?currentUser.getMobile():' '%>">
				</span> 
				<br /> 
				
				<span> <input type="checkbox" class="checkbox-inline" name="notify" autofocus="autofocus" checked="checked"  onclick="if($(this).is(':checked')) $(this).next().next().show(); else  $(this).next().next().hide();"> 
					<i class="fa fa-envelope-o">  Email</i>&nbsp; 
						<input class="collapse small form-control" id="notifyEmail" type="text" disabled="disabled" value="<%=CommonUtil.isValid(currentUser.getEmail()) ? currentUser.getEmail(): ' '%>">
				</span> 
				<br /> 
				
				 <span> <input type="checkbox" class="checkbox-inline" name="notify"  onclick="if($(this).is(':checked')) $(this).next().next().show(); else  $(this).next().next().hide();"> 
				 	<i class="fa fa-home">  Postal</i>&nbsp;
					<textarea class="collapse small form-control" id="notifyPostal" rows="2" cols="20"><%=CommonUtil.isValid(currentUser.getPostalAddress()) ? currentUser.getPostalAddress():" "%>
					</textarea>
				</span> 
				<br />
 			</div>
		</div>
	</div>
<br /> 
<br /> 

	<div class="row">
		<div class="col-md-12">
			<span>  In case we find a matching buyer of this ticket </span>
			<br /> 
			<br /> 
			<span>	<input id="option1Radio" type="radio" name="wayToShare" value="option1"  onclick="wayToShare(this,true)" checked="checked"> Let sharemyticket give your contact info
			to the matching buyer. 
			</span>
			<br /> 
			<span>
			<input id="option2Radio" type="radio" name="wayToShare" value="option2" onclick="wayToShare(this,confirm('Are you sure ? This might take some delay'))">
			Let sharemyticket mediate and get the ticket confirmation (Risky - Not Recommended)
			<br /> 
			<br /> 
			</span>
		</div>
	</div>

</body>
</html>