//$Id$
/**
 * 
 */
package com.server.constants;

import java.util.regex.Pattern;

/**
 * @author sudharsan-2598
 *
 */
public class RegexConstants
{
	 // This pattern is to validate the emailID and not to match an emailid - only emailID and not both emailid and username
    public static final Pattern VALID_EMAIL_PATTERN  = Pattern.compile("([A-Za-z0-9]([\\.\\-\\_\\+]?[A-Za-z0-9]){0,63}([\\+]([A-Za-z0-9\\+][\\.]?[A-Za-z0-9]){0,64})?@([A-Za-z0-9]([\\-]?[A-Za-z0-9]){0,63}[\\.]){1,2}[A-Za-z0-9]{1,64})");
	public static final Pattern INVALID_TICKET_PATTERN = Pattern.compile("(?i)(CAN)|(MOD)|(CAN\\/MOD)|(WL)|(GNWL)|(RLWL)|(PQWL)|(REGRET)|(Released)");
	public static final Pattern MOBILE  = Pattern.compile("[0-9]{8,15}");
	
}
