package com.elecom.smartcarrier.common;

public class DefineValue {
    public final static String PREFERENCE_MAIN_INIT_STATE = "main_init_state";
    public final static String PREFERENCE_DEFAULT_CARRIER = "default_carrier";
    public final static String PREFERENCE_DEFAULT_USER = "default_user";
    public final static String PREFERENCE_OPEN_STATE_VALUE = "open_state_value";
    public final static String PREFERENCE_LOCK_STATE_VALUE = "lock_state_value";


    public static final int INIT_STATE_GUIDE = 0;
    public static final int INIT_STATE_PERMISSION = 1;
    public static final int INIT_STATE_FINISH = 2;

    public static final int REQ_GUIDE = 1000;
    public static final int REQ_PERMISSION = 1001;
    public static final int REQ_SIGN_IN = 1002;
    public static final int REQ_BLUETOOTH = 100;
    //public static final int REQ_ = 1000;


    public static final String FA_USER_PROPERTY_PERMISSION = "userproperty_permission";
    public static final String FA_USER_PROPERTY_PERMISSION_OK = "1";
    public static final String FA_USER_PROPERTY_PERMISSION_NO = "0";
}
