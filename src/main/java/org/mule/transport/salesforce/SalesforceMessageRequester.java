/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce;

import org.mule.api.MuleMessage;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.transport.AbstractMessageRequester;

/**
 * <code>SalesforceMessageRequester</code> TODO document
 */
public class SalesforceMessageRequester extends AbstractMessageRequester
{

    public SalesforceMessageRequester(InboundEndpoint endpoint)
    {
        super(endpoint);
    }

    protected MuleMessage doRequest(long timeout) throws Exception
    {

        return null;
    }

    public void doConnect() throws Exception
    {

    }

    public void doDisconnect() throws Exception
    {

    }

    public void doDispose()
    {

    }

}
