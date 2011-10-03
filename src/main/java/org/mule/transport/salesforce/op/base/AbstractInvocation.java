/*
 * $Id: AbstractInvocation.java 1036 2010-11-24 08:47:10Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.op.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;

/**
 * Implementations of this class will perform the actual interaction with the
 * Salesforce API via the send() method.
 */
public abstract class AbstractInvocation
{

    protected Log logger = LogFactory.getLog(this.getClass());
    protected GenericKeyedObjectPool soapBindingPool;
    protected Object[] invocationParameters;

    private boolean returnable = false;

    public AbstractInvocation(GenericKeyedObjectPool soapBindingPool, Object[] invocationParameters)
    {
        this.soapBindingPool = soapBindingPool;
        this.invocationParameters = invocationParameters;
    }

    public boolean isReturnable()
    {
        return returnable;
    }

    public void setReturnable(boolean returnable)
    {
        this.returnable = returnable;
    }

    public Object[] getInvocationParameters()
    {
        return invocationParameters;
    }

    public void setInvocationParameters(Object[] invocationParameters)
    {
        this.invocationParameters = invocationParameters;
    }

    public abstract Object invoke() throws Exception;

}
