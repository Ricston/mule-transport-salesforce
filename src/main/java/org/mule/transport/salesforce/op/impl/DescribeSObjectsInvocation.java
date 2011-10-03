/*
 * $Id: DescribeSObjectsInvocation.java 1036 2010-11-24 08:47:10Z jcalleja $
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
import org.mule.transport.salesforce.op.util.PrintUtil;
import org.mule.transport.salesforce.op.util.SessionIdUtil;

import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.SoapBindingStub;

/**
 * DescribeSObjectsInvocation requires a MuleMessage with a payload which contains a
 * String[] and a String (i.e. the sessionId) as the first and second parameter
 * respectively.
 */
public class DescribeSObjectsInvocation extends AbstractInvocation
{

    public DescribeSObjectsInvocation(GenericKeyedObjectPool soapBindingPool, Object[] invocationParameters)
    {
        super(soapBindingPool, invocationParameters);

    }

    @Override
    public Object invoke() throws Exception
    {
        String[] sObjectType = (String[]) invocationParameters[0];
        String sessionId = (String) invocationParameters[1];
        DescribeSObjectResult[] result;

        SalesforceSoapBindingKey key = new SalesforceSoapBindingKey(sessionId);
        SoapBindingStub sfdc = (SoapBindingStub) soapBindingPool.borrowObject(key);

        if (SessionIdUtil.isAppended(sessionId)) sessionId = SessionIdUtil.unappendedSessionId(sessionId);

        try
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("Before executing describeSObjects: sObjectType = "
                             + PrintUtil.printStrArray(sObjectType) + ", SessionId = " + sessionId);
            }

            result = sfdc.describeSObjects(sObjectType);

            if (logger.isDebugEnabled())
            {
                logger.debug("After executing describeSObjects: sObjectType = "
                             + PrintUtil.printStrArray(sObjectType) + ", SessionId = " + sessionId);
            }
        }
        finally
        {
            soapBindingPool.returnObject(key, sfdc);
        }

        return result;

    }

}
