/*
 * $Id: SalesforceMessageDispatcher.java 1037 2010-11-24 14:09:16Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce;

import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.endpoint.OutboundEndpoint;
import org.mule.transport.AbstractMessageDispatcher;
import org.mule.transport.salesforce.op.base.InvokerFactory;
import org.mule.transport.salesforce.op.base.SfInvoker;

/**
 * <code>SalesforceMessageDispatcher</code> TODO document
 */
public class SalesforceMessageDispatcher extends AbstractMessageDispatcher
{
    public SalesforceMessageDispatcher(OutboundEndpoint endpoint)
    {
        super(endpoint);

    }

    public void doConnect() throws Exception
    {
    }

    public void doDisconnect() throws Exception
    {
    }

    public void doDispatch(MuleEvent event) throws Exception
    {
        throw new UnsupportedOperationException("doDispatch");
    }

    /**
     * An AbstractSalesforceInvokerFactory is used to instantiate a class which will
     * call the relevant Salesforce functionality.
     * 
     * @return a MuleMessage which wraps the returned value of invoking the
     *         Salesforce supported methods which have been implemented in this
     *         transport. (note: not all returned values are identical to the types
     *         returned by the Salesforce API. For example, Salesforce's query()
     *         returns a QueryResult but its return value is a MuleMessage with a
     *         SObject[] payload here).
     */
    public MuleMessage doSend(MuleEvent event) throws Exception
    {
        MuleMessage msg = event.getMessage();

        SfInvoker invoker = InvokerFactory.createInvoker(event.getEndpoint().getEndpointURI().getAuthority(),
            ((SalesforceConnector) getConnector()).getSoapBindingPool(), msg);

        invoker.setMuleContext(event.getMuleContext());

        if (invoker != null)
            return invoker.invoke();
        else
            return null;

    }

    public void doDispose()
    {
    }

}
