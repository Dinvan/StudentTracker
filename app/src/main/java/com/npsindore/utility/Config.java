package com.npsindore.utility;

/**
 * Created by ravib on 05/11/2017.
 */
public class Config {

    //Set true for show logcat
    public static boolean DEBUG = true;
    public static final String DEVICE_TYPE_ID = "android";
    public static String DEVICETOKEN_GCMID = "";
    public static String USER_TYPE_ID = "1";
    public static String NOTIFICATION_CHANNEL_ID = "NPS_Notification_1";
    /**
     * Application mode development-devel or production-prod
     */
    public static final String ENVIRONMENT = "devel";

    public static final int TYPE_HOMEWORK = 1;
    public static final int TYPE_ANNOUNCEMENT = 2;
    public static final int LOGIN_TYPE_TEACHER = 1;
    public static final int LOGIN_TYPE_PARENT = 2;
    public static final String LOGIN_TYPE = "login_type";

    /**
     * Message to show when request is left without completion
     */
    public static final String REQUEST_STOPPED_MSG = "Activity is not running, quitting request";

    /**
     * \
     * Base API URL
     */

   //   public static String BASE_URL = "http://ibsglobal.raynapps.com/";
  //  public static String BASE_URL = "http://webapp.rayninfolabs.com/";
    public static String BASE_URL = "http://npsindore.raynapps.com/";
    /**
     * Base API URL
     */
    public static String SERVICE_URL;

    /**
     * Base Upload data URL
     */
    public static String UPLOAD_URL = "";

    /**
     * Base URL for getting image from server, in case image path only contains
     * name
     */
    public static String IMAGE_URL;

    static {
        if ("devel".equalsIgnoreCase(ENVIRONMENT)) {
            SERVICE_URL = BASE_URL;
        } else if ("prod".equalsIgnoreCase(ENVIRONMENT)) {

        } else {
            SERVICE_URL = "SERVICE_URL not set";
            UPLOAD_URL = "UPLOAD_URL not set";
            IMAGE_URL = "IMAGE_URL not set";
        }
    }

    /**
     * Error shown when no response received or unrecognized response received
     */
    public static final String DEFAULT_SERVICE_ERROR = "Network seems to be busy right now, please try again later.";//"Can not connect to server!";

}
