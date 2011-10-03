/*
 * $Id: GetDeletedInvocation.java 1036 2010-11-24 08:47:10Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.op.impl;

import java.util.Calendar;

import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.mule.transport.salesforce.SalesforceSoapBindingKey;
import org.mule.transport.salesforce.op.base.AbstractInvocation;
import org.mule.transport.salesforce.op.util.PrintUtil;
import org.mule.transport.salesforce.op.util.SessionIdUtil;

import com.sforce.soap.partner.GetDeletedResult;
import com.sforce.soap.partner.SoapBindingStub;

/**
 * GetDeletedInvocation requires a MuleMessage with a payload which contains a
 * String, Calendar, Calendar, and a String (i.e. the sessionId) as the first,
 * second, third, and fourth parameter respectively.
 */
public class GetDeletedInvocation extends AbstractInvocation
{

    public GetDeletedInvocation(GenericKeyedObjectPool soapBindingPool, Object[] invocationParameters)
    {
        super(soapBindingPool, invocationParameters);
    }

    @Override
    public Object invoke() throws Exception
    {

        String sObjectType = (String) invocationParameters[0];
        Calendar startDate = (Calendar) invocationParameters[1];
        Calendar endDate = (Calendar) invocationParameters[2];
        String sessionId = (String) invocationParameters[3];
        GetDeletedResult result;

        if (SessionIdUtil.isAppended(sessionId)) sessionId = SessionIdUtil.unappendedSessionId(sessionId);

        SalesforceSoapBindingKey key = new SalesforceSoapBindingKey(sessionId);
        SoapBindingStub sfdc = (SoapBindingStub) soapBindingPool.borrowObject(key);

        try
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("Before executing getDeleted: sObjectType = " + sObjectType + ", startDate = "
                             + PrintUtil.printCalendar(startDate) + ", endDate = "
                             + PrintUtil.printCalendar(endDate) + ", SessionId = " + sessionId);
            }

            result = sfdc.getDeleted(sObjectType, startDate, endDate);

            if (logger.isDebugEnabled())
            {
                logger.debug("After executing getDeleted: sObjectType = " + sObjectType + ", SessionId = "
                             + sessionId);
            }
        }
        finally
        {
            soapBindingPool.returnObject(key, sfdc);
        }

        return result;

    }

}
