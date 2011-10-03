/*
 * $Id: SessionIdUtil.java 1036 2010-11-24 08:47:10Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.op.util;

import org.mule.transport.salesforce.SalesforceProperties;

public class SessionIdUtil
{

    public static boolean isAppended(String sessionId)
    {
        if (sessionId.startsWith(SalesforceProperties.SALESFORCE_SUCCESSFULL_LOGIN)) return true;

        return false;
    }

    public static String unappendedSessionId(String sessionId)
    {
        return sessionId.substring(SalesforceProperties.SALESFORCE_SUCCESSFULL_LOGIN.length());
    }

}
