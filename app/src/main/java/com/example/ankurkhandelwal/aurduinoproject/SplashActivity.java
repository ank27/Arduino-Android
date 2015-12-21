package com.example.ankurkhandelwal.aurduinoproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends Activity {

	Intent intent = null;
    static Activity activity;
    CountDownTimer countDownTimer;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity=this;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		PlayServiceUtils playServiceUtils= new PlayServiceUtils(getApplicationContext(),this);
		playServiceUtils.register_app_using_gcm();
		intent = new Intent(this, Main.class);

	    countDownTimer = new CountDownTimer(1500, 500) {
			@Override
			public void onTick(long millisUntilFinished) {
			}
			@Override
			public void onFinish() {
					SplashActivity.this.startActivity(intent);
					SplashActivity.this.finish();
			}
		};
		countDownTimer.start();
    }

@Override
public void onStart()
{
	super.onStart();
}
}