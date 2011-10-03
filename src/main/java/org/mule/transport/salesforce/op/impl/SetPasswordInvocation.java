/*
 * $Id: SetPasswordInvocation.java 1036 2010-11-24 08:47:10Z jcalleja $
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

import com.sforce.soap.partner.SetPasswordResult;
import com.sforce.soap.partner.SoapBindingStub;

/**
 * SetPasswordInvocation requires a MuleMessage with a payload which contains a
 * String (userId), a String (password) and a String (sessionId) as the first,
 * second, and third parameter respectively.
 */
public class SetPasswordInvocation extends AbstractInvocation
{

    public SetPasswordInvocation(GenericKeyedObjectPool soapBindingPool, Object[] invocationParameters)
    {
        super(soapBindingPool, invocationParameters);
    }

    @Override
    public Object invoke() throws Exception
    {

        String userId = (String) invocationParameters[0];
        String password = (String) invocationParameters[1];
        String sessionId = (String) invocationParameters[2];
        SetPasswordResult result;

        if (SessionIdUtil.isAppended(sessionId)) sessionId = SessionIdUtil.unappendedSessionId(sessionId);

        SalesforceSoapBindingKey key = new SalesforceSoapBindingKey(sessionId);
        SoapBindingStub sfdc = (SoapBindingStub) soapBindingPool.borrowObject(key);

        try
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("Before executing setPassword: userId = " + userId + ", password = " + password
                             + ", SessionId = " + sessionId);
            }

            result = sfdc.setPassword(userId, password);

            if (logger.isDebugEnabled())
            {
                logger.debug("After executing setPassword: userId = " + userId + ", password = " + password
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
