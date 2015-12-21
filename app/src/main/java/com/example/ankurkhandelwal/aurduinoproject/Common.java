package com.example.ankurkhandelwal.aurduinoproject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Common extends Application {
    public static HashMap<String, String > light_list= new HashMap<String, String>();
    public static String temp;
    public static String light_intensity;
    public static String motion;
    public static String regid="";
    public static SharedPreferences prefs;
    public static SharedPreferences.Editor editor;
    public static Application activity;
    public static String[] email_array;
    @Override
    public void onCreate(){
        super.onCreate();
        activity=this;
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        editor=prefs.edit();
        List<String> emailList = getEmailList();
        email_array = emailList.toArray(new String[emailList.size()]);

    }

    private List<String> getEmailList() {
        List<String> lst = new ArrayList<String>();
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (Patterns.EMAIL_ADDRESS.matcher(account.name).matches()) {
                lst.add(account.name);
            }
        }
        return lst;
    }

    public static String get_device_id(){
        TelephonyManager manager=(TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getDeviceId();
    }
    public static String get_preferred_email() {
        return email_array[0];
    }
}
