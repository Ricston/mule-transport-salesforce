/*
 * $Id: SalesforceConnector.java 1037 2010-11-24 14:09:16Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.transport.AbstractConnector;

import org.apache.commons.pool.impl.GenericKeyedObjectPool;

/**
 * <code>SalesforceConnector</code> TODO document
 */
public class SalesforceConnector extends AbstractConnector
{
    public SalesforceConnector(MuleContext context)
    {
        super(context);

    }

    /* This constant defines the main transport protocol identifier */
    public static final String SALESFORCE = "salesforce";

    protected GenericKeyedObjectPool soapBindingPool = new GenericKeyedObjectPool();
    protected SalesforceSoapBindingFactory factory = new SalesforceSoapBindingFactory();

    public GenericKeyedObjectPool getSoapBindingPool()
    {
        return soapBindingPool;
    }

    public void setSoapBindingPool(GenericKeyedObjectPool soapBindingPool)
    {
        this.soapBindingPool = soapBindingPool;
    }

    public void doInitialise() throws InitialisationException
    {

        soapBindingPool.setFactory(factory);

        // There should only be one pooled instance per socket (key)
        soapBindingPool.setMaxActive(1);
        soapBindingPool.setWhenExhaustedAction(GenericKeyedObjectPool.WHEN_EXHAUSTED_BLOCK);
    }

    public void doConnect() throws Exception
    {

    }

    public void doDisconnect() throws Exception
    {

    }

    public void doStart() throws MuleException
    {

    }

    public void doStop() throws MuleException
    {

    }

    public void doDispose()
    {

    }

    public String getProtocol()
    {
        return SALESFORCE;
    }

}
