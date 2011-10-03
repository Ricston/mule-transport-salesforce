/*
 * $Id: SfRegex.java 956 2010-09-09 11:52:25Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.op.hybrid;

import java.util.regex.Pattern;

import org.mule.transport.salesforce.SalesforceProperties;

public class SfRegex
{

    public static final String PARAM_GIVEN_DATA_CODE = "g";
    public static final String PARAM_INVOCATION_RESULT_DATA_CODE = "r";

    private static final String SALESFORCE_OPERATIONS_REGEX = allSalesforceOperationsAsRegex();

    // note: the ; is not a special character in regex and so it does not need to be
    // escaped. It is
    // escaped anyway just in case.
    public static final String SALESFORCE_OPERATIONS_SEPERATOR_REGEX = "\\s*\\;\\s*";

    // note: the , is not a special character in regex and so it does not need to be
    // escaped. It is
    // escaped anyway just in case.
    public static final String SALESFORCE_PARAM_SEPERATOR_REGEX = "\\s*\\,\\s*";

    // used in order to get the parameters of each invocation.
    // e.g. for login(g1, g2), it gives: "g1, g2"
    private static final String PARAM_ONLY_REGEX = "\\s*[gGrR][1-9]\\d*\\s*(,\\s*[gGrR][1-9]\\d*\\s*)*";

    private static final String HYBRID_RETURN_INVOCATION_RESULT_REGEX = "\\s*\\*\\s*";
    private static final String HYBRID_RETURN_ALL_INVOCATION_RESULT_REGEX = "\\s*\\*\\s*all\\s*";

    private static final String PARAM_SECTION_REGEX = "\\(" + PARAM_ONLY_REGEX + "\\)\\s*;\\s*";

    // (?i)(\s*(\s*\*\s*)?((login)|(logout)|...|(query))\s*\(\s*[gGrR][1-9]\d*\s*(,\s*[gGrR][1-9]\d*\s*)*\)\s*;\s*)+(\s*\*\s*all\s*)?
    private static final String SALESFORCE_HYBRID_OPERATIONS_REGEX = "(?i)(\\s*("
                                                                     + HYBRID_RETURN_INVOCATION_RESULT_REGEX
                                                                     + ")?"
                                                                     + SALESFORCE_OPERATIONS_REGEX
                                                                     + "\\s*"
                                                                     + PARAM_SECTION_REGEX
                                                                     + ")+("
                                                                     + HYBRID_RETURN_ALL_INVOCATION_RESULT_REGEX
                                                                     + ")?";

    public static final Pattern SIMPLE_INVOCATION_PATTERN = Pattern.compile(SALESFORCE_OPERATIONS_REGEX);

    public static final Pattern PARAM_WITH_DELIMITERS_PATTERN = Pattern.compile(PARAM_ONLY_REGEX);

    public static final Pattern HYBRID_INVOCATION_PATTERN = Pattern.compile(SALESFORCE_HYBRID_OPERATIONS_REGEX);

    public static final Pattern RETURN_ALL_PATTERN = Pattern.compile("(?i)"
                                                                     + HYBRID_RETURN_ALL_INVOCATION_RESULT_REGEX);

    public static final Pattern RETURN_PATTERN = Pattern.compile(HYBRID_RETURN_INVOCATION_RESULT_REGEX);

    // --------------------------------------------------------------------------------------------

    private static String allSalesforceOperationsAsRegex()
    {
        StringBuilder strBuilder = new StringBuilder(450);

        for (int i = 0; i < SalesforceProperties.ALL_SALESFORCE_OPERATIONS.length; i++)
        {
            if (i == SalesforceProperties.ALL_SALESFORCE_OPERATIONS.length - 1)
            {
                strBuilder.append(String.format("(%s)", SalesforceProperties.ALL_SALESFORCE_OPERATIONS[i]));
            }
            else
            {
                strBuilder.append(String.format("(%s)|", SalesforceProperties.ALL_SALESFORCE_OPERATIONS[i]));
            }
        }

        strBuilder.insert(0, "(");
        strBuilder.insert(strBuilder.length(), ")");

        return strBuilder.toString();
    }

}
