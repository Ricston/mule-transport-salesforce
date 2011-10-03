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
import org.mule.transport.salesforce.op.util.PrintUtil;
import org.mule.transport.salesforce.op.util.SessionIdUtil;
import org.mule.transport.salesforce.op.util.TransformUtil;

import com.sforce.soap.partner.DescribeLayoutResult;
import com.sforce.soap.partner.SoapBindingStub;
import com.sforce.soap.partner.sobject.SObject;

/**
 * DescribeLayoutInvocation requires a MuleMessage with a payload which contains a
 * String, a String[], and a String (i.e. the sessionId) as the first, second, and
 * third parameter respectively.
 */
public class DescribeLayoutInvocation extends AbstractInvocation
{

    public DescribeLayoutInvocation(GenericKeyedObjectPool soapBindingPool, Object[] invocationParameters)
    {
        super(soapBindingPool, invocationParameters);
    }

    @Override
    public Object invoke() throws Exception
    {
        String sObjectType = (String) invocationParameters[0];

        String[] recordTypeIds = null;
        if (invocationParameters[1] instanceof SObject[])
        {
            recordTypeIds = TransformUtil.getIds(invocationParameters[1]);
        }
        else if (invocationParameters[1] instanceof String[])
        {
            recordTypeIds = (String[]) invocationParameters[1];
        }

        String sessionId = (String) invocationParameters[2];
        DescribeLayoutResult result;

        if (SessionIdUtil.isAppended(sessionId)) sessionId = SessionIdUtil.unappendedSessionId(sessionId);

        SalesforceSoapBindingKey key = new SalesforceSoapBindingKey(sessionId);
        SoapBindingStub sfdc = (SoapBindingStub) soapBindingPool.borrowObject(key);

        try
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("Before executing describeLayout: sObjectType = " + sObjectType
                             + ", recordTypeIds = " + PrintUtil.printStrArray(recordTypeIds)
                             + ", SessionId = " + sessionId);
            }

            result = sfdc.describeLayout(sObjectType, recordTypeIds);

            if (logger.isDebugEnabled())
            {
                logger.debug("After executing describeLayout: sObjectType = " + sObjectType
                             + ", recordTypeIds = " + PrintUtil.printStrArray(recordTypeIds)
                             + ", SessionId = " + sessionId);
            }
        }

        finally
        {
            soapBindingPool.returnObject(key, sfdc);

        }

        return result;

    }

}
