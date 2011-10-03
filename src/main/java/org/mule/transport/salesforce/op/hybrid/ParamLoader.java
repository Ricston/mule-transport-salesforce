/*
 * $Id: ParamLoader.java 956 2010-09-09 11:52:25Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.op.hybrid;

public class ParamLoader
{

    private Object[] dataGiven;

    public ParamLoader(Object[] dataGiven)
    {
        this.dataGiven = dataGiven;
    }

    public Object[] load(String[] codedParam)
    {
        Object[] parameters = new Object[codedParam.length];

        for (int i = 0; i < codedParam.length; i++)
        {
            // "g" or "r"
            String gOrR = codedParam[i].substring(0, 1);

            if (gOrR.equalsIgnoreCase(SfRegex.PARAM_GIVEN_DATA_CODE))
            {
                parameters[i] = dataGiven[Integer.parseInt(codedParam[i].substring(1)) - 1];
            }
            else if (gOrR.equalsIgnoreCase(SfRegex.PARAM_INVOCATION_RESULT_DATA_CODE))
            {
                parameters[i] = new InvocationResult(Integer.parseInt(codedParam[i].substring(1)));
            }
        }

        return parameters;
    }

}
