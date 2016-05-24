//$Id$
package com.share.sql.query;

import java.sql.SQLException;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public interface UserDetailsQuery
{
	JSONObject getUserDetails(String emailAddress) throws JSONException, SQLException;
	JSONObject getUserDetails(UUID cookie)throws JSONException, SQLException;
}
