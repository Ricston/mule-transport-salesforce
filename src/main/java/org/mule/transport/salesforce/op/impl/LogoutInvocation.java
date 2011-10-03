/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.op.impl;

import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.mule.transport.salesforce.SalesforceProperties;
import org.mule.transport.salesforce.SalesforceSoapBindingKey;
import org.mule.transport.salesforce.op.base.AbstractInvocation;
import org.mule.transport.salesforce.op.util.SessionIdUtil;

import com.sforce.soap.partner.SoapBindingStub;

/**
 * LogoutInvocation requires the sessionId as a payload in the MuleMessage parameter
 * in its constructor.
 */
public class LogoutInvocation extends AbstractInvocation
{
    public LogoutInvocation(GenericKeyedObjectPool soapBindingPool, Object[] invocationParameters)
    {
        super(soapBindingPool, invocationParameters);
    }

    @Override
    public Object invoke() throws Exception
    {

        String sessionId = (String) invocationParameters[0];

        if (SessionIdUtil.isAppended(sessionId)) sessionId = SessionIdUtil.unappendedSessionId(sessionId);

        SalesforceSoapBindingKey key = new SalesforceSoapBindingKey(sessionId);

        SoapBindingStub sfdc = (SoapBindingStub) soapBindingPool.borrowObject(key);
        key.setSfdc(sfdc);

        try
        {
            sfdc.logout();

            if (logger.isDebugEnabled())
            {
                logger.debug("Successfully logged out of Salesforce. The Session Id was: " + sessionId);
            }
        }
        finally
        {
            soapBindingPool.clear(key);
        }

        return SalesforceProperties.SALESFORCE_SUCCESSFULL_LOGOUT + sessionId;
    }
}
