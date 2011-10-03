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
import org.mule.transport.salesforce.op.util.TransformUtil;

import com.sforce.soap.partner.DeleteResult;
import com.sforce.soap.partner.SoapBindingStub;
import com.sforce.soap.partner.sobject.SObject;

/**
 * DeleteInvocation requires a MuleMessage with a payload which contains a String[]
 * and a String (i.e. the sessionId) as the first and second parameter respectively.
 */
public class DeleteInvocation extends AbstractInvocation
{
    public DeleteInvocation(GenericKeyedObjectPool soapBindingPool, Object[] invocationParameters)
    {
        super(soapBindingPool, invocationParameters);
    }

    @Override
    public Object invoke() throws Exception
    {
        String[] ids = null;
        if (invocationParameters[0] instanceof SObject[])
            ids = TransformUtil.getIds(invocationParameters[0]);
        else
            ids = (String[]) invocationParameters[0];

        String sessionId = (String) invocationParameters[1];
        DeleteResult[] deleteResult;

        if (SessionIdUtil.isAppended(sessionId)) sessionId = SessionIdUtil.unappendedSessionId(sessionId);

        SalesforceSoapBindingKey key = new SalesforceSoapBindingKey(sessionId);
        SoapBindingStub sfdc = (SoapBindingStub) soapBindingPool.borrowObject(key);

        try
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("After executing delete: SessionId = " + sessionId);
            }

            deleteResult = sfdc.delete(ids);

            if (logger.isDebugEnabled())
            {
                logger.debug("After executing delete: SessionId = " + sessionId);
            }
        }
        finally
        {
            soapBindingPool.returnObject(key, sfdc);
        }

        return deleteResult;

    }

}
