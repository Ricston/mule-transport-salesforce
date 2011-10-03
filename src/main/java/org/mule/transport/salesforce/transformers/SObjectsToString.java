/*
 * $Id: SObjectsToString.java 956 2010-09-09 11:52:25Z jcalleja $
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

import org.apache.axis.message.MessageElement;

/*
 * Used in the http-example
 */
public class SObjectsToString extends AbstractTransformer
{

    public SObjectsToString()
    {
        registerSourceType(SObject[].class);
        setReturnClass(String.class);
    }

    @Override
    protected Object doTransform(Object src, String encoding) throws TransformerException
    {

        SObject[] contacts = (SObject[]) src;
        String output = "";
        for (SObject o : contacts)
        {

            MessageElement[] msgElement = o.get_any();
            // _any is an array of of org.apache.axis.message.MessageElement
            // therefore: "for all MessageElement(s) in each SObject o in contacts,
            // do:
            for (int i = 0; i < o.get_any().length; i++)
            {
                output += "<br/>" + msgElement[i].getName() + " : " + msgElement[i].getValue() + "<br/>";
            }

        }

        return output;

    }

}
