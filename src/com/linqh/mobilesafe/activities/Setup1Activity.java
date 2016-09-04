package com.linqh.mobilesafe.activities;

import com.linqh.mobilesafe.R;
import com.linqh.mobilesafe.utils.IntentUtils;

import android.os.Bundle;

public class Setup1Activity extends SetupBasicActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}

	@Override
	public void show_pre() {

	}

	@Override
	public void show_next() {
		IntentUtils.StartActivityAndFinish(Setup1Activity.this, Setup2Activity.class);
	}

}
