/*
 * $Id: InvocationPlaceholder.java 956 2010-09-09 11:52:25Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.op.hybrid;

public class InvocationPlaceholder
{

    private String operation;
    private String[] parameters;
    private boolean returnable = false;

    public InvocationPlaceholder(String operation, String[] parameters)
    {
        this.operation = operation;
        this.parameters = parameters;
    }

    public InvocationPlaceholder(String operation, String[] parameters, boolean returnable)
    {
        this.operation = operation;
        this.parameters = parameters;
        this.returnable = returnable;
    }

    public String getOperation()
    {
        return operation;
    }

    public String[] getParameters()
    {
        return parameters;
    }

    public boolean isReturnable()
    {
        return returnable;
    }

    public void setReturnable(boolean returnable)
    {
        this.returnable = returnable;
    }

}
