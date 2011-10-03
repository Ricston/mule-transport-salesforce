/*
 * $Id: QueryInvocation.java 1036 2010-11-24 08:47:10Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.op.impl;

import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.mule.transport.salesforce.SalesforceSoapBindingKey;
import org.mule.transport.salesforce.op.base.AbstractInvocation;
import org.mule.transport.salesforce.op.util.SessionIdUtil;

import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.SoapBindingStub;
import com.sforce.soap.partner.sobject.SObject;

/**
 * QueryInvocation requires a MuleMessage with a payload which contains a String (the
 * query) and a String (the sessionId) as the first and second parameter
 * respectively.
 */
public class QueryInvocation extends AbstractInvocation
{

    public QueryInvocation(GenericKeyedObjectPool soapBindingPool, Object[] invocationParameters)
    {
        super(soapBindingPool, invocationParameters);
    }

    @Override
    public Object invoke() throws Exception
    {

        String queryString = (String) invocationParameters[0];
        String sessionId = (String) invocationParameters[1];

        if (SessionIdUtil.isAppended(sessionId)) sessionId = SessionIdUtil.unappendedSessionId(sessionId);

        SalesforceSoapBindingKey key = new SalesforceSoapBindingKey(sessionId);

        if (logger.isDebugEnabled()) logger.debug("Query=" + queryString + " Key=" + sessionId);

        SoapBindingStub sfdc = (SoapBindingStub) soapBindingPool.borrowObject(key);

        QueryResult queryResult = null;

        try
        {
            queryResult = sfdc.query(queryString);
            boolean done = false;
            logger.info("Query has " + queryResult.getSize() + " items");
            if (queryResult.getSize() > 0)
            {
                while (!done)
                {
                    if (queryResult.isDone())
                    {
                        done = true;
                    }
                    else
                    {
                        queryResult = sfdc.queryMore(queryResult.getQueryLocator());
                    }
                }
            }
            else
            {
                if (logger.isWarnEnabled())
                {
                    logger.warn("No Records were found");
                }
            }

            logger.info("Query succesfully executed");

        }
        finally
        {
            soapBindingPool.returnObject(key, sfdc);
        }

        SObject[] records = queryResult.getRecords();
        if (records == null)
        {
            records = new SObject[0];
        }

        return records;

    }

}
