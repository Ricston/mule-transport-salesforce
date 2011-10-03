/*
 * $Id: SalesforceHybridInvocationsTestCase.java 1503 2011-10-03 14:02:28Z claude.mamo $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.functional;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleMessage;
import org.mule.transport.salesforce.SalesforceProperties;

import com.sforce.soap.partner.DeleteResult;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.sobject.SObject;

import javax.xml.namespace.QName;

import org.apache.axis.message.MessageElement;

public class SalesforceHybridInvocationsTestCase extends AbstractSalesforceFunctionalTestCase
{

    @Override
    protected String getConfigResources()
    {
        return "test-hybrid-invocations-mule-config.xml";
    }

    public SalesforceHybridInvocationsTestCase()
    {
        super();
        this.setDisposeManagerPerSuite(true);
    }

    @Override
    protected void doTearDown() throws Exception
    {
        super.doTearDown();
        client = null;

    }

    // ------------------------------------------------------------------------------------

    public void testLoginQuery() throws Exception
    {
        Object[] payload = new Object[3];
        payload[0] = username_1;
        payload[1] = password_1;
        payload[2] = "SELECT Contact.Firstname, Contact.Lastname, Contact.Department, "
                     + "Contact.Email, Contact.Birthdate FROM Contact" + " WHERE LastName = 'Boyle'";

        MuleMessage msg = client.send("vm://test.login.query", new DefaultMuleMessage(payload));

        assertNotNull(msg);
        assertNotNull(msg.getPayload());
        assertTrue(msg.getPayload() instanceof SObject[]);

        SObject[] contacts = (SObject[]) msg.getPayload();
        for (SObject o : contacts)
        {
            System.out.println(o.toString());

            MessageElement[] msgElement = o.get_any();

            String[] expected = new String[]{"Lauren", "Boyle", "Technology", "lboyle@uog.com", "1952-02-19"};

            // _any is an array of of org.apache.axis.message.MessageElement
            // therefore: "for all MessageElement(s) in each SObject o in contacts,
            // do:
            for (int i = 0; i < o.get_any().length; i++)
            {
                assertEquals(expected[i], msgElement[i].getValue());
                System.out.println("         " + msgElement[i].getName() + " : " + msgElement[i].getValue());
            }
        }

    }

    public void testLoginQueryLogout() throws Exception
    {

        Object[] payload = new Object[3];
        payload[0] = username_1;
        payload[1] = password_1;
        payload[2] = "SELECT Contact.Firstname, Contact.Lastname, Contact.Department, "
                     + "Contact.Email, Contact.Birthdate FROM Contact" + " WHERE LastName = 'Boyle'";

        MuleMessage msg = client.send("vm://test.login.query.logout", new DefaultMuleMessage(payload));

        assertNotNull(msg);
        assertNotNull(msg.getPayload());
        assertTrue(msg.getPayload() instanceof Object[]);

        Object[] result = (Object[]) msg.getPayload();

        assertTrue(result[0] instanceof SObject[]);
        assertTrue(result[1] instanceof String);

        SObject[] contacts = (SObject[]) result[0];
        String logoutResult = (String) result[1];

        assertTrue(logoutResult.startsWith(SalesforceProperties.SALESFORCE_SUCCESSFULL_LOGOUT));

        for (SObject o : contacts)
        {
            System.out.println(o.toString());

            MessageElement[] msgElement = o.get_any();

            String[] expected = new String[]{"Lauren", "Boyle", "Technology", "lboyle@uog.com", "1952-02-19"};

            // _any is an array of of org.apache.axis.message.MessageElement
            // therefore: "for all MessageElement(s) in each SObject o in contacts,
            // do:
            for (int i = 0; i < o.get_any().length; i++)
            {
                assertEquals(expected[i], msgElement[i].getValue());
                System.out.println("         " + msgElement[i].getName() + " : " + msgElement[i].getValue());
            }
        }

    }

    public void testLoginCreateQueryDelete() throws Exception
    {

        MessageElement[] contact = new MessageElement[5];
        contact[0] = new MessageElement(new QName("FirstName"), "DummyFirstName");
        contact[1] = new MessageElement(new QName("LastName"), "DummyLastName");
        contact[2] = new MessageElement(new QName("Salutation"), "Mr.");
        contact[3] = new MessageElement(new QName("Phone"), "111.111.1111");
        contact[4] = new MessageElement(new QName("Title"), "CEO");

        // Create an array of SObjects to hold the contacts
        SObject[] sObjects = new SObject[1];

        // Add the contact to the SObject array
        sObjects[0] = new SObject();
        sObjects[0].set_any(contact);
        sObjects[0].setType("Contact");

        Object[] payload = new Object[4];
        payload[0] = username_1;
        payload[1] = password_1;
        payload[2] = sObjects;
        payload[3] = "SELECT Id FROM Contact WHERE LastName = 'DummyLastName'";

        MuleMessage msg = client.send("vm://test.login.create.query.delete", new DefaultMuleMessage(payload));

        assertNotNull(msg);
        assertNotNull(msg.getPayload());
        assertTrue(msg.getPayload() instanceof Object[]);

        Object[] results = (Object[]) msg.getPayload();

        assertTrue(results.length == 3);
        assertTrue(results[0] instanceof SaveResult[]);
        assertTrue(results[1] instanceof SObject[]);
        assertTrue(results[2] instanceof DeleteResult[]);

        SaveResult[] saveResult = (SaveResult[]) results[0];
        SObject[] queryResult = (SObject[]) results[1];
        DeleteResult[] deleteResult = (DeleteResult[]) results[2];

        assertTrue(saveResult.length == 1 && queryResult.length == 1 && deleteResult.length == 1);

        assertEquals(queryResult[0].getId(), saveResult[0].getId());
        assertEquals(deleteResult[0].getId(), saveResult[0].getId());

    }

}
