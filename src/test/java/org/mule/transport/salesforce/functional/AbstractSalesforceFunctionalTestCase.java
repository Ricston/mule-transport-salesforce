/*
 * $Id: AbstractSalesforceFunctionalTestCase.java 956 2010-09-09 11:52:25Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.functional;

import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;

import java.io.InputStream;
import java.util.Properties;

/**
 * You will need to create two developer's account with Salesforce to get all these
 * tests to succeed. The only test which uses them both is testLoginLogout. After
 * creating the two accounts, generate a security token by going to Setup > My
 * Personal Information > Reset your security token; in your Salesforce account.
 * Finally, you will have to make the two usernames and passwords (with the security
 * token attached) the values of sfusername1, sfpassword1, and sfusername2,
 * sfpassword2 in the test.properties file in src/test/resources
 */
public abstract class AbstractSalesforceFunctionalTestCase extends FunctionalTestCase
{

    protected String username_1;
    protected String password_1;

    protected String username_2;
    protected String password_2;

    protected MuleClient client;

    protected Properties properties;

    @Override
    protected void doSetUp() throws Exception
    {
        super.doSetUp();
        client = new MuleClient();
        properties = new Properties();

        InputStream propertiesFile = this.getClass().getClassLoader().getResourceAsStream("test.properties");
        properties.load(propertiesFile);

        username_1 = properties.getProperty("sfusername1");
        password_1 = properties.getProperty("sfpassword1");
        username_2 = properties.getProperty("sfusername2");
        password_2 = properties.getProperty("sfpassword2");
    }

}
