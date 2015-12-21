package com.example.ankurkhandelwal.aurduinoproject;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.ankurkhandelwal.aurduinoproject.Adapter.Appliance_list_adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApplianceActivity extends Activity {
    Toolbar toolbar;
    ListView list_appliances;
    HashMap<String, String> appliance_list;
    Appliance_list_adapter appliance_list_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appliance);
//        getWindow().setEnterTransition(transition);
        list_appliances=(ListView) findViewById(R.id.list_appliances);

        toolbar = (Toolbar) findViewById(R.id.toolbar_appliance);
        toolbar.setTitle("MyAppliances");
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_action_back_black));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                
            }
        });
        appliance_list=Common.light_list;
//        appliance_list_adapter=new Appliance_list_adapter(this,appliance_list);
//        list_appliances.setAdapter(appliance_list_adapter);

    }
}
