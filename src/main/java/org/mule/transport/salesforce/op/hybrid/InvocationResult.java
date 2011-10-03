/*
 * $Id: InvocationResult.java 1036 2010-11-24 08:47:10Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.op.hybrid;

public class InvocationResult
{

    private int resultNumber;

    public InvocationResult(int resultNumber)
    {
        this.resultNumber = resultNumber - 1;
        // resultNumber will be used as a list index. Therefore, it should not be
        // negative.
        assert (resultNumber >= 0) : "An InvocationResult is used to refer to the result of an invocation"
                                     + " which has not yet taken place. You refer to invocations starting with 1 for the first"
                                     + " invocation, 2 for the second, and so on.";
    }

    public int getResultNumberAsListIndex()
    {
        return resultNumber;
    }

}
