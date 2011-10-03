/*
 * $Id: SalesforceSoapBindingFactory.java 1036 2010-11-24 08:47:10Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce;

import com.sforce.soap.partner.SoapBindingStub;

import org.apache.commons.pool.KeyedPoolableObjectFactory;

public class SalesforceSoapBindingFactory implements KeyedPoolableObjectFactory
{

    @Override
    public void activateObject(Object arg0, Object arg1) throws Exception
    {

    }

    @Override
    public void destroyObject(Object key, Object object) throws Exception
    {
        SoapBindingStub sfdc = (SoapBindingStub) object;
        sfdc.logout();
    }

    @Override
    public Object makeObject(Object key) throws Exception
    {
        SalesforceSoapBindingKey sfdcKey = (SalesforceSoapBindingKey) key;
        return sfdcKey.getSfdc();
    }

    @Override
    public void passivateObject(Object arg0, Object arg1) throws Exception
    {

    }

    @Override
    public boolean validateObject(Object key, Object object)
    {
        SoapBindingStub sfdc = (SoapBindingStub) object;

        return true;
    }

}
