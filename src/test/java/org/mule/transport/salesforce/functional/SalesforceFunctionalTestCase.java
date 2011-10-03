/*
 * $Id: SalesforceFunctionalTestCase.java 1037 2010-11-24 14:09:16Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.functional;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.transport.salesforce.SalesforceProperties;

import com.sforce.soap.partner.DeleteResult;
import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.Error;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.FieldType;
import com.sforce.soap.partner.PicklistEntry;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.SearchRecord;
import com.sforce.soap.partner.UpsertResult;
import com.sforce.soap.partner.sobject.SObject;

import java.util.ArrayList;
import java.util.Date;

import javax.xml.namespace.QName;

import org.apache.axis.message.MessageElement;

public class SalesforceFunctionalTestCase extends AbstractSalesforceFunctionalTestCase
{

    protected final String dummy_name = "Tom";
    protected final String dummy_surname = "Green";
    protected final String full_dummy_name = dummy_name + " " + dummy_surname;

    public SalesforceFunctionalTestCase()
    {
        super();
        this.setDisposeManagerPerSuite(true);
    }

    @Override
    protected String getConfigResources()
    {
        return "test-mule-config.xml";
    }

    private MuleMessage setUpLogin(String username, String password) throws MuleException
    {

        Object[] payload = new Object[2];
        payload[0] = username;
        payload[1] = password;

        return client.send("vm://test.login", new DefaultMuleMessage(payload, muleContext));

    }

    private MuleMessage search(String searchQuery, String sessionId) throws MuleException
    {

        return client.send("vm://test.search", new DefaultMuleMessage(new Object[]{searchQuery, sessionId},
            muleContext));

    }

    // ----------------------------------------------------------------------------------

    /**
     * Tests logging in with an incorrect password.
     */
    public void testInvalidLogin() throws Exception
    {

        MuleMessage response = setUpLogin(username_1, password_1 + "invalid");

        assertTrue(response.getPayload() instanceof String);
        String responseString = response.getPayloadAsString();
        System.out.println(responseString);
        assertTrue(responseString.startsWith("INVALID_LOGIN"));

    }

    /**
     * Tests logging in and out with 2 different accounts and checking that they have
     * different session ids.
     * 
     * @throws Exception
     */
    public void testLoginLogout() throws Exception
    {

        MuleMessage msgLogin_1;
        String sessionId_1;
        String strLogin_1;

        MuleMessage msgLogin_2;
        String sessionId_2;
        String strLogin_2;

        MuleMessage msgLogout_1;
        String strLogout_1;

        MuleMessage againMsgLogin_1;
        String differentFromSessionId_1;
        String againStrLogin_1;

        MuleMessage msgLogout_2;
        String strLogout_2;

        // login with username_1 and password_1
        msgLogin_1 = setUpLogin(username_1, password_1);
        sessionId_1 = msgLogin_1.getPayloadAsString().substring(
            SalesforceProperties.SALESFORCE_SUCCESSFULL_LOGIN.length());

        assertTrue(msgLogin_1.getPayload() instanceof String);
        strLogin_1 = msgLogin_1.getPayloadAsString();
        System.out.println("Login with username and password = " + strLogin_1);
        assertTrue(strLogin_1.startsWith(SalesforceProperties.SALESFORCE_SUCCESSFULL_LOGIN));

        // login with username_2 and password_2 and assert that first and second
        // logins have different sessionIds
        msgLogin_2 = setUpLogin(username_2, password_2);
        sessionId_2 = msgLogin_2.getPayloadAsString().substring(
            SalesforceProperties.SALESFORCE_SUCCESSFULL_LOGIN.length());

        assertTrue(msgLogin_2.getPayload() instanceof String);
        strLogin_2 = msgLogin_2.getPayloadAsString();
        System.out.println(strLogin_2);
        assertTrue(strLogin_2.startsWith(SalesforceProperties.SALESFORCE_SUCCESSFULL_LOGIN));
        assertFalse(sessionId_1.equals(sessionId_2));

        // logout of sessionId_1 (corresponding username_1 and password_1)
        msgLogout_1 = client.send("vm://test.logout", new DefaultMuleMessage(new Object[]{sessionId_1},
            muleContext));

        assertTrue(msgLogout_1.getPayload() instanceof String);
        strLogout_1 = msgLogout_1.getPayloadAsString();
        System.out.println(strLogout_1);
        assertTrue(strLogout_1.startsWith(SalesforceProperties.SALESFORCE_SUCCESSFULL_LOGOUT));
        assertEquals(sessionId_1,
            strLogout_1.substring(SalesforceProperties.SALESFORCE_SUCCESSFULL_LOGOUT.length()));

        // login again using username_1 and password_1 (make sure that the new
        // sessionId (i.e. differentFromSessionId_1)
        // is != sessionId_1). Also, assert that differentFromSessionId_1 !=
        // sessionId_2
        againMsgLogin_1 = setUpLogin(username_1, password_1);
        differentFromSessionId_1 = againMsgLogin_1.getPayloadAsString().substring(
            SalesforceProperties.SALESFORCE_SUCCESSFULL_LOGIN.length());

        assertTrue(againMsgLogin_1.getPayload() instanceof String);
        againStrLogin_1 = againMsgLogin_1.getPayloadAsString();
        System.out.println("Login again with username and password = " + againStrLogin_1);
        assertTrue(againStrLogin_1.startsWith(SalesforceProperties.SALESFORCE_SUCCESSFULL_LOGIN));
        assertFalse(differentFromSessionId_1.equals(sessionId_1));
        assertFalse(differentFromSessionId_1.equals(sessionId_2));

        // logout of sessionId_2 (corresponding username_2 and password_2 )
        msgLogout_2 = client.send("vm://test.logout", new DefaultMuleMessage(new Object[]{sessionId_2},
            muleContext));

        assertTrue(msgLogout_2.getPayload() instanceof String);
        strLogout_2 = msgLogout_2.getPayloadAsString();
        System.out.println(strLogout_2);
        assertTrue(strLogout_2.startsWith(SalesforceProperties.SALESFORCE_SUCCESSFULL_LOGOUT));
        assertEquals(sessionId_2,
            strLogout_2.substring(SalesforceProperties.SALESFORCE_SUCCESSFULL_LOGOUT.length()));

    }

    /**
     * Tests querying.
     * 
     * @throws Exception
     */
    public void testSingleQueryById() throws Exception
    {

        MuleMessage response = setUpLogin(username_2, password_2);
        String sessionId = response.getPayloadAsString().substring(
            SalesforceProperties.SALESFORCE_SUCCESSFULL_LOGIN.length());

        MuleMessage msg = client.send("vm://test.query", new String[]{
            "SELECT Id, Firstname, Lastname, Account.Name, AccountId  FROM Contact", sessionId}, null);

        assertNotNull(msg);
        assertNotNull(msg.getPayload());
        System.out.println(msg.getPayloadAsString());
        SObject[] contacts = (SObject[]) msg.getPayload();
        for (SObject o : contacts)
        {
            System.out.println(o.toString());

            MessageElement[] msgElement = o.get_any();
            // _any is an array of of org.apache.axis.message.MessageElement
            // therefore: "for all MessageElement(s) in each SObject o in
            // contacts,
            // do:
            for (int i = 0; i < o.get_any().length; i++)
            {
                System.out.println("         " + msgElement[i].getName() + " : " + msgElement[i].getValue());
            }
        }
    }

    /**
     * Tests creating and deleting Contacts. All created contacts are also deleted.
     * 
     * @throws Exception
     */
    public void testCreateDelete() throws Exception
    {

        MuleMessage response = setUpLogin(username_2, password_2);
        String sessionId = response.getPayloadAsString().substring(
            SalesforceProperties.SALESFORCE_SUCCESSFULL_LOGIN.length());

        // ---------
        //
        // http://www.mulesoft.org/documentation/display/SFDC/More+Examples
        //
        MessageElement[] contact = new MessageElement[5];
        contact[0] = new MessageElement(new QName("FirstName"), dummy_name);
        contact[1] = new MessageElement(new QName("LastName"), dummy_surname);
        contact[2] = new MessageElement(new QName("Salutation"), "Mr.");
        contact[3] = new MessageElement(new QName("Phone"), "111.111.1111");
        contact[4] = new MessageElement(new QName("Title"), "CEO");

        // Create an array of SObjects to hold the contacts
        SObject[] sObjects = new SObject[1];

        // Add the contact to the SObject array
        sObjects[0] = new SObject();
        sObjects[0].set_any(contact);
        sObjects[0].setType("Contact");

        // -----------

        Object[] payloadCreate = new Object[2];
        payloadCreate[0] = sObjects;
        payloadCreate[1] = sessionId;

        MuleMessage replyOfCreate = client.send("vm://test.create", new DefaultMuleMessage(payloadCreate,
            muleContext));

        assertNotNull(replyOfCreate);
        assertNotNull(replyOfCreate.getPayload());

        assertTrue(replyOfCreate.getPayload() instanceof SaveResult[]);
        SaveResult[] saveResult = (SaveResult[]) replyOfCreate.getPayload();

        // ----

        Object[] payloadQuery = new Object[2];
        payloadQuery[0] = new String("SELECT Id FROM Contact WHERE Name = '" + full_dummy_name + "'");
        payloadQuery[1] = sessionId;

        MuleMessage replyOfQuery = client.send("vm://test.query", new DefaultMuleMessage(payloadQuery,
            muleContext));
        assertNotNull(replyOfQuery);
        assertNotNull(replyOfQuery.getPayload());

        assertTrue(replyOfQuery.getPayload() instanceof SObject[]);
        SObject[] sObject = (SObject[]) replyOfQuery.getPayload();
        assertTrue(sObject.length == 1);

        assertEquals(sObject[0].getId(), saveResult[0].getId());

        // ---

        String[] deleteIds = new String[]{saveResult[0].getId()};

        Object[] payloadDelete = new Object[2];
        payloadDelete[0] = deleteIds;
        payloadDelete[1] = sessionId;

        MuleMessage replyOfDelete = client.send("vm://test.delete", new DefaultMuleMessage(payloadDelete,
            muleContext));

        assertNotNull(replyOfDelete);
        assertNotNull(replyOfDelete.getPayload());

        assertTrue(replyOfDelete.getPayload() instanceof DeleteResult[]);
        DeleteResult[] deleteResult = (DeleteResult[]) replyOfDelete.getPayload();

        assertTrue(deleteResult.length == 1);
        assertEquals(saveResult[0].getId(), deleteResult[0].getId());

        // ---

        Object[] payloadQuery2 = new Object[2];
        payloadQuery2[0] = new String("SELECT Id FROM Contact WHERE Name = '" + full_dummy_name + "'");
        payloadQuery2[1] = sessionId;

        MuleMessage replyOfQuery2 = client.send("vm://test.query", new DefaultMuleMessage(payloadQuery2,
            muleContext));

        assertNotNull(replyOfQuery2);
        assertTrue(replyOfQuery2.getPayload() instanceof SObject[]);
        SObject[] sObject2 = (SObject[]) replyOfQuery2.getPayload();
        assertTrue(sObject2.length == 0);
        System.out.println(full_dummy_name + " could not be found.");

    }

    /**
     * Tests searching.
     * 
     * @throws MuleException
     * @throws Exception
     */
    public void testSearch() throws MuleException, Exception
    {

        // Refer to:
        // http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_search.htm

        MuleMessage response = setUpLogin(username_2, password_2);

        String sessionId = response.getPayloadAsString().substring(
            SalesforceProperties.SALESFORCE_SUCCESSFULL_LOGIN.length());

        // Phone of sForce account is: 4159017000
        String queryStr = new String("find {4159017000} " + "in phone fields " + "returning "
                                     + "contact(id, phone, firstname, lastname), "
                                     + "lead(id, phone, firstname, lastname), " + "account(id, phone, name)");

        MuleMessage mm = search(queryStr, sessionId);

        assertNotNull(mm);
        assertNotNull(mm.getPayload());

        assertTrue(mm.getPayload() instanceof SearchRecord[]);
        SearchRecord[] records = (SearchRecord[]) mm.getPayload();

        if (records != null && records.length > 0)
        {
            // We are going to use lists to hold the results
            ArrayList<SObject> contacts = new ArrayList<SObject>();
            ArrayList<SObject> leads = new ArrayList<SObject>();
            ArrayList<SObject> accounts = new ArrayList<SObject>();
            // We go through the results and determine what type
            // of object we found by using instanceof and add each record
            // to the correct ArrayList
            for (int i = 0; i < records.length; i++)
            {
                SObject record = records[i].getRecord();
                if (record.getType().equals("Contact"))
                {
                    contacts.add(record);
                }
                else if (record.getType().equals("Lead"))
                {
                    leads.add(record);
                }
                else if (record.getType().equals("Account"))
                {
                    accounts.add(record);
                }
            }

            // we now have our results sorted into buckets of specific types
            // so we can report our findings
            if (contacts.size() > 0)
            {
                System.out.println("Found " + contacts.size() + " contacts:");
                for (int i = 0; i < contacts.size(); i++)
                {
                    SObject contact = (SObject) contacts.get(i);
                    System.out.println("Type: " + contact.getType() + ", Id: " + contact.getId());
                }
            }
            if (leads.size() > 0)
            {
                System.out.println("Found " + leads.size() + " leads:");
                for (int i = 0; i < leads.size(); i++)
                {
                    SObject lead = (SObject) leads.get(i);
                    System.out.println("Type: " + lead.getType() + ", Id: " + lead.getId());
                }
            }
            if (accounts.size() > 0)
            {
                System.out.println("Found " + accounts.size() + " accounts:");
                for (int i = 0; i < accounts.size(); i++)
                {
                    SObject account = (SObject) accounts.get(i);
                    System.out.println("Type: " + account.getType() + ", Id: " + account.getId());
                }
            }
        }
        else
        {
            System.out.println("No records were found for the search.");
        }

    }

    /**
     * Tests updating. After the updated value is changed it is updated again to
     * restore its state.
     * 
     * @throws MuleException
     * @throws Exception
     */
    public void testUpdate() throws MuleException, Exception
    {

        MuleMessage response = setUpLogin(username_2, password_2);

        String sessionId = response.getPayloadAsString().substring(
            SalesforceProperties.SALESFORCE_SUCCESSFULL_LOGIN.length());

        // Phone of sForce account is: (415) 901-7000
        // To search for it, use: 4159017000
        // To restore it, use: (415) 901-7000 (so that it'll be the same as you
        // started off with)
        String queryStr = new String("find {4159017000} " + "in phone fields " + "returning account(id)");

        MuleMessage mm = search(queryStr, sessionId);

        assertTrue(mm.getPayload() instanceof SearchRecord[]);
        SearchRecord[] records = (SearchRecord[]) mm.getPayload();

        if (records.length > 0)
        {
            System.out.println("Found " + records.length + " objects:");
            for (int i = 0; i < records.length; i++)
            {
                System.out.println("Type: " + records[i].getRecord().getType() + ", Id: "
                                   + records[i].getRecord().getId());
            }
        }
        else
        {
            System.out.println("No objects were found for the search.");
        }

        MessageElement[] updateMsgElement = new MessageElement[1];
        updateMsgElement[0] = new MessageElement(new QName("Phone"), "12345678");

        // For the sake of this test, just one SObject will be found in the
        // search
        // (of type Account).
        // That object will be the sForce Account.
        SObject[] objToUpdate = new SObject[1];

        // objToUpdate[0] = records[0].getRecord();
        objToUpdate[0] = new SObject();
        objToUpdate[0].set_any(updateMsgElement);
        objToUpdate[0].setType("Account");
        objToUpdate[0].setId(records[0].getRecord().getId());
        System.out.println("Id of SObject to update = " + records[0].getRecord().getId());

        MuleMessage msg = client.send("vm://test.update", new DefaultMuleMessage(new Object[]{objToUpdate,
            sessionId}, muleContext));

        assertNotNull(msg);
        assertNotNull(msg.getPayload());

        assertTrue(msg.getPayload() instanceof SaveResult[]);
        SaveResult[] saveResults = (SaveResult[]) msg.getPayload();

        if (saveResults != null && saveResults.length > 0)
        {
            if (saveResults[0].isSuccess())
            {
                System.out.println("Id of updated Object: " + saveResults[0].getId());
            }
            else
            {
                // Handle the errors. We just print the first error out for
                // sample
                // purposes.
                Error[] errors = saveResults[0].getErrors();
                if (errors.length > 0)
                {
                    System.out.println("Error code: " + errors[0].getStatusCode());
                    System.out.println("Error message: " + errors[0].getMessage());
                }
            }
        }

        MessageElement[] restoreMsgElement = new MessageElement[1];
        restoreMsgElement[0] = new MessageElement(new QName("Phone"), "(415) 901-7000");

        SObject[] objToResotre = new SObject[1];

        // objToUpdate[0] = records[0].getRecord();
        objToResotre[0] = new SObject();
        objToResotre[0].set_any(restoreMsgElement);
        objToResotre[0].setType("Account");
        objToResotre[0].setId(records[0].getRecord().getId());

        MuleMessage msgRestored = client.send("vm://test.update", new DefaultMuleMessage(new Object[]{
            objToResotre, sessionId}, muleContext));

        assertTrue(msgRestored.getPayload() instanceof SaveResult[]);
        SaveResult[] saveResultRestored = (SaveResult[]) msgRestored.getPayload();

        if (saveResultRestored != null && saveResultRestored.length > 0)
        {
            if (saveResultRestored[0].isSuccess())
            {
                System.out.println("Id of restored Object: " + saveResultRestored[0].getId());
            }
            else
            {
                // Handle the errors. We just print the first error out for
                // sample
                // purposes.
                Error[] errors = saveResultRestored[0].getErrors();
                if (errors.length > 0)
                {
                    System.out.println("Error code: " + errors[0].getStatusCode());
                    System.out.println("Error message: " + errors[0].getMessage());
                }
            }
        }

    }

    /**
     * Tests describeSObjects and provides information about the Account and Contact
     * type.
     * 
     * @throws Exception
     */
    public void testDescribeSObjects() throws Exception
    {

        MuleMessage msg = setUpLogin(username_2, password_2);
        String sessionId = msg.getPayloadAsString().substring(
            SalesforceProperties.SALESFORCE_SUCCESSFULL_LOGIN.length());

        DescribeSObjectResult[] describeSObjectResults;

        try
        {
            // add other types (after "contact") to see their description.
            MuleMessage msgDescribe = client.send("vm://test.describeSObjects", new DefaultMuleMessage(
                new Object[]{new String[]{"account", "contact"}, sessionId}, muleContext));

            assertNotNull(msgDescribe);
            assertNotNull(msgDescribe.getPayload());

            assertTrue(msgDescribe.getPayload() instanceof DescribeSObjectResult[]);
            describeSObjectResults = (DescribeSObjectResult[]) msgDescribe.getPayload();

            for (int x = 0; x < describeSObjectResults.length; x++)
            {
                DescribeSObjectResult describeSObjectResult = describeSObjectResults[x];
                // Retrieve fields from the results
                Field[] fields = describeSObjectResult.getFields();
                // Get the name of the object
                String objectName = describeSObjectResult.getName();
                // Get some flags
                boolean isActivateable = describeSObjectResult.isActivateable();
                boolean isCreateable = describeSObjectResult.isCreateable();
                boolean isDeletable = describeSObjectResult.isDeletable();
                boolean isMergeable = describeSObjectResult.isMergeable();
                boolean isQueryable = describeSObjectResult.isQueryable();
                boolean isSearchable = describeSObjectResult.isSearchable();
                boolean isRetrieveable = describeSObjectResult.isRetrieveable();
                boolean isUpdateable = describeSObjectResult.isUpdateable();
                boolean isUndeletable = describeSObjectResult.isUndeletable();

                System.out.println("Object name: " + objectName);
                System.out.println("  isActivateable: " + isActivateable + ", isCreateable: " + isCreateable
                                   + ", isDeletable: " + isDeletable + ", isMergeable: " + isMergeable
                                   + ", isQueryable: " + isQueryable + ", isSearchable: " + isSearchable
                                   + ", isRetrieveable: " + isRetrieveable + ", isUpdateable: "
                                   + isUpdateable + ", isUndeletable: " + isUndeletable);

                if (fields != null)
                {
                    // Iterate through the fields to get properties for each
                    // field
                    for (int i = 0; i < fields.length; i++)
                    {
                        Field field = fields[i];
                        int byteLength = field.getByteLength();
                        int digits = field.getDigits();
                        String label = field.getLabel();
                        int length = field.getLength();
                        String name = field.getName();
                        PicklistEntry[] picklistValues = field.getPicklistValues();
                        String[] referenceTos = field.getReferenceTo();
                        FieldType fieldType = field.getType();
                        boolean fieldIsCreateable = field.isCreateable();
                        System.out.println("\tField name: " + name);
                        boolean isIdLookup = field.isIdLookup();
                        System.out.println("\t\tfieldType = " + fieldType + ", label = " + label
                                           + ", length = " + length + ", byteLength = " + byteLength
                                           + ", digits = " + digits + ", fieldIsCreateable = "
                                           + fieldIsCreateable + ", isIdLookup = " + isIdLookup);
                        // Determine whether there are picklist values
                        if (picklistValues != null && picklistValues[0] != null)
                        {
                            System.out.println("\t\tPicklist values = ");
                            for (int j = 0; j < picklistValues.length; j++)
                            {
                                if (picklistValues[j].getLabel() != null)
                                {
                                    System.out.println("\t\t\tItem: " + picklistValues[j].getLabel());
                                }
                            }
                        }
                        // Determine whether this field refers to another object
                        if (referenceTos != null && referenceTos[0] != null)
                        {
                            System.out.print("\t\tField references the following objects:");
                            for (int j = 0; j < referenceTos.length; j++)
                            {
                                System.out.print(" " + referenceTos[j]);
                            }
                            System.out.println();
                        }
                    }
                }
                System.out.println("\n---\t---\t---\t---\t---\t---\t---\t---\n");
            }
        }
        catch (Exception ex)
        {
            System.out.println("\nFailed to get object descriptions, error message was: \n" + ex.getMessage());
        }

    }

    /**
     * Will first test upsert when there isn't an opportunity with the relevant name.
     * This will create the opportunity. Assert on that result (that it was
     * successful). Will then test upsert with the same name of the opportunity which
     * was inserted above. This will update that opportunity (e.g. change the
     * amount). Assert on that result (that it was successful) and then delete this
     * opportunity to keep the state unchanged.
     * 
     * @throws Exception
     */
    public void testUpsert() throws Exception
    {

        MuleMessage msg = setUpLogin(username_2, password_2);
        String sessionId = msg.getPayloadAsString().substring(
            SalesforceProperties.SALESFORCE_SUCCESSFULL_LOGIN.length());

        MessageElement[] opportunity = new MessageElement[4];

        opportunity[0] = new MessageElement(new QName("Name"), "BlahBlah");
        opportunity[1] = new MessageElement(new QName("Amount"), 111.11);
        opportunity[2] = new MessageElement(new QName("StageName"), "A stage name");
        opportunity[3] = new MessageElement(new QName("CloseDate"), new Date());

        SObject[] upserts = new SObject[1];
        SObject upsertOpportunity = new SObject();
        upsertOpportunity.setType("Opportunity");
        upsertOpportunity.set_any(opportunity);

        upserts[0] = upsertOpportunity;

        MuleMessage msgUpsert1 = client.send("vm://test.upsert", new DefaultMuleMessage(new Object[]{"Name",
            upserts, sessionId}, muleContext));

        assertNotNull(msgUpsert1);
        assertTrue(msgUpsert1.getPayload() instanceof UpsertResult[]);

        UpsertResult[] upResult1 = (UpsertResult[]) msgUpsert1.getPayload();
        assertTrue(upResult1.length == 1);
        assertTrue(upResult1[0].isSuccess());

        MessageElement[] opportunityUpdate = new MessageElement[4];

        opportunityUpdate[0] = new MessageElement(new QName("Name"), "BlahBlah");
        opportunityUpdate[1] = new MessageElement(new QName("Amount"), 777.58);
        opportunityUpdate[2] = new MessageElement(new QName("StageName"), "Updated");
        opportunityUpdate[3] = new MessageElement(new QName("CloseDate"), new Date());

        SObject[] upserts2 = new SObject[1];
        SObject upsertOpportunityUpdate = new SObject();
        upsertOpportunityUpdate.setType("Opportunity");
        upsertOpportunityUpdate.set_any(opportunityUpdate);

        upserts2[0] = upsertOpportunityUpdate;

        MuleMessage msgUpsert2 = client.send("vm://test.upsert", new DefaultMuleMessage(new Object[]{"Name",
            upserts2, sessionId}, muleContext));

        assertNotNull(msgUpsert2);
        assertTrue(msgUpsert2.getPayload() instanceof UpsertResult[]);

        UpsertResult[] upResult2 = (UpsertResult[]) msgUpsert2.getPayload();
        assertTrue(upResult2.length == 1);
        assertTrue(upResult2[0].isSuccess());

        String[] deleteIds = {upResult2[0].getId()};

        MuleMessage replyOfDelete = client.send("vm://test.delete", new DefaultMuleMessage(new Object[]{
            deleteIds, sessionId}, muleContext));

        assertNotNull(replyOfDelete);
        assertNotNull(replyOfDelete.getPayload());

        assertTrue(replyOfDelete.getPayload() instanceof DeleteResult[]);
        DeleteResult[] deleteResult = (DeleteResult[]) replyOfDelete.getPayload();

        assertTrue(deleteResult.length == 1);
        assertEquals(upResult2[0].getId(), deleteResult[0].getId());

    }

}
