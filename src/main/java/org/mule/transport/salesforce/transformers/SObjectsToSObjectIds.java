/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.transformers;

import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractTransformer;

import com.sforce.soap.partner.sobject.SObject;

public class SObjectsToSObjectIds extends AbstractTransformer
{

    public SObjectsToSObjectIds()
    {
        registerSourceType(SObject[].class);
        setReturnClass(String[].class);
    }

    @Override
    protected Object doTransform(Object src, String encoding) throws TransformerException
    {

        // http://www.mulesoft.org/documentation/display/SFDC/More+Examples

        SObject[] so = (SObject[]) src;
        String[] deleteIds = new String[so.length];

        for (int i = 0; i < so.length; i++)
        {
            deleteIds[i] = so[i].getId();
        }

        return deleteIds;
    }

}
