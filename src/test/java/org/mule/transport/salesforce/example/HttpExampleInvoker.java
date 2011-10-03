/*
 * $Id: HttpExampleInvoker.java 956 2010-09-09 11:52:25Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.example;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.mule.transport.salesforce.SalesforceProperties;

import com.sforce.soap.partner.sobject.SObject;

import java.io.IOException;

/**
 * onCall this component simply transforms the message so that it can call its action
 * method with the relevant pieces of information (viz username, password, lastname
 * of contact of interest). The action method makes use of the IHttpExample interface
 * which binds this component to the other 2 services defined in http-example.xml
 * (viz LoginService and QueryService), and so, this component has access to these
 * services.
 */
public class HttpExampleInvoker implements Callable
{

    private IHttpExample iHttpExample;

    public SObject[] action(String[] parsed) throws IOException
    {
        String temp = iHttpExample.login(parsed[0], parsed[1]);

        String sessionId = "";

        if (temp.startsWith(SalesforceProperties.SALESFORCE_SUCCESSFULL_LOGIN))
            sessionId = temp.substring(SalesforceProperties.SALESFORCE_SUCCESSFULL_LOGIN.length());

        return iHttpExample.query(parsed[2], sessionId);

    }

    public void setIHttpExample(IHttpExample iHttpExample)
    {
        this.iHttpExample = iHttpExample;
    }

    public IHttpExample getIHttpExample()
    {
        return iHttpExample;
    }

    @Override
    public Object onCall(MuleEventContext eventContext) throws Exception
    {
        String[] src = (String[]) eventContext.transformMessage();
        return action(src);
    }

}
