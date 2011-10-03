/*
 * $Id: ParseHybridInvocationTest.java 1036 2010-11-24 08:47:10Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.regex;

import junit.framework.TestCase;

import org.mule.transport.salesforce.op.hybrid.InvocationPlaceholder;
import org.mule.transport.salesforce.op.hybrid.HybridInvocationParser;
import org.mule.transport.salesforce.op.hybrid.SfRegex;

public class ParseHybridInvocationTest extends TestCase
{

    protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testReturnIndication()
    {

        String endpointConfig = "*login(g1,g2);*query(g3,r1);";

        InvocationPlaceholder[] placeholders = HybridInvocationParser.getPlaceholders(endpointConfig,
            SfRegex.SALESFORCE_OPERATIONS_SEPERATOR_REGEX, SfRegex.SALESFORCE_PARAM_SEPERATOR_REGEX,
            SfRegex.SIMPLE_INVOCATION_PATTERN, SfRegex.PARAM_WITH_DELIMITERS_PATTERN, SfRegex.RETURN_PATTERN,
            SfRegex.RETURN_ALL_PATTERN);

        assertEquals(placeholders[0].getOperation(), "login");
        assertEquals(placeholders[0].getParameters()[0], "g1");
        assertEquals(placeholders[0].getParameters()[1], "g2");
        assertTrue(placeholders[0].isReturnable());

        assertEquals(placeholders[1].getOperation(), "query");
        assertEquals(placeholders[1].getParameters()[0], "g3");
        assertEquals(placeholders[1].getParameters()[1], "r1");
        assertTrue(placeholders[1].isReturnable());

        String endpointConfig2 = "login(g1,g2);*query(g3,r1);";

        InvocationPlaceholder[] kvps2 = HybridInvocationParser.getPlaceholders(endpointConfig2,
            SfRegex.SALESFORCE_OPERATIONS_SEPERATOR_REGEX, SfRegex.SALESFORCE_PARAM_SEPERATOR_REGEX,
            SfRegex.SIMPLE_INVOCATION_PATTERN, SfRegex.PARAM_WITH_DELIMITERS_PATTERN, SfRegex.RETURN_PATTERN,
            SfRegex.RETURN_ALL_PATTERN);

        assertEquals(kvps2[0].getOperation(), "login");
        assertEquals(kvps2[0].getParameters()[0], "g1");
        assertEquals(kvps2[0].getParameters()[1], "g2");
        assertTrue(!kvps2[0].isReturnable());

        assertEquals(kvps2[1].getOperation(), "query");
        assertEquals(kvps2[1].getParameters()[0], "g3");
        assertEquals(kvps2[1].getParameters()[1], "r1");
        assertTrue(kvps2[1].isReturnable());

    }

    public void testReturnAllIndication()
    {

        String endpointConfig = "login(g1,g2);query(g3,r1);*ALL";

        InvocationPlaceholder[] placeholders = HybridInvocationParser.getPlaceholders(endpointConfig,
            SfRegex.SALESFORCE_OPERATIONS_SEPERATOR_REGEX, SfRegex.SALESFORCE_PARAM_SEPERATOR_REGEX,
            SfRegex.SIMPLE_INVOCATION_PATTERN, SfRegex.PARAM_WITH_DELIMITERS_PATTERN, SfRegex.RETURN_PATTERN,
            SfRegex.RETURN_ALL_PATTERN);

        assertEquals(placeholders[0].getOperation(), "login");
        assertEquals(placeholders[0].getParameters()[0], "g1");
        assertEquals(placeholders[0].getParameters()[1], "g2");
        assertTrue(placeholders[0].isReturnable());

        assertEquals(placeholders[1].getOperation(), "query");
        assertEquals(placeholders[1].getParameters()[0], "g3");
        assertEquals(placeholders[1].getParameters()[1], "r1");
        assertTrue(placeholders[1].isReturnable());

        assertTrue(placeholders.length == 2);

        String endpointConfig2 = "login(g1,g2);*query(g3,r1);*All";

        InvocationPlaceholder[] placeholders2 = HybridInvocationParser.getPlaceholders(endpointConfig2,
            SfRegex.SALESFORCE_OPERATIONS_SEPERATOR_REGEX, SfRegex.SALESFORCE_PARAM_SEPERATOR_REGEX,
            SfRegex.SIMPLE_INVOCATION_PATTERN, SfRegex.PARAM_WITH_DELIMITERS_PATTERN, SfRegex.RETURN_PATTERN,
            SfRegex.RETURN_ALL_PATTERN);

        assertEquals(placeholders2[0].getOperation(), "login");
        assertEquals(placeholders2[0].getParameters()[0], "g1");
        assertEquals(placeholders2[0].getParameters()[1], "g2");
        assertTrue(placeholders2[0].isReturnable());

        assertEquals(placeholders2[1].getOperation(), "query");
        assertEquals(placeholders2[1].getParameters()[0], "g3");
        assertEquals(placeholders2[1].getParameters()[1], "r1");
        assertTrue(placeholders2[1].isReturnable());

        assertTrue(placeholders2.length == 2);
    }

}
