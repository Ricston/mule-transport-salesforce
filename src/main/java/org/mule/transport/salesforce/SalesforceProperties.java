/*
 * $Id: SalesforceProperties.java 1036 2010-11-24 08:47:10Z jcalleja $
 * --------------------------------------------------------------------------------------
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.salesforce;

public class SalesforceProperties
{
    public static final String SALESFORCE_LOGIN = "salesforce_login";
    public static final String SALESFORCE_PASSWORD = "salesforce_password";
    public static final String SALESFORCE_KEY = "salesforce_key";
    public static final String SALESFORCE_SERVERURL = "salesforce_serverurl";

    public static final String SALESFORCE_OPERATION = "salesforce_operation";

    // -------------------

    public static final String SALESFORCE_OPERATION_LOGIN = "login";
    public static final String SALESFORCE_OPERATION_DESCRIBE_SOBJECTS = "describeSObjects";
    public static final String SALESFORCE_OPERATION_DESCRIBE_GLOBAL = "describeGlobal";
    public static final String SALESFORCE_OPERATION_DESCRIBE_LAYOUT = "describeLayout";
    public static final String SALESFORCE_OPERATION_DESCRIBE_SOFTPHONE_LAYOUT = "describeSoftphoneLayout";
    public static final String SALESFORCE_OPERATION_DESCRIBE_TABS = "describeTabs";
    public static final String SALESFORCE_OPERATION_CREATE = "create";
    public static final String SALESFORCE_OPERATION_UPDATE = "update";
    public static final String SALESFORCE_OPERATION_UPSERT = "upsert";
    public static final String SALESFORCE_OPERATION_MERGE = "merge";
    public static final String SALESFORCE_OPERATION_DELETE = "delete";
    public static final String SALESFORCE_OPERATION_UNDELETE = "undelete";
    public static final String SALESFORCE_OPERATION_EMPTY_RECYCLE_BIN = "emptyRecycleBin";
    public static final String SALESFORCE_OPERATION_RETRIEVE = "retrieve";
    public static final String SALESFORCE_OPERATION_PROCESS = "process";
    public static final String SALESFORCE_OPERATION_CONVERT_LEAD = "convertLead";
    public static final String SALESFORCE_OPERATION_LOGOUT = "logout";
    public static final String SALESFORCE_OPERATION_INVALIDATE_SESSIONS = "invalidateSessions";
    public static final String SALESFORCE_OPERATION_GET_DELETED = "getDeleted";
    public static final String SALESFORCE_OPERATION_GET_UPDATED = "getUpdated";
    public static final String SALESFORCE_OPERATION_QUERY = "query";
    public static final String SALESFORCE_OPERATION_QUERY_ALL = "queryAll";
    public static final String SALESFORCE_OPERATION_QUERY_MORE = "queryMore";
    public static final String SALESFORCE_OPERATION_SEARCH = "search";
    public static final String SALESFORCE_OPERATION_GET_SERVER_TIMESTAMP = "getServerTimestamp";
    public static final String SALESFORCE_OPERATION_SET_PASSWORD = "setPassword";
    public static final String SALESFORCE_OPERATION_RESET_PASSWORD = "resetPassword";
    public static final String SALESFORCE_OPERATION_GET_USER_INFO = "getUserInfo";
    public static final String SALESFORCE_OPERATION_SEND_EMAIL = "sendEmail";

    // -------------------
    public static final String[] ALL_SALESFORCE_OPERATIONS = {SALESFORCE_OPERATION_LOGIN,
        SALESFORCE_OPERATION_DESCRIBE_SOBJECTS, SALESFORCE_OPERATION_DESCRIBE_GLOBAL,
        SALESFORCE_OPERATION_DESCRIBE_LAYOUT, SALESFORCE_OPERATION_DESCRIBE_SOFTPHONE_LAYOUT,
        SALESFORCE_OPERATION_DESCRIBE_TABS, SALESFORCE_OPERATION_CREATE, SALESFORCE_OPERATION_UPDATE,
        SALESFORCE_OPERATION_UPSERT, SALESFORCE_OPERATION_MERGE, SALESFORCE_OPERATION_DELETE,
        SALESFORCE_OPERATION_UNDELETE, SALESFORCE_OPERATION_EMPTY_RECYCLE_BIN, SALESFORCE_OPERATION_RETRIEVE,
        SALESFORCE_OPERATION_PROCESS, SALESFORCE_OPERATION_CONVERT_LEAD, SALESFORCE_OPERATION_LOGOUT,
        SALESFORCE_OPERATION_INVALIDATE_SESSIONS, SALESFORCE_OPERATION_GET_DELETED,
        SALESFORCE_OPERATION_GET_UPDATED, SALESFORCE_OPERATION_QUERY, SALESFORCE_OPERATION_QUERY_ALL,
        SALESFORCE_OPERATION_SEARCH, SALESFORCE_OPERATION_GET_SERVER_TIMESTAMP,
        SALESFORCE_OPERATION_SET_PASSWORD, SALESFORCE_OPERATION_RESET_PASSWORD,
        SALESFORCE_OPERATION_GET_USER_INFO, SALESFORCE_OPERATION_SEND_EMAIL};
    // -------------------

    public static final String SALESFORCE_OPERATION_LOGIN_QUERY = "loginQuery";

    // -------------------

    public static final String SALESFORCE_OPERATION_QUERY_CASE = "queryCase";
    public static final String SALESFORCE_SUCCESSFULL_LOGIN = "SUCCESSFUL_LOGIN: ";
    public static final String SALESFORCE_SUCCESSFULL_LOGOUT = "SUCCESSFUL_LOGOUT: ";
}
