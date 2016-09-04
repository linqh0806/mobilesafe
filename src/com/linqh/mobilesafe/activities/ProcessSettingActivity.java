package com.linqh.mobilesafe.activities;

import com.linqh.mobilesafe.R;
import com.linqh.mobilesafe.ui.SettingCheckView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ProcessSettingActivity extends Activity {
	private SettingCheckView scv_setting_sys;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_setting);
		sp=getSharedPreferences("config", MODE_PRIVATE);
		scv_setting_sys=(SettingCheckView) findViewById(R.id.scv_setting_sys);
		if(sp.getBoolean("show_system", false)){
			scv_setting_sys.setChecked(true);
		}else {
			scv_setting_sys.setChecked(false);
		}
		scv_setting_sys.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(scv_setting_sys.isChecked()){
					scv_setting_sys.setChecked(false);
					Editor editor=sp.edit();
					editor.putBoolean("show_system", false);
					editor.commit();
				}else {
					scv_setting_sys.setChecked(true);
					Editor editor=sp.edit();
					editor.putBoolean("show_system", true);
					editor.commit();
				}
			}
		});
	}

}
