/*
 * $Id: StreamToUserInput.java 1036 2010-11-24 08:47:10Z jcalleja $
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

import java.io.IOException;

import org.apache.commons.httpclient.ContentLengthInputStream;

/*
 * Used in the http-example
 */
public class StreamToUserInput extends AbstractTransformer
{

    public StreamToUserInput()
    {
        registerSourceType(ContentLengthInputStream.class);
        setReturnClass(String[].class);
    }

    @Override
    protected Object doTransform(Object src, String encoding) throws TransformerException
    {

        ContentLengthInputStream inputStream = (ContentLengthInputStream) src;

        String http = "";

        while (true)
        {
            int x = -1;
            try
            {
                x = inputStream.read();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            if (x != -1)
                http += String.valueOf((char) x);
            else
                break;
        }

        String[] parsed = new String[3];
        parsed[0] = http.substring(http.indexOf("username=") + 9, http.indexOf("&password"));
        parsed[0] = parsed[0].replace("%40", "@");
        parsed[1] = http.substring(http.indexOf("password=") + 9, http.indexOf("&contactname"));
        parsed[2] = http.substring(http.indexOf("contactname=") + 12, http.length());

        parsed[2] = "SELECT Contact.Firstname, Contact.Lastname, Contact.Department, Contact.Phone, "
                    + "Contact.MobilePhone, Contact.Email, Contact.Birthdate FROM Contact"
                    + " WHERE LastName = '" + parsed[2] + "'";

        return parsed;

    }

}
