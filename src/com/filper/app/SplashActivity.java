package com.filper.app;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.app.Activity;
import android.graphics.Color;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_splash);
		LinearLayout layoutSplash = (LinearLayout) findViewById(R.id.layoutSplash);
		layoutSplash.setBackgroundColor(Color.WHITE);
	}



}
