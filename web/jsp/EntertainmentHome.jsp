<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>

	<div class="serviceAction">
		<nav class="navbar-service navbar" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-ex1-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
			</div>
			<!--             Collect the nav links for toggling -->
			<div class="collapse navbar-collapse navbar-ex1-collapse">
				<ul class="nav navbar-nav navbar-left">
					<li><a href="#newRelease"> New Releases</a></li>
					<li><a href="#buyMovie">Buy</a></li>
					<li><a href="#sellMovie">Sell </a></li>
				</ul>
			</div>
		</div>
		</nav>

		<div class="actionPage" id="entertainmentPage">
			<div id="entertainmentActionPage" style="text-align: center;">
				We are developing sooner to share all those extra movie tickets you
				got.. <br />
				<br />
				<br /> 
				
				<a href="#" class="btn btn-default btn-primary"
					id="entertainmentHome" onclick="entertainmentPage('wantNow')">
					Click Me 
				</a> 
				to show that you need this developed now more than ever.
				
			</div>
		</div>
	</div>
	<script>
$(document).ready(function (){
// 	pnrStatusClick('viewPnr');
// 	sellPnrclick('sellPnrNavButton')
});

</script>

</body>
</html>