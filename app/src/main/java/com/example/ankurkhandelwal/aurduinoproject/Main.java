package com.example.ankurkhandelwal.aurduinoproject;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ankurkhandelwal.aurduinoproject.Adapter.Appliance_list_adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class Main extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static Activity activity;
    TextView city_text,time_text,appliance_text,light_intensity_text,temp_value_text;
    Animation fade_in;
    LinearLayout temp_value_layout, light_value_layout,fab_layout;
    FloatingActionButton fab_button;
    RelativeLayout main,light_layout,temp_layout,main_layout,appliance_layout,no_appliances;
    public static ListView list_appliances;
    HashMap<String, String> appliance_list;
    Appliance_list_adapter appliance_list_adapter;
    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    CoordinatorLayout coordinate_layout;
    CountDownTimer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);
        time_text=(TextView) findViewById(R.id.time_text);
        city_text=(TextView) findViewById(R.id.city_text);
        light_intensity_text=(TextView) findViewById(R.id.light_intensity_text);
        appliance_text=(TextView) findViewById(R.id.appliance_text);
        fade_in=AnimationUtils.loadAnimation(this,R.anim.fade_in);
        temp_value_layout=(LinearLayout) findViewById(R.id.temp_value_layout);
        light_value_layout=(LinearLayout) findViewById(R.id.light_value_layout);
        temp_value_text=(TextView) findViewById(R.id.temp_value_text);
        fab_button=(FloatingActionButton) findViewById(R.id.fab_button);
        fab_layout=(LinearLayout) findViewById(R.id.fab_layout);
        main=(RelativeLayout) findViewById(R.id.main);
        light_layout=(RelativeLayout)findViewById(R.id.light_layout);
        temp_layout=(RelativeLayout) findViewById(R.id.temp_layout);
        appliance_layout=(RelativeLayout) findViewById(R.id.appliance_layout);
        main_layout=(RelativeLayout) findViewById(R.id.main_layout);
        appliance_layout.setVisibility(View.INVISIBLE);
        list_appliances=(ListView) findViewById(R.id.list_appliances);
        no_appliances=(RelativeLayout) findViewById(R.id.no_appliances);
        coordinate_layout=(CoordinatorLayout) findViewById(R.id.coordinate_layout);
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(R.color.navigationBarColor,R.color.windowBackground,R.color.colorPrimary,R.color.navSelectedColor);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);

        activity=this;
        toolbar = (Toolbar) findViewById(R.id.toolbar_appliance);
        toolbar.setTitle("My Appliances");
        toolbar.setTitleTextColor(Color.parseColor("#555555"));
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_action_back_black));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appliance_layout.setVisibility(View.GONE);
                main_layout.setVisibility(View.VISIBLE);
            }
        });

        fab_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appliance_layout.setVisibility(View.VISIBLE);
                float finalRadius = (float) Math.hypot(appliance_layout.getWidth(), appliance_layout.getHeight());
                Animator anim = ViewAnimationUtils.createCircularReveal(appliance_layout, 390, 1000, 40, finalRadius);
                anim.setInterpolator(new LinearInterpolator());
                anim.setDuration(800);
                anim.start();
                main_layout.setVisibility(View.GONE);
                appliance_layout.setVisibility(View.VISIBLE);
                set_values();
            }
        });

//        fab_button.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
////                int cx = (int) ((v.getLeft() + v.getRight())/2);
////                int cy = (int) ((v.getTop() + v.getBottom())/2);
//                appliance_layout.setVisibility(View.VISIBLE);
//                float finalRadius = (float) Math.hypot(appliance_layout.getWidth(), appliance_layout.getHeight());
////                System.out.println("values" + cx + " " + cy + " " + finalRadius);
//                Animator anim = ViewAnimationUtils.createCircularReveal(appliance_layout, 390, 1000, 40, finalRadius);
////                Animator anim=ViewAnimationUtils.createCircularReveal(appliance_layout,cx,cy,0,finalRadius);
//                anim.setInterpolator(new LinearInterpolator());
//                anim.setDuration(800);
//                anim.start();
//                main_layout.setVisibility(View.GONE);
//                appliance_layout.setVisibility(View.VISIBLE);
//                set_values();
//                return false;
//            }
//        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {}
        }, 700);

        temp_layout.setAnimation(fade_in);
        light_layout.setAnimation(fade_in);

        ObjectAnimator slideup =ObjectAnimator.ofFloat(fab_layout, View.TRANSLATION_Y, 1280,0);
        slideup.setDuration(800);
        slideup.setRepeatMode(ValueAnimator.RESTART);
        slideup.setInterpolator(new LinearInterpolator());
        slideup.start();
        appliance_text.setAnimation(fade_in);

//        Calendar cal = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
//        time_text.setText(sdf.format(cal.getTime())+"");

        timer = new CountDownTimer(1000000000, 1000) {

            public void onTick(long millisUntilFinished) {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
                time_text.setText(sdf.format(cal.getTime())+"");
            }
            public void onFinish() {

            }
        };
        timer.start();

        get_data();
    }


    @Override
    public void onBackPressed(){
        if(appliance_layout.getVisibility()==View.VISIBLE){
            int cx=toolbar.getRight();
            int cy=(toolbar.getTop()+toolbar.getBottom())/2;
            Animator anim = ViewAnimationUtils.createCircularReveal(appliance_layout, cx, cy, 1920, 0);
            anim.setInterpolator(new LinearInterpolator());
            anim.setDuration(800);
            anim.start();
            appliance_layout.setVisibility(View.GONE);
            main_layout.setVisibility(View.VISIBLE);

//            appliance_layout.setVisibility(View.GONE);
//            main_layout.setVisibility(View.VISIBLE);
        }else {
            super.onBackPressed();
        }
    }

    private void get_data() {
        PlayServiceUtils playService = new  PlayServiceUtils(getApplicationContext(), this);
        if (playService.IsInternetActive()) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
            sendPostReqAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "No Active Internet", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
                swipeRefreshLayout.setRefreshing(true);
                get_data();
            }
        },10000);
    }

    public class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
        BufferedReader bufferedReader;
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(Constant.SERVER_URL + "/MyApplication/app/get_data");
                System.out.println("url " + url);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(10000);
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line=null;
                while ((line = bufferedReader.readLine()) != null){
                    response.append(line);
                }
                return response.toString();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            finally {
                if(bufferedReader!=null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            swipeRefreshLayout.setRefreshing(false);
            if(result!=null){
                System.out.println("result "+result);
                try {
                    JSONObject jsonObject= new JSONObject(result);
                    Common.temp= jsonObject.getString("temp");
                    Common.light_intensity =jsonObject.getString("light_intensity");
                    Common.motion= jsonObject.getString("motion_value");
                    JSONArray light_array = jsonObject.getJSONArray("led_state");
                    for(int i=0;i<light_array.length();i++){
                        JSONObject light_object = light_array.getJSONObject(i);
                        String light1 =light_object.getString("light1");
                        String light2 =light_object.getString("light2");
                        String light3 =light_object.getString("light3");
                        String light4 =light_object.getString("light4");
                        Common.light_list.put("light1",light1);
                        Common.light_list.put("light2",light2);
                        Common.light_list.put("light3",light3);
                        Common.light_list.put("light4",light4);
                    }
                    temp_value_text.setText(Common.temp);
                    light_intensity_text.setText(Common.light_intensity);
                    appliance_list=Common.light_list;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else{
                Snackbar snack = Snackbar.make(coordinate_layout, "Couldn't connect to Server!", Snackbar.LENGTH_LONG);
                snack.setActionTextColor(Color.parseColor("#FFF9C4"));
                snack.setAction("Re-connect", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        get_data();
                    }
                });
                snack.show();
            }
        }
    }

    private void set_values() {
        if(Common.light_list.size()>0){
            appliance_layout.setVisibility(View.VISIBLE);
            no_appliances.setVisibility(View.GONE);
//            appliance_list_adapter=new Appliance_list_adapter(this,activity,appliance_list);
//            list_appliances.setAdapter(appliance_list_adapter);
            list_appliances.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(activity.getApplicationContext(),"Could not connect to server",Toast.LENGTH_LONG).show();
        }
        appliance_list_adapter=new Appliance_list_adapter(this,activity,appliance_list);
        list_appliances.setAdapter(appliance_list_adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onStop(){
        super.onStop();
        timer.cancel();
    }
}
