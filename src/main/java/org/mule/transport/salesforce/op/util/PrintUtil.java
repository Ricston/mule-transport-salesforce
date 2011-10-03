/*
 * $Id: PrintUtil.java 1036 2010-11-24 08:47:10Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce.op.util;

import java.util.Calendar;

/**
 * A utility class containing printing methods.
 */
public class PrintUtil
{

    public static String printStrArray(String[] strArray)
    {
        String temp = "";

        for (String str : strArray)
        {
            temp += str + ", ";
        }

        return temp;
    }

    public static String printCalendar(Calendar c)
    {
        String str = "(dd/mm/yyyy) ";
        str += c.get(Calendar.DAY_OF_MONTH);
        str += "/" + c.get(Calendar.MONTH);
        str += "/" + c.get(Calendar.YEAR);

        return str;
    }

}
