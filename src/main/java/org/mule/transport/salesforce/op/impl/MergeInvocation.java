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
import org.mule.transport.salesforce.SalesforceSoapBindingKey;
import org.mule.transport.salesforce.op.base.AbstractInvocation;
import org.mule.transport.salesforce.op.util.SessionIdUtil;

import com.sforce.soap.partner.MergeRequest;
import com.sforce.soap.partner.MergeResult;
import com.sforce.soap.partner.SoapBindingStub;

/**
 * MergeInvocation requires a MuleMessage with a payload which contains a
 * MergeRequest[] and a String (i.e. the sessionId) as the first and second parameter
 * respectively.
 */
public class MergeInvocation extends AbstractInvocation
{
    public MergeInvocation(GenericKeyedObjectPool soapBindingPool, Object[] invocationParameters)
    {
        super(soapBindingPool, invocationParameters);
    }

    @Override
    public Object invoke() throws Exception
    {
        MergeRequest[] request = (MergeRequest[]) invocationParameters[0];
        String sessionId = (String) invocationParameters[1];
        MergeResult[] result;

        if (SessionIdUtil.isAppended(sessionId)) sessionId = SessionIdUtil.unappendedSessionId(sessionId);

        SalesforceSoapBindingKey key = new SalesforceSoapBindingKey(sessionId);
        SoapBindingStub sfdc = (SoapBindingStub) soapBindingPool.borrowObject(key);

        try
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("Before executing merge: SessionId = " + sessionId);
            }

            result = sfdc.merge(request);

            if (logger.isDebugEnabled())
            {
                logger.debug("After executing merge: SessionId = " + sessionId);
            }
        }
        finally
        {

            soapBindingPool.returnObject(key, sfdc);
        }

        return result;

    }

}
