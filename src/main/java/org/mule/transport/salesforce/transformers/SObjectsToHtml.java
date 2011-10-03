/*
 * $Id: SObjectsToHtml.java 1036 2010-11-24 08:47:10Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.transformers;

import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractTransformer;
import org.mule.util.FileUtils;

import com.sforce.soap.partner.sobject.SObject;

import java.io.File;
import java.io.IOException;

import org.apache.axis.message.MessageElement;

/*
 * Used in the http-example
 */
public class SObjectsToHtml extends AbstractTransformer
{

    private String htmlFormatter;

    public SObjectsToHtml()
    {
        registerSourceType(SObject[].class);
        setReturnClass(String.class);
    }

    @Override
    public void initialise() throws InitialisationException
    {
        super.initialise();
        try
        {
            htmlFormatter = FileUtils.readFileToString(new File("src/test/resources/example1/result.html"));
        }
        catch (IOException e)
        {
            new InitialisationException(e, this);
        }
    }

    @Override
    protected Object doTransform(Object src, String encoding) throws TransformerException
    {

        SObject[] contacts = (SObject[]) src;
        StringBuilder html = new StringBuilder("");
        for (SObject o : contacts)
        {
            MessageElement[] msgElement = o.get_any();
            // _any is an array of of org.apache.axis.message.MessageElement
            // therefore: "for all MessageElement(s) in each SObject o in contacts,
            // do:
            for (int i = 0; i < o.get_any().length; i++)
            {
                html.append("<br/>" + msgElement[i].getName() + " : " + msgElement[i].getValue() + "<br/>");
            }
        }

        return String.format(htmlFormatter, html.toString());

    }

}
