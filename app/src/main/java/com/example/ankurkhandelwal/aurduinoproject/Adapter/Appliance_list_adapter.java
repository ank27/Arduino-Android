package com.example.ankurkhandelwal.aurduinoproject.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.Layout;
import android.text.style.IconMarginSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ankurkhandelwal.aurduinoproject.Constant;
import com.example.ankurkhandelwal.aurduinoproject.Main;
import com.example.ankurkhandelwal.aurduinoproject.PlayServiceUtils;
import com.example.ankurkhandelwal.aurduinoproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;


public class Appliance_list_adapter extends BaseAdapter {
    private HashMap<String, String> appliance_list = new HashMap<String, String>();
    Context context;
    Activity activity;

    public Appliance_list_adapter(Context context,Activity activity ,HashMap<String, String> appliance_list){
        this.context=context;
        this.appliance_list=appliance_list;
       this.activity =activity;
    }
    @Override
    public int getCount() {
        if(appliance_list!=null){
            return appliance_list.size();
        }else{
            return 4;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_appliance, null);
            holder = new ViewHolder();
            holder.appliance_text=(TextView)convertView.findViewById(R.id.title);
            holder.appliance_image=(ImageView)convertView.findViewById(R.id.icon);
            holder.appliance_switch=(SwitchCompat)convertView.findViewById(R.id.switch_compat);
            if (appliance_list!=null) {
                holder.appliance_text.setText("Device " + (position + 1));
                switch (position){
                    case 0:
                        holder.appliance_image.setImageResource(R.drawable.ic_fan);
                        if(appliance_list.get("light1").equals("1")){
                            holder.appliance_switch.setChecked(true);
                            holder.appliance_image.setColorFilter(R.color.colorPrimary);
                        }else{
                            holder.appliance_switch.setChecked(false);
                        }
                        break;
                    case 1:
                        holder.appliance_image.setImageResource(R.drawable.ic_light);
                        if(appliance_list.get("light2").equals("1")){
                            holder.appliance_switch.setChecked(true);
                            holder.appliance_image.setColorFilter(R.color.colorPrimary);
                        }else{
                            holder.appliance_switch.setChecked(false);
                        }
                        break;

                    case 2:
                        holder.appliance_image.setImageResource(R.drawable.ic_lamp);
                        if(appliance_list.get("light3").equals("1")){
                            holder.appliance_switch.setChecked(true);
                            holder.appliance_image.setColorFilter(R.color.colorPrimary);
                        }else{
                            holder.appliance_switch.setChecked(false);
                        }
                        break;

                    case 3:
                        holder.appliance_image.setImageResource(R.drawable.ic_light);
                        if(appliance_list.get("light4").equals("1")){
                            holder.appliance_switch.setChecked(true);
                            holder.appliance_image.setColorFilter(R.color.colorPrimary);
                        }else{
                            holder.appliance_switch.setChecked(false);
                        }
                        break;
                    default:
                        break;
                }

            }else{
                switch (position){
                    case 0:
                        holder.appliance_text.setText("Fan");
                        holder.appliance_image.setImageResource(R.drawable.ic_fan);
                        holder.appliance_switch.setChecked(false);
                        holder.appliance_image.setColorFilter(R.color.colorPrimary);
                        break;
                    case 1:
                        holder.appliance_text.setText("Light");
                        holder.appliance_image.setImageResource(R.drawable.ic_light);
                        holder.appliance_switch.setChecked(false);
                        holder.appliance_image.setColorFilter(R.color.colorPrimary);
                        break;

                    case 2:
                        holder.appliance_text.setText("Table Lamp");
                        holder.appliance_image.setImageResource(R.drawable.ic_lamp);
                        holder.appliance_switch.setChecked(false);
                        holder.appliance_image.setColorFilter(R.color.colorPrimary);
                        break;

                    case 3:
                        holder.appliance_text.setText("Light");
                        holder.appliance_image.setImageResource(R.drawable.ic_light);
                        holder.appliance_switch.setChecked(false);
                        holder.appliance_image.setColorFilter(R.color.colorPrimary);
                        break;
                    default:
                        break;
                }
            }
            holder.appliance_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        send_info(position, "1");
                        holder.appliance_image.setColorFilter(R.color.colorPrimary);
                    }else {
                        send_info(position,"0");
                    }
                }
            });
        }
        return convertView;
    }

    private void send_info(final int position, final String check) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(Constant.SERVER_URL + "/MyApplication/app/send_values/");
                    System.out.println("url " + url);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("charset", "utf-8");
                    DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream ());
                    String jsonParam = "light=" + URLEncoder.encode(String.valueOf(position + 1), "UTF-8") +
                            "&state=" + URLEncoder.encode(check, "UTF-8");
                    dataOutputStream.writeBytes(jsonParam);
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    int responseCode = urlConnection.getResponseCode();
                    return String.valueOf(responseCode);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(String result){
                if(result.equals("200")){
                    Toast.makeText(activity.getApplicationContext(),"Device "+(position+1) +" state Chenged", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(activity.getApplicationContext(),"Error in Network Connection, Try Again Later", Toast.LENGTH_LONG).show();
                }
            }
        }

        PlayServiceUtils playService = new PlayServiceUtils(context, activity);
        if (playService.IsInternetActive()) {
            if(appliance_list!=null){
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
                sendPostReqAsyncTask.execute();
            }else {
                Toast.makeText(activity.getApplicationContext(), "No Server Response", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity.getApplicationContext(), "No Active Internet", Toast.LENGTH_SHORT).show();
        }

    }

    public class ViewHolder {
        ImageView appliance_image;
        TextView appliance_text;
        SwitchCompat appliance_switch;
    }
}
