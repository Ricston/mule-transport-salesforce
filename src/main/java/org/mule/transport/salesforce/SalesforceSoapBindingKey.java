/*
 * $Id: SalesforceSoapBindingKey.java 1036 2010-11-24 08:47:10Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce;

import com.sforce.soap.partner.SoapBindingStub;

public class SalesforceSoapBindingKey
{
    private String sessionId;

    private SoapBindingStub sfdc;

    public SalesforceSoapBindingKey(String sessionId, SoapBindingStub sfdc)
    {
        super();
        this.sessionId = sessionId;
        this.sfdc = sfdc;
    }

    public SalesforceSoapBindingKey(String sessionId)
    {
        super();
        this.sessionId = sessionId;
        this.sfdc = null;
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public SoapBindingStub getSfdc()
    {
        return sfdc;
    }

    public void setSfdc(SoapBindingStub sfdc)
    {
        this.sfdc = sfdc;
    }

    @Override
    public int hashCode()
    {
        return sessionId.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        SalesforceSoapBindingKey key = (SalesforceSoapBindingKey) obj;

        return this.sessionId.equals(key.getSessionId());
    }

}
