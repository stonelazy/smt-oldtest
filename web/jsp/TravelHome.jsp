<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>

<div class="serviceAction" id="serviceAction">
 <nav class="navbar-service navbar" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
            </div>
<!--             Collect the nav links for toggling -->
            <div class="collapse navbar-collapse navbar-ex1-collapse">
                <ul class="nav navbar-nav navbar-left">
                    <li><a href="#irctc" onclick="pnrStatusClick('viewPnr')" >PNR Status</a>
                    </li>
                    <li><a href="#buyIrctc" onclick="buyTrainTkt('buyTrainTktNavButton')"  >BUY</a>
                    </li>
                    <li><a href="#sellIrctc" onclick="sellPnrclick('sellPnrNavButton')" >SELL</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    	<div class="actionPage" id="actionPage">
    		<div id="actionPagePrimary" class="actionPage" > </div>
    	</div>
</div>

<script>
$(document).ready(function (){
// 	pnrStatusClick('viewPnr');
	sellPnrclick('sellPnrNavButton')
});

</script>

</body>
</html>