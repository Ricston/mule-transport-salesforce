/*
 * $Id: DescribeGlobalInvocation.java 1036 2010-11-24 08:47:10Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.op.impl;

import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.mule.transport.salesforce.SalesforceSoapBindingKey;
import org.mule.transport.salesforce.op.base.AbstractInvocation;
import org.mule.transport.salesforce.op.util.SessionIdUtil;

import com.sforce.soap.partner.DescribeGlobalResult;
import com.sforce.soap.partner.SoapBindingStub;

/**
 * DescribeGlobalInvocation requires a MuleMessage with a payload which contains a
 * String (i.e. the sessionId).
 */
public class DescribeGlobalInvocation extends AbstractInvocation
{

    public DescribeGlobalInvocation(GenericKeyedObjectPool soapBindingPool, Object[] invocationParameters)
    {
        super(soapBindingPool, invocationParameters);
    }

    @Override
    public Object invoke() throws Exception
    {
        String sessionId = (String) invocationParameters[0];
        DescribeGlobalResult result;

        if (SessionIdUtil.isAppended(sessionId)) sessionId = SessionIdUtil.unappendedSessionId(sessionId);

        SalesforceSoapBindingKey key = new SalesforceSoapBindingKey(sessionId);
        SoapBindingStub sfdc = (SoapBindingStub) soapBindingPool.borrowObject(key);

        try
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("Before executing describeGlobal: SessionId = " + sessionId);
            }

            result = sfdc.describeGlobal();

            if (logger.isDebugEnabled())
            {
                logger.debug("After executing describeGlobal: SessionId = " + sessionId);
            }
        }
        finally
        {
            soapBindingPool.returnObject(key, sfdc);
        }

        return result;
    }

}
