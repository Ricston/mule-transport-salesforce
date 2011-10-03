/*
 * $Id: SalesforceNamespaceHandler.java 1036 2010-11-24 08:47:10Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.config;

import org.mule.config.spring.handlers.AbstractMuleNamespaceHandler;
import org.mule.endpoint.URIBuilder;
import org.mule.transport.salesforce.SalesforceConnector;

/**
 * Registers a Bean Definition Parser for handling
 * <code><salesforce:connector></code> elements and supporting endpoint elements.
 */
public class SalesforceNamespaceHandler extends AbstractMuleNamespaceHandler
{
    public void init()
    {

        registerStandardTransportEndpoints(SalesforceConnector.SALESFORCE, URIBuilder.PATH_ATTRIBUTES);

        registerConnectorDefinitionParser(SalesforceConnector.class);
    }
}
