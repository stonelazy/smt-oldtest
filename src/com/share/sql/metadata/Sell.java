//$Id$
/**
 * 
 */
package com.share.sql.metadata;

/**
 * @author sudharsan-2598
 *
 */
public class Sell
{

/*
CREATE TABLE Sell(
	PNR_NUMBER BIGINT PRIMARY KEY,
	TRAIN_NUMBER BIGINT,
	FROM_STZ character(4),
	TO_STZ character(4),
	DOJ BIGINT,
	EXTRA_PASSENGER smallint DEFAULT 0,
	USERS_ID BIGINT,
	NOTIFY smallint DEFAULT 0,
	CRITERIA smallint DEFAULT 0,
	MEDIATE smallint DEFAULT 0,
	STATUS smallint DEFAULT 0,
	REQUEST_TIME BIGINT
);
	 
*/
	public static final String TABLE_NAME="Sell";
	public static final String SELL_ID= "SELL_ID";
	public static final String USERS_ID= Users.USERS_ID;
	public static final String DOJ= "DOJ";
	public static final String TRAIN_NUMBER= "TRAIN_NUMBER";
	public static final String PNR= "PNR_NUMBER";
	public static final String FROM_STZ= "FROM_STZ";
	public static final String TO_STZ= "TO_STZ";
	public static final String EXTRA_PASSENGER= "EXTRA_PASSENGER";
	public static final String NOTIFY= "NOTIFY";
	public static final String CRITERIA= "CRITERIA";
	public static final String MEDIATE= "MEDIATE";
	public static final String STATUS= "STATUS";
	public static final String REQUEST_TIME= "REQUEST_TIME";

	public class SellConstants
	{
		private boolean isMobile;
		private boolean isEmail;
		private boolean isPostal;
		private int notifyStatus;
		
		/*
		 	NOTIFY STATUS
					Postal | Mobile | Email
						0       0       0		-	0
						0       0       1		-	1
						0		1		0		-	2
						0		1		1		-	3
						1		0		0		- 	4
						1		0		1		-  	5
						1		1		0		-	6
						1		1		1		-	7
						
			MEDIATE
			 0 - share the contact info
			 1 - do the mama velai
			 
			 STATUS
			0 - Not sold
			1 - sold
			2 - expired
		 */
	
		public int getNotifyStatus()
		{
			if(notifyStatus == 0)
			{
				calculateNotifyStatus();
			}
			return notifyStatus;
		}
		
		private void calculateNotifyStatus()
		{
			int calculateStatus=0;
			calculateStatus += isEmail ? 1:0;
			calculateStatus += isMobile ? 2:0;
			calculateStatus += isPostal ? 3:0;
			this.notifyStatus=calculateStatus;
		}

		public boolean isMobile()
		{
			return isMobile;
		}

		public void setMobile(boolean isMobile)
		{
			this.isMobile = isMobile;
		}

		public boolean isEmail()
		{
			return isEmail;
		}

		public void setEmail(boolean isEmail)
		{
			this.isEmail = isEmail;
		}

		public boolean isPostal()
		{
			return isPostal;
		}

		public void setPostal(boolean isPostal)
		{
			this.isPostal = isPostal;
		}

		public boolean isMobile(boolean isMobile)
		{
			return this.isMobile;
		}
		
		
		
	}
	
}