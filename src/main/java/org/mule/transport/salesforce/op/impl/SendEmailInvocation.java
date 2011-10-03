/*
 * $Id: SendEmailInvocation.java 1036 2010-11-24 08:47:10Z jcalleja $
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

import com.sforce.soap.partner.Email;
import com.sforce.soap.partner.SendEmailResult;
import com.sforce.soap.partner.SoapBindingStub;

/**
 * SendEmailInvocation requires a MuleMessage with a payload which contains a Email[]
 * and a String (i.e. the sessionId) as the first and second parameter respectively.
 */
public class SendEmailInvocation extends AbstractInvocation
{

    public SendEmailInvocation(GenericKeyedObjectPool soapBindingPool, Object[] invocationParameters)
    {
        super(soapBindingPool, invocationParameters);
    }

    @Override
    public Object invoke() throws Exception
    {

        Email[] messages = (Email[]) invocationParameters[0];
        String sessionId = (String) invocationParameters[1];
        SendEmailResult[] result;

        if (SessionIdUtil.isAppended(sessionId)) sessionId = SessionIdUtil.unappendedSessionId(sessionId);

        SalesforceSoapBindingKey key = new SalesforceSoapBindingKey(sessionId);
        SoapBindingStub sfdc = (SoapBindingStub) soapBindingPool.borrowObject(key);

        try
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("Before executing sendEmail: sessionId = " + sessionId);
            }

            result = sfdc.sendEmail(messages);

            if (logger.isDebugEnabled())
            {
                logger.debug("Before executing sendEmail: sessionId = " + sessionId);
            }
        }
        finally
        {
            soapBindingPool.returnObject(key, sfdc);
        }

        return result;
    }

}
