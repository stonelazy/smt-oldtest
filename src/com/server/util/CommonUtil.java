//$Id$
/**
 *
 */

package com.server.util;

import com.server.constants.GeneralConstants;
import org.apache.commons.io.Charsets;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author sudharsan-2598
 */
public class CommonUtil {
    private static Logger logger = Logger.getLogger(CommonUtil.class.getName());

    public static boolean isValid(Object test) {
        return test != null && !test.equals("null") && !test.equals("");
    }

    public static void writeResponse(Object errorResponse) {
        logger.config("response written in the object>> " + errorResponse);

        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding(Charsets.UTF_8.displayName());

        try (PrintWriter out = response.getWriter()) {
            out.println(errorResponse.toString());
        } catch (Exception e) {
            if (errorResponse == null || errorResponse.equals("")) {
                String content = "com.zoho.transmail.util.TransmailUtil.writeErrorResponse(Object) has been called with null value.. ";// No I18N
                content += " for iamuser>> this exception is handled for now.. sent a default 500 response";// No I18N
                // DebugMail sent to ensure it is not looping.. dont delete this - sudharsan
//				writeErrorResponse(MessageConstants.getResponseForErrorCode(500));
                writeResponse("response object is null");
            } else {
                logger.warning("Exception while sending a response back to customer");
            }
            logger.log(Level.WARNING, "Exception Occured", e);
        }
    }

    public static String getAuthCookieFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (int i = cookies.length - 1; i >= 0; i--) {
                if (cookies[i].getName().equals(GeneralConstants.AUTH_COOKIE_NAME)) {
                    return cookies[i].getValue();
                }
                logger.finer("cookie >> [" + i + "] with name>> " + cookies[i].getName() + " &value>> " + cookies[i].getValue()); // No i18N
            }
        }
        return null;
    }

}
