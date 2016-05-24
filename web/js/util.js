function isValid(str)
{
	return !str =="";
}

function IsJson(str) 
{
	try {
		JSON.parse(str);
	} catch (e) {
		return false;
	}
	return true;
}

function showDiv(id)
{
	$(id).show();
}