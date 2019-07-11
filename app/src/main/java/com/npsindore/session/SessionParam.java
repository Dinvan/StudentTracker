package com.npsindore.session;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SessionParam implements Serializable {

    public static String PREFRENCE_NAME = "Profile_Prefrence";

    public String session_key;
    public String user_guid;
    public String email;
    public String name;
    public String mobile;
    public String default_profile_type;
    public String last_login_at;
    public String active_profile_type;
    public int loginType;

    public String profile_image;

    //Please verify your mobile number first.
    public void persistData(Context activity) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("user_guid", user_guid);
        editor.putString("email", email);
        editor.putString("name", name);
        editor.putString("mobile", mobile);
        editor.putString("default_profile_type", default_profile_type);
        editor.putString("last_login_at", last_login_at);
        editor.putString("active_profile_type", active_profile_type);
        editor.putInt("login_type", loginType);
        editor.putString("profile_image", profile_image);
        editor.commit();
    }

    public void persistData(Context activity, String key, String value) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public SessionParam(Context context) {
        if (null != context) {
            SharedPreferences prefs = context.getSharedPreferences(PREFRENCE_NAME,
                    Context.MODE_PRIVATE);
            user_guid = prefs.getString("user_guid", "");
            session_key = prefs.getString("session_key", "");
            email = prefs.getString("email", "");
            name = prefs.getString("name", "");
            mobile = prefs.getString("mobile", "");
            default_profile_type = prefs.getString("default_profile_type", "");
            last_login_at = prefs.getString("last_login_at", "");
            active_profile_type = prefs.getString("active_profile_type", "");
            profile_image = prefs.getString("profile_image", "");
            loginType = prefs.getInt("login_type", 0);
        }
    }

    public SessionParam(JSONObject jsonObject) {
        if (null != jsonObject) {
            user_guid = jsonObject.optString("user_guid", "");
            session_key = jsonObject.optString("session_key", "");
            if (session_key.equals("null")) {
                session_key = "";
            }
            email = jsonObject.optString("email", "");
            if (email.equals("null")) {
                email = "";
            }

            name = jsonObject.optString("name", "");
            if (name.equals("null")) {
                name = "";
            }
            mobile = jsonObject.optString("mobile", "");
            if (mobile.equals("null")) {
                mobile = "";
            }
            default_profile_type = jsonObject.optString("default_profile_type", "");
            last_login_at = jsonObject.optString("last_login_at", "");
            active_profile_type = jsonObject.optString("active_profile_type", "");
            loginType = jsonObject.optInt("login_type");
            profile_image = jsonObject.optString("profile_image");
        }
    }

    public static void setSaveSessionKey(Context activity,
                                  String session_key) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("session_key", session_key);

        editor.commit();
    }

    public static void deletePrefrenceData(Context activity) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME,
                Context.MODE_PRIVATE);
        prefs.edit().clear().commit();
    }

    public static String getSessionKey(Context activity) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME,
                Context.MODE_PRIVATE);
        return prefs.getString("session_key", "");
    }

    public static void setPrefData(Context activity,
                                   String key, String value) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public static String getPrefData(Context activity, String key) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME,
                Context.MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    private static String key1 = "noti_type_homework";
    private static String key2 = "noti_type_announcement";
    private static String key3 = "noti_type_attendance";
    public static void setNotificationPref(Context activity,
                                   int type,String studentId) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if(type==1){
            String mKey = key1+"_" +studentId;
            int count = prefs.getInt(mKey,0);
            count++;
            editor.putInt(mKey, count);

        }
        else if(type==2){
            String mKey = key2+"_" +studentId;
            int count = prefs.getInt(mKey,0);
            count++;
            editor.putInt(mKey, count);
        }
        else if(type==3){
            String mKey = key3+"_" +studentId;
            int count = prefs.getInt(mKey,0);
            count++;
            editor.putInt(mKey, count);
        }
        editor.commit();
    }

    public static Map<String,Integer> getNotificationCount(Context ctx,String studentId){

        Map<String,Integer> counts = new HashMap<>();
        SharedPreferences prefs = ctx.getSharedPreferences(PREFRENCE_NAME,
                Context.MODE_PRIVATE);
        String mKey1 = key1+"_" +studentId;
        String mKey2 = key2+"_" +studentId;
        String mKey3 = key3+"_" +studentId;
        counts.put("homework",prefs.getInt(mKey1, 0));
        counts.put("announcement",prefs.getInt(mKey2, 0));
        counts.put("attendance",prefs.getInt(mKey3, 0));
        return counts;
    }


    public static void resetNotificationPref(Context activity,int type,String studentId) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME,
                Context.MODE_PRIVATE);
        String mKey1 = key1+"_" +studentId;
        String mKey2 = key2+"_" +studentId;
        String mKey3 = key3+"_" +studentId;
        SharedPreferences.Editor editor = prefs.edit();
        if(type==1){
            editor.putInt(mKey1, 0);
        }
        else if(type==2){
            editor.putInt(mKey2, 0);
        }
        else if(type==3){
            editor.putInt(mKey3, 0);
        }
        editor.commit();
    }
}