/*
 * $Id: RetrieveInvocation.java 1036 2010-11-24 08:47:10Z jcalleja $
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

import com.sforce.soap.partner.SoapBindingStub;
import com.sforce.soap.partner.sobject.SObject;

/**
 * RetrieveInvocation requires a MuleMessage with a payload which contains a String
 * (fieldList), String (sObjectType), String[] (ids), and a String (sessionId) as the
 * first, second, third, and fourth parameter respectively.
 */
public class RetrieveInvocation extends AbstractInvocation
{

    public RetrieveInvocation(GenericKeyedObjectPool soapBindingPool, Object[] invocationParameters)
    {
        super(soapBindingPool, invocationParameters);
    }

    @Override
    public Object invoke() throws Exception
    {
        String fieldList = (String) invocationParameters[0];
        String sObjectType = (String) invocationParameters[1];
        String[] ids = (String[]) invocationParameters[2];
        String sessionId = (String) invocationParameters[3];
        SObject[] result;

        if (SessionIdUtil.isAppended(sessionId)) sessionId = SessionIdUtil.unappendedSessionId(sessionId);

        SalesforceSoapBindingKey key = new SalesforceSoapBindingKey(sessionId);
        SoapBindingStub sfdc = (SoapBindingStub) soapBindingPool.borrowObject(key);

        try
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("Before executing retrieve: Field List = " + fieldList + ", sObjectType = "
                             + sObjectType + ", ids = " + PrintUtil.printStrArray(ids) + ", SessionId = "
                             + sessionId);
            }

            result = sfdc.retrieve(fieldList, sObjectType, ids);

            if (logger.isDebugEnabled())
            {
                logger.debug("After executing retrieve: sessionId = " + sessionId);
            }
        }
        finally
        {
            soapBindingPool.returnObject(key, sfdc);
        }

        return result;

    }

}
