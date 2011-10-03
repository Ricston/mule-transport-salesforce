/*
 * $Id: SfInvoker.java 1037 2010-11-24 14:09:16Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.op.base;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.transport.salesforce.op.hybrid.InvocationResult;

import java.util.ArrayList;
import java.util.List;

public class SfInvoker /*implements MuleContextAware*/
{
    private AbstractInvocation[] invocations;
    private List<Object> resultList;
    
    private MuleContext muleContext;
    
    public void setMuleContext(MuleContext muleContext) {
        this.muleContext = muleContext;
    }

    public SfInvoker(AbstractInvocation[] invocations)
    {
        this.invocations = invocations;
        this.resultList = new ArrayList<Object>(invocations.length);
    }

    public MuleMessage invoke() throws Exception
    {

        for (int i = 0; i < invocations.length; i++)
        {

            // If it's not the first invocation, setUpInvocationParameters of the
            // invocation
            // because it might contain InvocationResult substitutes for data instead
            // of the
            // actual data needed by the invocation.
            if (i != 0) setUpInvocationParameters(invocations[i]);

            resultList.add(invocations[i].invoke());

        }

        // this avoids returning an Object[] if there's only one Object being
        // returned.
        if (invocations.length == 1)
        {
            return new DefaultMuleMessage(resultList.get(0), muleContext);
        }

        List<Object> returnList = new ArrayList<Object>(invocations.length);

        // before returning, info is needed on what to return.
        for (int i = 0; i < invocations.length; i++)
        {
            if (invocations[i].isReturnable())
            {
                returnList.add(resultList.get(i));
            }
        }

        if (returnList.size() == 1)
        {
            return new DefaultMuleMessage(returnList.get(0), muleContext);
        }
        else
        {
            return new DefaultMuleMessage(returnList.toArray(), muleContext);
        }

    }

    private void setUpInvocationParameters(AbstractInvocation invocation)
    {
        Object[] invocationParameters = invocation.getInvocationParameters();

        // Substitute all InvocationResult objects in the invocationParameters
        // with the relevant
        // objects stored in resultList.
        for (int i = 0; i < invocationParameters.length; i++)
        {
            if (invocationParameters[i] instanceof InvocationResult)
            {
                InvocationResult invocationResult = (InvocationResult) invocationParameters[i];
                invocationParameters[i] = resultList.get(invocationResult.getResultNumberAsListIndex());
            }
        }
        invocation.setInvocationParameters(invocationParameters);
    }
    
}
