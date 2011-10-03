/*
 * $Id: HybridInvocationParser.java 956 2010-09-09 11:52:25Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.op.hybrid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class HybridInvocationParser
{

    public static InvocationPlaceholder[] getPlaceholders(String operations,
                                                          String opSepRegex,
                                                          String paramSepRegex,
                                                          Pattern opPattern,
                                                          Pattern paramPattern,
                                                          Pattern returnPattern,
                                                          Pattern returnAllPattern)
        throws PatternSyntaxException
    {

        // First, trim incoming string to remove leading and
        // trailing spaces.
        operations = operations.trim();

        String[] opStrs = operations.split(opSepRegex);

        // if the last element in opStrs contains a return all indication:
        if (returnAllPattern.matcher(opStrs[opStrs.length - 1]).matches())
        {
            // since the last element is just an indication to make all invocation's
            // results returnable
            // there is no need to make room for it in the InvocationPlaceholder[]
            InvocationPlaceholder[] placeholders = new InvocationPlaceholder[opStrs.length - 1];

            for (int i = 0; i < opStrs.length - 1; i++)
            {

                Matcher opMatcher = opPattern.matcher(opStrs[i]);
                Matcher paramMatcher = paramPattern.matcher(opStrs[i]);

                String operation = null;
                String paramUnparsed = null;

                while (opMatcher.find())
                {
                    operation = opMatcher.group();
                }
                while (paramMatcher.find())
                {
                    paramUnparsed = paramMatcher.group();
                }

                paramUnparsed = paramUnparsed.trim();

                placeholders[i] = new InvocationPlaceholder(operation, paramUnparsed.split(paramSepRegex),
                    true);

            }

            return placeholders;
        }
        else
        {
            InvocationPlaceholder[] placeholders = new InvocationPlaceholder[opStrs.length];

            for (int i = 0; i < opStrs.length; i++)
            {

                Matcher opMatcher = opPattern.matcher(opStrs[i]);
                Matcher paramMatcher = paramPattern.matcher(opStrs[i]);
                Matcher returnIndicationMatcher = returnPattern.matcher(opStrs[i]);

                String operation = null;
                String paramUnparsed = null;

                while (opMatcher.find())
                {
                    operation = opMatcher.group();
                }
                while (paramMatcher.find())
                {
                    paramUnparsed = paramMatcher.group();
                }

                paramUnparsed = paramUnparsed.trim();

                if (returnIndicationMatcher.find())
                    placeholders[i] = new InvocationPlaceholder(operation,
                        paramUnparsed.split(paramSepRegex), true);
                else
                    placeholders[i] = new InvocationPlaceholder(operation,
                        paramUnparsed.split(paramSepRegex), false);

            }

            return placeholders;

        }

    }

}
