/*
 * $Id: InvokerFactory.java 956 2010-09-09 11:52:25Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.op.base;

import java.util.regex.Matcher;

import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.mule.api.MuleMessage;
import org.mule.transport.salesforce.op.hybrid.InvocationPlaceholder;
import org.mule.transport.salesforce.op.hybrid.HybridInvocationParser;
import org.mule.transport.salesforce.op.hybrid.ParamLoader;
import org.mule.transport.salesforce.op.hybrid.SfRegex;

public class InvokerFactory
{

    public static SfInvoker createInvoker(String endpointConfig,
                                          GenericKeyedObjectPool soapBindingPool,
                                          MuleMessage msg)
    {

        Matcher simpleInvocationMatcher = SfRegex.SIMPLE_INVOCATION_PATTERN.matcher(endpointConfig);
        Matcher hybridInvocationMatcher = SfRegex.HYBRID_INVOCATION_PATTERN.matcher(endpointConfig);

        if (simpleInvocationMatcher.matches())
        {

            AbstractInvocation invocation = InvocationFactory.createInvocation(endpointConfig,
                soapBindingPool, (Object[]) msg.getPayload());
            invocation.setReturnable(true);

            return new SfInvoker(new AbstractInvocation[]{invocation});

        }
        else if (hybridInvocationMatcher.matches())
        {

            ParamLoader pl = new ParamLoader((Object[]) msg.getPayload());

            InvocationPlaceholder[] placeholders = HybridInvocationParser.getPlaceholders(endpointConfig,
                SfRegex.SALESFORCE_OPERATIONS_SEPERATOR_REGEX, SfRegex.SALESFORCE_PARAM_SEPERATOR_REGEX,
                SfRegex.SIMPLE_INVOCATION_PATTERN, SfRegex.PARAM_WITH_DELIMITERS_PATTERN,
                SfRegex.RETURN_PATTERN, SfRegex.RETURN_ALL_PATTERN);

            AbstractInvocation[] invocations = new AbstractInvocation[placeholders.length];

            for (int i = 0; i < placeholders.length; i++)
            {
                invocations[i] = InvocationFactory.createInvocation(placeholders[i].getOperation(),
                    soapBindingPool, pl.load(placeholders[i].getParameters()));

                invocations[i].setReturnable(placeholders[i].isReturnable());
            }

            return new SfInvoker(invocations);

        }

        return null;

    }

}
