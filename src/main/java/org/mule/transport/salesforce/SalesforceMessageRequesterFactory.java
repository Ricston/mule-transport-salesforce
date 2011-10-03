/*
 * $Id: SalesforceMessageRequesterFactory.java 1036 2010-11-24 08:47:10Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce;

import org.mule.api.MuleException;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.transport.MessageRequester;
import org.mule.transport.AbstractMessageRequesterFactory;

/**
 * <code>SalesforceMessageRequester</code> TODO document
 */
public class SalesforceMessageRequesterFactory extends AbstractMessageRequesterFactory
{

    public MessageRequester create(InboundEndpoint endpoint) throws MuleException
    {
        return new SalesforceMessageRequester(endpoint);
    }

}
