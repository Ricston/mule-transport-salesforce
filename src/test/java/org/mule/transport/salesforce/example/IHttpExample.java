/*
 * $Id: IHttpExample.java 1036 2010-11-24 08:47:10Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.example;

import com.sforce.soap.partner.sobject.SObject;

/**
 * This interface is used to bind the
 * org.mule.transport.salesforce.example.HttpExampleInvoker component in
 * http-example.xml to LoginService via the login method and QueryService via the
 * query method.
 */
public interface IHttpExample
{

    public String login(String username, String password);

    public SObject[] query(String queryStr, String sessionId);

}
