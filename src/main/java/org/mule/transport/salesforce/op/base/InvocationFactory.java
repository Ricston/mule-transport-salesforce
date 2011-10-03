/*
 * $Id: InvocationFactory.java 956 2010-09-09 11:52:25Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.op.base;

import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.mule.transport.salesforce.SalesforceProperties;
import org.mule.transport.salesforce.op.impl.ConvertLeadInvocation;
import org.mule.transport.salesforce.op.impl.CreateInvocation;
import org.mule.transport.salesforce.op.impl.DeleteInvocation;
import org.mule.transport.salesforce.op.impl.DescribeGlobalInvocation;
import org.mule.transport.salesforce.op.impl.DescribeLayoutInvocation;
import org.mule.transport.salesforce.op.impl.DescribeSObjectsInvocation;
import org.mule.transport.salesforce.op.impl.DescribeSoftphoneLayoutInvocation;
import org.mule.transport.salesforce.op.impl.DescribeTabsInvocation;
import org.mule.transport.salesforce.op.impl.EmptyRecycleBinInvocation;
import org.mule.transport.salesforce.op.impl.GetDeletedInvocation;
import org.mule.transport.salesforce.op.impl.GetServerTimestampInvocation;
import org.mule.transport.salesforce.op.impl.GetUpdatedInvocation;
import org.mule.transport.salesforce.op.impl.GetUserInfoInvocation;
import org.mule.transport.salesforce.op.impl.InvalidateSessionsInvocation;
import org.mule.transport.salesforce.op.impl.LoginInvocation;
import org.mule.transport.salesforce.op.impl.LogoutInvocation;
import org.mule.transport.salesforce.op.impl.MergeInvocation;
import org.mule.transport.salesforce.op.impl.ProcessInvocation;
import org.mule.transport.salesforce.op.impl.QueryAllInvocation;
import org.mule.transport.salesforce.op.impl.QueryInvocation;
import org.mule.transport.salesforce.op.impl.ResetPasswordInvocation;
import org.mule.transport.salesforce.op.impl.RetrieveInvocation;
import org.mule.transport.salesforce.op.impl.SearchInvocation;
import org.mule.transport.salesforce.op.impl.SendEmailInvocation;
import org.mule.transport.salesforce.op.impl.SetPasswordInvocation;
import org.mule.transport.salesforce.op.impl.UndeleteInvocation;
import org.mule.transport.salesforce.op.impl.UpdateInvocation;
import org.mule.transport.salesforce.op.impl.UpsertInvocation;

public class InvocationFactory
{

    public static AbstractInvocation createInvocation(String operation,
                                                      GenericKeyedObjectPool soapBindingPool,
                                                      Object[] parameters)
    {

        if (null == operation)
        {
            return null;
        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_LOGIN))
        {
            return new LoginInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_DESCRIBE_SOBJECTS))
        {
            return new DescribeSObjectsInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_DESCRIBE_GLOBAL))
        {
            return new DescribeGlobalInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_DESCRIBE_LAYOUT))
        {
            return new DescribeLayoutInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_DESCRIBE_SOFTPHONE_LAYOUT))
        {
            return new DescribeSoftphoneLayoutInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_DESCRIBE_TABS))
        {
            return new DescribeTabsInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_CREATE))
        {
            return new CreateInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_UPDATE))
        {
            return new UpdateInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_UPSERT))
        {
            return new UpsertInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_MERGE))
        {
            return new MergeInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_DELETE))
        {
            return new DeleteInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_UNDELETE))
        {
            return new UndeleteInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_EMPTY_RECYCLE_BIN))
        {
            return new EmptyRecycleBinInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_RETRIEVE))
        {
            return new RetrieveInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_PROCESS))
        {
            return new ProcessInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_CONVERT_LEAD))
        {
            return new ConvertLeadInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_LOGOUT))
        {
            return new LogoutInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_INVALIDATE_SESSIONS))
        {
            return new InvalidateSessionsInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_GET_DELETED))
        {
            return new GetDeletedInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_GET_UPDATED))
        {
            return new GetUpdatedInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_QUERY))
        {
            return new QueryInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_QUERY_ALL))
        {
            return new QueryAllInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_SEARCH))
        {
            return new SearchInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_GET_SERVER_TIMESTAMP))
        {
            return new GetServerTimestampInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_SET_PASSWORD))
        {
            return new SetPasswordInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_RESET_PASSWORD))
        {
            return new ResetPasswordInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_GET_USER_INFO))
        {
            return new GetUserInfoInvocation(soapBindingPool, parameters);

        }
        else if (operation.equals(SalesforceProperties.SALESFORCE_OPERATION_SEND_EMAIL))
        {
            return new SendEmailInvocation(soapBindingPool, parameters);

        }

        return null;

    }

}
