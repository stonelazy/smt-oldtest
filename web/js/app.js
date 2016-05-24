
$(document).ready(function() {
	onRefresh();
//	breadcrumbs();
});

//$(window).load(function() {
//	// Animate loader off screen
//	$(".se-pre-con").fadeOut("slow");
//});
//
//$(document).ajaxStart(function(){
//	$(".se-pre-con").fadeOut("slow");	
//});

$(document).ajaxStart(function(){
	$('#overlay').show();
	$('#ajaxLoading').show();	
});
$(document).ajaxComplete(function(){
	$('#overlay').hide();
	$('#ajaxLoading').hide();
});

var userAccount = null;
var stationInfo=null;
var sellPnr= {};
sellPnr.status=0;

function goHomeLater()
{
	console.log('am gona wait for 3s');
	window.location.reload();
}

function onRefresh()
{
	$.get("authInfo.do?action=check&mode=authInfo", function(msg) {
		if (IsJson(msg)) {
			var response = JSON.parse(msg);
			
			if (response && response.EMAIL) 
			{
				userAccount = response;
				$("#settingsButton").html(userAccount.EMAIL);
				$("#settingsButton").show();
				$("#signInButton").hide();
				
				$("#replyToEmail").val(userAccount.EMAIL);
				$("#replyToEmail").attr('disabled',true);
				
				if(userAccount.NAME)
				{
					$("#userName").val(userAccount.NAME);
					$("#userName").attr('disabled',true);
				}
			}
			window.scrollTo(0,0);
		} else {
			console.log('in else');
		}
	});
}

function comeInSubmit(emailAddress,password)
{
	var email= emailAddress.value;
	var pass = password.value;
	var json = {};
	json.emailAddress = email;
	json.password = pass;
	
	console.log('comeinsubmit ', emailAddress);
	
	$.post("login.action",json,function(msg){
		var response = JSON.parse(msg);
		userAccount = response.data;
			document.getElementById("comeInResult").innerHTML = response.message;
			if(response.code<300)
			{
				setTimeout(goHomeLater, 1500);
				$("#comeInResult").addClass('text-success');
			}
			else if(response.code > 300)
			{
				$("#comeInResult").addClass('text-danger');
//				setTimeout(goHomeLater, 3000);
			}
	});  
	
	return false;
}

function submitFeedBack(msg)
{
	console.log(msg.value);
	var json = {};
	json.email = $("#replyToEmail").val();
	json.userName = $("#userName").val();
	json.message = msg.value;
	json.mode = 'feedback';
	json.action = 'add';

	if (msg.value != "") {
		$.ajax({
			url : 'optauth/feedbackmessage.do',
			type : 'post',
			data : json
		}).done(function(data) {
			console.log('feedback submitted')
		}).error(function() {
			alert('failure');
		});
	}

	return false;
}

function mainServices(event)
{
	var $this = $(this);
	var $event = event;
	var $id = $event.target.id;
	console.log('sidebar click ', $id);
	
	if ($id.indexOf('travelHome') > -1) {
		console.log('travel');
		var jsonStr = {};
		jsonStr.mode='irctcHome';
		jsonStr.action='view';
		jsonStr.csrf='csrf';
	/*	
		$.ajax({
			url:'irctc.do',
			type:'get',
			data:jsonStr
		}).done(function(data){
			$('#mainContainer').html(data);
		}).error(function()
		{
			alert('failure');
		});
		*/
		$.ajax({
			url:'jsp/TravelHome.jsp',
			type:'get',
		}).done(function(data){
			$('#home').html(data);
		}).error(function()
		{
			alert('failure');
		});
	} else if($id.indexOf('signIn') > -1)
		{
			showSignInPage();
		}
	else if($id=="homeNavBar")
	{
		console.log('homenavbar');
	}
	else if($id == "entertainmentHome")
	{
		$('#home').html('get ready.. it will be out soon');
		$.ajax({
			url:'jsp/EntertainmentHome.jsp',
			type:'get',
		}).done(function(data){
			$('#home').html(data);
		}).error(function()
		{
			alert('failure');
		});
	}
}

function showSignInPage()
{
	$('#signInDiv').show();
	$("nav.navbar-inverse").hide();
	$('#home').hide();	
}

function goHome(isUserRequestNeeded)
{	
	if(isUserRequestNeeded)
		{
		$.get("getUserAccount.do",json,function(msg){
			userAccount = msg;
		});  
		}
	
	console.log('call home');
	$('#signInDiv').hide();
	$("nav.navbar-inverse").show();
	$('#home').show();
	window.scrollTo(0,0);
}

function pnrStatusClick(id,event)
{
	if (id.indexOf('getPnrStatus') > -1) {
		console.log('getpnr');
		var json = {};
		var isSellable = false;
		
		json.pnrNumber = $('#pnrNumber').val();
		json.mode='pnr';
		json.action='view';
		
		$.get("viewPnr.do",json,function(msg){
			$('#pnrResult').html(msg);
			isSellable = $("#isSellable").text();
			console.log('isSellable', isSellable,  ' !useraccount', !userAccount);
		
			if(userAccount && isSellable)
			{
				console.log('gona show sell button');
				$("#continueSelling").addClass('collapse.in');
				$("#continueSelling").removeClass('collapse');
//				$("#sellThis").removeClass('collapse'); //TODO
//				$("#sellThis").addClass('collapse.in');
				$("#pnrResult").addClass('text-success');
			}
			else
			{
				if(!isSellable)
				{
					$('#pnrResult').append('<br/> <div class="text-danger"> This ticket is not sellable </div>');
				}
				else if(!userAccount)
				{
					$.get("jsp/SignInConfirmation.jsp",function(msg){
						$('#pnrResult').html(msg);
						$('#customizedMsg').html('You need to be signed in to sell the ticket');
					}); 
				}
				$("#sellThis").removeClass('collapse.in');
				$("#sellThis").addClass('collapse');
			}
		});  
	} else if(id.indexOf('viewPnr')>-1){
		$.ajax({
			url : 'jsp/PNRHome.jsp',
			type : 'get',
		}).done(function(data) {
			$('#actionPage').html(data);
			
		}).error(function() {
			alert('failure');
		});
	}
}

function sellPnrclick(id,event)
{
	console.log('id ', id , '  event ', event);
	if(id == "sellPnrNavButton")
	{
		console.log('sellPnrNavButton succ');
		if(userAccount)
		{
			var json = {};
			json.mode='sellPnr';
			json.action='view';
			$.ajax({
				url : 'auth/sellPnr.do',
				type : 'get',
				data : json
			}).done(function(data) {
				$('#actionPage').html(data);
			}).error(function(data) {
				console.log('you are not signed up ',data);	
				$.get("jsp/SignInConfirmation.jsp",function(msg){
					$('#actionPage').html(msg);
				}); 
			});
		}
		else
		{
			console.log('you are not signed up');	
			$.get("jsp/SignInConfirmation.jsp",function(msg){
				$('#actionPage').html(msg);
			}); 
		}
	}
	else if (id == "getPnrStatus")
	{
		console.log('getpnr');
		var json = {};
		var isSellable = false;
		json.pnrNumber = $('#pnrNumber').val();
		json.mode='pnr';
		json.action='view';
		sellPnr.pnrNumber = json.pnrNumber; 
		
		$.get("viewPnr.do",json,function(data){
			$('#actionResult').html(data);
			isSellable = $("#isSellable").text();
			console.log('isSellable', isSellable,  ' !useraccount', !userAccount);
		
			if(userAccount && isSellable)
			{
				console.log('gona show sell button');
				startSellingProcess(data);
			}
			else
			{
				if(!isSellable)
				{
					$('#actionResult').append('<br/> <div class="text-danger"> This ticket is not sellable </div>');
				}
				else if(!userAccount)
				{
					$.get("jsp/SignInConfirmation.jsp",function(msg){
						$('#actionResult').html(msg);
						$('#customizedMsg').html('You need to be signed in to sell the ticket');
					}); 
				}
				$("#sellThis").removeClass('collapse.in');
				$("#sellThis").addClass('collapse');
				sellPnr.status = 0;
			}
		});  
	
	}
	else if(id == "startSelling")
 	{
		if (userAccount) {
			var json = {};
			json.mode = 'sellPnr';
			json.action = 'add';
			json.pnrNumber = $('#pnrNumber').val();
			sellPnr.pnrNumber = json.pnrNumber; 

			$.ajax({
				url : 'auth/sellPnr.do',
				type : 'post',
				data : json
			}).done(function(data) {
				console.log('success startselling', data);
				sellPnrStatusSuccess(data);
			}).error(function(data) {
				console.log('error startselling', eval(data).responseText);
				data = eval(data).responseText;
				
					var errorMsg = eval(JSON.parse(data));
					if (errorMsg.code > 424) {
						console.log('msg',errorMsg.message );
//						$('#pnrResult').append(data.message);
						$('#actionResult').append(errorMsg.message);
						$('#actionResult').addClass('text-danger');
						$('#sellThis').removeClass('collapse.in');
						$('#sellThis').addClass('collapse');
					} else {
						$.get("jsp/SignInConfirmation.jsp", function(msg) {
							$('#actionPage').html(msg);
						});
						sellPnr.status = 0;
					}
			});

		} else {
			$.get("jsp/SignInConfirmation.jsp", function(msg) {
				$('#actionPage').html(msg);
			});
		}
	}
	else if(id=="continueSelling")
	{
		console.log('pnrstatuss continue selling',sellPnr.status);
		
		if(sellPnr.status < 2)
		{
			if (userAccount) {
				var json = {};
				json.mode = 'sellPnr';
				json.action = 'add';
				json.pnrNumber = $('#pnrNumber').val();
				sellPnr.pnrNumber = json.pnrNumber; 

				$.ajax({
					url : 'auth/sellPnr.do',
					type : 'post',
					data : json
				}).done(function(data) {
					console.log('success startselling', data);
					sellPnrStatusSuccess(data);
				}).error(function(data) {
					console.log('error startselling', eval(data).responseText);
					data = eval(data).responseText;
					
						var errorMsg = eval(JSON.parse(data));
						if (errorMsg.code > 424) {
							console.log('msg',errorMsg.message );
//							$('#pnrResult').append(data.message);
							$('#actionResult').append(errorMsg.message);
							$('#actionResult').addClass('text-danger');
							$('#sellThis').removeClass('collapse.in');
							$('#sellThis').addClass('collapse');
						} else {
							$.get("jsp/SignInConfirmation.jsp", function(msg) {
								$('#actionPage').html(msg);
							});
							sellPnr.status = 0;
						}
				});

			} else {
				$.get("jsp/SignInConfirmation.jsp", function(msg) {
					$('#actionPage').html(msg);
				});
			}
		}
		else if(sellPnr.status == 2)
		{
			if(!$('[name="confirmedSeats"]').is(':checked'))
			{
				$("#sellErrorMsg").html("You should pick atleast one confirmed seat to proceed");
				$("#sellErrorMsg").removeClass('collapse');
				$("#sellErrorMsg").addClass('collapse.in');
			}
			else
 			{
//				console.log('output ',getJsonArrayOfSelectedCheckbox('confirmedSeats'));
				sellPnr.passengers=getJsonArrayOfSelectedCheckbox('confirmedSeats');
				
				$.ajax({
					url : 'auth/notifyInfo.do',
					type : 'get',
					data : ''
				}).done(function(data) {
					pickSeatsSuccess(data);
				}).error(function(data) {
					data = eval(data).responseText;

					var errorMsg = eval(JSON.parse(data));
					if (errorMsg.code > 424) {
						console.log('msg', errorMsg.message);
						$('#pickSeatsData').append(errorMsg.message);
						$('#pickSeatsData').addClass('text-danger');
						$('#continueSelling').removeClass('collapse.in');
						$('#continueSelling').addClass('collapse');
					} else {
						$.get("jsp/SignInConfirmation.jsp", function(msg) {
							$('#actionPage').html(msg);
						});
						sellPnr.status = 0;
					}
				});
			}
		}
		else if(sellPnr.status==3)
		{
			console.log('pnrstatuss supposed be three',sellPnr.status);
			
			if(!$('[name="notify"]').is(':checked'))
			{
				$("#sellErrorMsg").html("Please select atleast one medium of communication, Our word, we won't spam you.");
				$("#sellErrorMsg").removeClass('collapse');
				$("#sellErrorMsg").addClass('collapse.in');
			}
			else if(($('input[name=notify]:checked').length == 1) && $("#notifyPostal").prev().prev().is(':checked'))
			{
				console.log('only one box checked');
				$("#notifyPostal").prev().prev().is(':checked');
				{
					$("#sellErrorMsg").html("Woah! Are you serious ? The only way of notifying you should be postal ? It might take weeks!! ");
					$("#sellErrorMsg").removeClass('collapse');
					$("#sellErrorMsg").addClass('collapse.in');
				}
			}
			else
 			{
				var notifyInfo = $('input[name=notify]:checked').next().next();
				var isPassed = false;
				$('input[name=notify]:checked').attr('checked', true);
				var entry = {};
				for (var z = 0; z < notifyInfo.length; z++) {
					entry[notifyInfo[z].id] = notifyInfo[z].value;

					if (notifyInfo[z].id.indexOf('mobile') > -1) {
						if (isNaN(notifyInfo[z].value)) {
 
						}
					}
					isPassed = true;
				}
				sellPnr.notify = entry;

				console.log('notify info !!', sellPnr);

				if (isPassed) {
					var ajaxData={};
					ajaxData.sellPnr1=sellPnr;
					ajaxData.mode='sellPnr';
					ajaxData.action='orderConfirm';
					$.ajax({
						url : 'auth/sellPnr.do',
						type : 'post',
//						dataType: "json",
//						 contentType: "application/json; charset=utf-8",
						data : {sellPnr : JSON.stringify(sellPnr),action:'orderConfirm',mode:'sellPnr'}
					}).done(function(data) {
						console.log('confirm success',data);
						notificationSuccess(data);
					}).error(function(data) {
						data = eval(data).responseText;

						var errorMsg = eval(JSON.parse(data));
						if (errorMsg.code > 424) {
							console.log('msg', errorMsg.message);
							$('#pickSeatsData').append(errorMsg.message);
							$('#pickSeatsData').addClass('text-danger');
							$('#continueSelling').removeClass('collapse.in');
							$('#continueSelling').addClass('collapse');
						} else {
							$.get("jsp/SignInConfirmation.jsp", function(msg) {
								$('#actionPage').html(msg);
							});
							sellPnr.status = 0;
						}
					});
				} else {
					sellPnr.notify = [];
				}
			}
		}
	}
	else if(id== "delSharedPnr")
	{
		console.log('and its shared pnr is ',event.id);
		var ajaxData={};
		ajaxData.pnrNumber=event.id;
		ajaxData.mode='sellPnr';
		ajaxData.action='delete';
		
		$.ajax({
			url : 'auth/sellPnr.do',
			type : 'post',
			data : ajaxData
		}).done(function(data) {
			$('#actionPage').html(data);
		}).error(function(data) {
			data = eval(data).responseText;
			var errorMsg = eval(JSON.parse(data));
			 
				$.get("jsp/SignInConfirmation.jsp", function(msg) {
					$('#actionPage').html(msg);
				});
				sellPnr.status = 0;
		});
		
	}
	else if(id == "sellFromBuy")
	{
		// this yet needs to be coded
		if(userAccount)
		{
			console.log('sellfrombuy');
			var json = {};
			var isSellable = false;
			
			json.pnrNumber = $('#pnrNumber').val();
			json.mode='pnr';
			json.action='view';
			
			$.get("viewPnr.do",json,function(msg){
				$('#actionResult').html(msg);
				isSellable = $("#isSellable").text();

				if(userAccount && isSellable)
				{
					$("#sellThis").removeClass('collapse');
					$("#sellThis").addClass('collapse.in');
				}
			});  
		}
		else
		{
			$.get("jsp/SignInConfirmation.jsp",function(msg){
				$('#actionPage').html(msg);
			}); 
		}
	}
}
function hideDiv(id)
{
	id.hide();
//	id.addClass('collapse');
//	id.removeClass('collapse.in');
}

function showDiv(id)
{
	id.show();
//	id.addClass('collapse.in');
//	id.removeClass('collapse');
}

function goBackToPickSeats()
{
	console.log('going to pick seats');
	sellPnrStatusSuccess($('#pickSeatsData').html());
//	sellPnr.status=2;
}

function startSellingProcess(data)
{
	$("#sellErrorMsg").addClass('collapse');
	$("#sellErrorMsg").removeClass('collapse.in');
	
	$("#actionResult").addClass('text-success');
	
	$("#sellPnrList").addClass("collapse");
	$("#enterPnrBox").addClass("collapse");

	$("#continueSelling").addClass('collapse.in');
	$("#continueSelling").removeClass('collapse');
	$("#continueSelling").addClass('pull-right');
	$("#continueSelling").html('continue');
	
	$(".active").removeClass('active');
	$('#checkStatusCrumb').addClass('active');
	
	
	showDiv($('#actionResult'));
	hideDiv($('#pickSeatsData'));
	hideDiv($('#notificationData'));
	hideDiv($('#confirmationData'));
	$("#breadCrumb").show();
	$("#checkStatusCrumb").attr('onclick','startSellingProcess()');
	
	sellPnr.status=1;	
}

function sellPnrStatusSuccess(data)
{
	$("#sellErrorMsg").addClass('collapse');
	$("#sellErrorMsg").removeClass('collapse.in');
	
	$('#pickSeatsData').html(data);

	$(".active").removeClass('active');
	$('#pickSeatsCrumb').addClass('active');
	
	$("#continueSelling").addClass('collapse.in');
	$("#continueSelling").addClass('pull-right');
	$("#continueSelling").removeClass('collapse');
	$("#continueSelling").html('continue');
	
	$("#sellThis").removeClass('collapse.in');
	$("#sellThis").addClass('collapse');

	hideDiv($('#actionResult'));
	showDiv($('#pickSeatsData'));
	hideDiv($('#notificationData'));
	hideDiv($('#confirmationData'));
	$("#breadCrumb").show();
	$("#pickSeatsCrumb").attr('onclick','sellPnrStatusSuccess($("#pickSeatsData").html())');
	
	sellPnr.status=2;
}

function pickSeatsSuccess(data)
{
	$("#sellErrorMsg").addClass('collapse');
	$("#sellErrorMsg").removeClass('collapse.in');
	
	$('#notificationData').html(data);

	$(".active").removeClass('active');
	$('#notificationCrumb').addClass('active');
	
	$("#continueSelling").addClass('collapse.in');
	$("#continueSelling").removeClass('collapse');
	$("#continueSelling").removeClass('pull-right');
	$("#continueSelling").html('Confirm Order');
	
	$('#pickSeatsData').addClass('collapse');
	$('#pickSeatsData').removeClass('collapse.in');

	hideDiv($('#actionResult'));
	hideDiv($('#pickSeatsData'));
	showDiv($('#notificationData'));
	hideDiv($('#confirmationData'));
	
	$("#notificationCrumb").attr('onclick','pickSeatsSuccess($("#notificationData").html())');
	
	sellPnr.status=3;
}

function notificationSuccess(data)
{
	$("#sellErrorMsg").addClass('collapse');
	$("#sellErrorMsg").removeClass('collapse.in');
	
	$('#confirmationData').html(data);

	$(".active").removeClass('active');
	$('#confirmationCrumb').addClass('active');

	$("#continueSelling").removeClass('collapse.in');
	$("#continueSelling").addClass('collapse');

	$("#sellThis").removeClass('collapse.in');
	$("#sellThis").addClass('collapse');

	hideDiv($('#actionResult'));
	hideDiv($('#pickSeatsData'));
	hideDiv($('#notificationData'));
	showDiv($('#confirmationData'));
	
	$("#checkStatusCrumb").attr('onclick','return 0');
	$("#pickSeatsCrumb").attr('onclick','return 0');
//	$("#notificationCrumb").attr('onclick','javascript:void()');
	$("#confirmationCrumb").attr('onclick','return 0');
	sellPnr.passengers=null;
	sellPnr.status=4;
}

function buyTrainTkt(eventName)
{
	if (userAccount) {
		if (eventName == "buyTrainTktNavButton") {
			console.log('buyTrainTktNavButton succ');
			var json = {};
			json.mode = 'buyPnr';
			json.action = 'view';

			$.ajax({
				url : 'auth/buyPnr.do',
				type : 'get',
				data : json
			}).done(function(data) {
				$('#actionPage').html(data);
			}).error(function(data) {
				console.log('you are not signed up ', data);
				$.get("jsp/SignInConfirmation.jsp", function(msg) {
					$('#actionPage').html(msg);
				});
			});
		} else if (eventName == "getStationInfo") {

			if (!stationInfo) {
				var json = {};
				json.mode = 'buyPnr';
				json.action = 'stationInfo';
				$.ajax({
					url : 'auth/buyPnr.do',
					type : 'get',
					data : json
				}).done(function(data) {
					console.log(data);
					stationInfo = data;
				}).error(function(data) {
					console.log('you are not signed up ', data);
					$.get("jsp/SignInConfirmation.jsp", function(msg) {
						$('#actionPage').html(msg);
					});
				});
			}
//			 $( "#suggestStation" ).autocomplete({
//			      source: stationInfo
//			 });
		} else if (eventName == "buyPnrSearch") {
			var json = {};
			json.mode = 'buyPnr';
			json.action = 'searchTrain';
			json.fromStz= $("#fromstz").val();
			json.toStz= $("#tostz").val();
			json.doj = $("#datepicker").value;
			$.ajax({
				url : 'auth/buyPnr.do',
				type : 'get',
				data : json
			}).done(function(data) {
				console.log('buypnr result ',data);
			}).error(function(data) {
				 console.log('error occurd budy');
			});
		}

	} else {
		console.log('you are not signed up');
		$.get("jsp/SignInConfirmation.jsp", function(msg) {
			$('#actionPage').html(msg);
		});
	}
}

function entertainmentPage(idName, element) {
	if (idName == "wantNow") {
		$.ajax({
			url : 'optauth/entertainment.do',
			type : 'post',
			data : {
				like : true,
				action : 'upvote',
				mode : 'entertainment'
			}
		}).done(function(data) {
			console.log('confirm success', data);
//			$('#entertainmentActionPage').append('<br/> <div class="text-danger"> This ticket is not sellable </div>');
			
			if(userAccount)
			{
				$('#entertainmentActionPage').html('Thanks for your support!! Even we are working for the same :) We will ping you once it is ready');
				if(userAccount.NAME)
				{
					$('#entertainmentActionPage').prepend('Hey ',userAccount.NAME + "!");
				}
			}
			else
			{
				$('#entertainmentActionPage').html('Thanks! Even we are working for the same :) Had we known who you are, we would have notified you through Email');
			}
		}).error(function(data) {

		})
	}
}


function wayToShare(thisElement, confirmation)
{
	if(confirmation)
	{
		var ajaxData={};
		ajaxData.smtMediate=thisElement.value=='option2';
		ajaxData.mode='sellPnr';
		ajaxData.action='smtMediate';
		ajaxData.pnrNumber=sellPnr.pnrNumber;
		sellPnr.smtMediate=ajaxData.smtMediate;
	}
	else
	{
		$("#option1Radio").attr('checked',false);
		$("#option2Radio").attr('checked',false);
	}
}


function getJsonArrayOfSelectedCheckbox(checkName)
{
	var valuesArr = {};
	var totalChecked = $('input[name=' + checkName + ']:checked').length;

	var j = 0;
	for (var i = totalChecked; i > 0; i--) {
//		valuesArr.push($('input[name=' + checkName + ']:checked')[i - 1].value);
		valuesArr[j++] = $('input[name=' + checkName + ']:checked')[i - 1].value;
		$('input[name=' + checkName + ']:checked').attr('checked',true);
	}
	return valuesArr;
}

function travelEvents(event)
{
	var $this = $(this);
	var $event = event;
	var $id = $event.target.id;
}
