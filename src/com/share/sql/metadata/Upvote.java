//$Id$
/**
 * 
 */
package com.share.sql.metadata;

/**
 * @author sudharsan-2598
 *
 */
public class Upvote
{
	/*
	CREATE TABLE Upvote(
	UPVOTE_ID BIGSERIAL PRIMARY KEY,
	USERS_ID BIGINT
);
alter sequence upvote_upvote_id_seq start 120000 cache 1;
			*/
	
	public static final String TABLE_NAME="Upvote";
	public static final String UPVOTE_ID= "UPVOTE_ID";
	public static final String USERS_ID= Users.USERS_ID;
}
