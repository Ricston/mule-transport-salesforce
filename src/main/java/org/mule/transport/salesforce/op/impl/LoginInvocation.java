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

import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.SessionHeader;
import com.sforce.soap.partner.SforceServiceLocator;
import com.sforce.soap.partner.SoapBindingStub;
import com.sforce.soap.partner.fault.LoginFault;

/**
 * LoginInvocation requires a MuleMessage with a payload which contains a String
 * (username) and a String (password) as the first and second parameter respectively.
 */
public class LoginInvocation extends AbstractInvocation
{

    public LoginInvocation(GenericKeyedObjectPool soapBindingPool, Object[] invocationParameters)
    {
        super(soapBindingPool, invocationParameters);
    }

    @Override
    public Object invoke() throws Exception
    {

        try
        {
            String username = (String) invocationParameters[0];
            String password = (String) invocationParameters[1];
            SoapBindingStub sfdc = (SoapBindingStub) new SforceServiceLocator().getSoap();
            LoginResult loginResult = sfdc.login(username, password);
            String sessionId = loginResult.getSessionId();

            sfdc._setProperty(SoapBindingStub.ENDPOINT_ADDRESS_PROPERTY, loginResult.getServerUrl());

            SessionHeader sh = new SessionHeader();
            sh.setSessionId(sessionId);

            if (logger.isDebugEnabled())
            {
                logger.debug("Successfully logged on to Salesforce. Session Id is " + sessionId);
            }

            // Set the session header for subsequent call authentication
            sfdc.setHeader(new SforceServiceLocator().getServiceName().getNamespaceURI(), "SessionHeader", sh);

            SalesforceSoapBindingKey key = new SalesforceSoapBindingKey(sessionId, sfdc);
            soapBindingPool.borrowObject(key);
            soapBindingPool.returnObject(key, sfdc);

            return SalesforceProperties.SALESFORCE_SUCCESSFULL_LOGIN + sessionId;

        }
        catch (LoginFault loginFault)
        {
            return loginFault.getFaultString();
        }

    }
}
