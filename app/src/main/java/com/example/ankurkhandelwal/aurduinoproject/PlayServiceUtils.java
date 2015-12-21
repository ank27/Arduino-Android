package com.example.ankurkhandelwal.aurduinoproject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayServiceUtils {
    Context context;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    String SENDER_ID = "607969547155";
    static final String TAG = "GCMDemo";
    GoogleCloudMessaging gcm;
    Activity activity;

    public PlayServiceUtils(Context context,Activity activity){
        this.context=context;
        this.activity=activity;
        this.gcm = GoogleCloudMessaging.getInstance(context);
    }

    public boolean IsInternetActive(){
        ConnectionDetector cd = new ConnectionDetector(context);
        return  cd.isConnectingToInternet();
    }
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    public void register_app_using_gcm(){
        if(checkPlayServices()){
            Common.regid = Common.prefs.getString("registration_id", "");
            if (Common.regid.equals("")) {
                register_in_background();
            }
        }
    }

    private void register_in_background() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    Common.regid = gcm.register(SENDER_ID);
                    int appVersion=getAppVersion(context);
                    msg = "Device registered, registration ID=" + Common.regid;
                    sendRegistrationIdToBackend(Common.regid,appVersion);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
            }
        }.execute(null, null, null);
    }

    private void sendRegistrationIdToBackend(String regid, int appVersion) {
        SharedPreferences.Editor  editor = Common.prefs.edit();
        editor.putString("registration_id", regid).apply();
        editor.putInt("app_version", appVersion).apply();
        try {
            String email=Common.get_preferred_email();
            int app_version=getAppVersion(context);
            URL url=new URL(Constant.SERVER_URL + "/MyApplication/app/register_app_id/");
            System.out.println("url " + url);
            HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            String jsonParam ="email=" + String.valueOf(email) +
                    "&app_id=" + String.valueOf(Common.regid)+
                    "&device_id=" + Common.get_device_id()+
                    "&app_version=" + String.valueOf(app_version);
            System.out.println("jsonparams " + jsonParam);
            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream ());
            dataOutputStream.writeBytes(jsonParam);
            dataOutputStream.flush();
            dataOutputStream.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}