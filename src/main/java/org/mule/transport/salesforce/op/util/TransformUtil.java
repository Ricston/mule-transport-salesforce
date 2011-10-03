/*
 * $Id: TransformUtil.java 956 2010-09-09 11:52:25Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.op.util;

import com.sforce.soap.partner.sobject.SObject;

public class TransformUtil
{

    public static String[] getIds(Object src)
    {
        SObject[] so = (SObject[]) src;
        String[] ids = new String[so.length];

        for (int i = 0; i < so.length; i++)
        {
            ids[i] = so[i].getId();
        }

        return ids;
    }

}
