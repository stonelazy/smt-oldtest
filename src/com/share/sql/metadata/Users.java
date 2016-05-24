//$Id$
/**
 * 
 */

package com.share.sql.metadata;

/**
 * @author sudharsan-2598
 *
 *
 *
CREATE TABLE Users(
	USERS_ID BIGSERIAL PRIMARY KEY,
	NAME text,
	EMAIL text,
	POSTAL text,
	MOBILE BIGINT,
	SEX smallint,
	COOKIE UUID DEFAULT gen_random_uuid(),
	PASSWORD bytea
);
alter sequence users_users_id_seq start 12000 cache 25;

 */
public class Users
{
	public static final String TABLE_NAME = "Users";
	public static final String USERS_ID = "USERS_ID";
	public static final String NAME = "NAME";
	public static final String EMAIL = "EMAIL";
	public static final String POSTAL = "POSTAL";
	public static final String MOBILE = "MOBILE";
	public static final String SEX = "SEX";
	public static final String COOKIE = "COOKIE";
	public static final String PASSWORD = "PASSWORD";
	public static final String AGE = "AGE";
}
