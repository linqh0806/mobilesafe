package com.linqh.mobilesafe.activities;

import com.linqh.mobilesafe.R;
import com.linqh.mobilesafe.service.CallSmsService;
import com.linqh.mobilesafe.service.NumberLocationService;
import com.linqh.mobilesafe.ui.SettingChangeView;
import com.linqh.mobilesafe.ui.SettingCheckView;
import com.linqh.mobilesafe.utils.ServiceStatusUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class SettingActivity extends Activity {
	private SettingCheckView scv_setting_update;
	private SettingCheckView scv_setting_blacknumber;
	private SettingCheckView scv_setting_numberlocation;
	private SettingChangeView scv_setting_style;
	private SettingChangeView scv_setting_during;
	
	private String[] items=new String[]{"蓝","灰","绿","黄","白"};
	
	private SharedPreferences sp;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		scv_setting_update=(SettingCheckView) findViewById(R.id.scv_setting_update);
		sp=getSharedPreferences("config",MODE_PRIVATE);
		//设置手机是否开启自动更新
		boolean update=sp.getBoolean("update",false);
		scv_setting_update.setChecked(update);
		scv_setting_update.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Editor et=sp.edit();
				if(scv_setting_update.isChecked()){
					scv_setting_update.setChecked(false);
					et.putBoolean("update",false);
				}else {
					scv_setting_update.setChecked(true);
					et.putBoolean("update",true);
				}
				et.commit();
			}
		});
		//设置手机是否开启黑名单
		scv_setting_blacknumber=(SettingCheckView) findViewById(R.id.scv_setting_blacknumber);
		
		scv_setting_blacknumber.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(scv_setting_blacknumber.isChecked()){
					Intent service=new Intent(SettingActivity.this,CallSmsService.class);
					stopService(service);
					scv_setting_blacknumber.setChecked(false);
				}else {
					Intent service=new Intent(SettingActivity.this,CallSmsService.class);
					startService(service);
					scv_setting_blacknumber.setChecked(true);
				}
			}
		});
		//设置手机是否开启来电归属地提醒
		scv_setting_numberlocation=(SettingCheckView) findViewById(R.id.scv_setting_numberlocation);
		scv_setting_numberlocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(scv_setting_numberlocation.isChecked()){
					Intent service=new Intent(SettingActivity.this,NumberLocationService.class);
					stopService(service);
					scv_setting_numberlocation.setChecked(false);
				}else {
					Intent service=new Intent(SettingActivity.this,NumberLocationService.class);
					startService(service);
					scv_setting_numberlocation.setChecked(true);
				}
			}
		});
		
		//设置手机来电归属地对话框风格		
		scv_setting_style=(SettingChangeView) findViewById(R.id.scv_setting_style);
		scv_setting_style.setDescText(items[sp.getInt("which", 0)]);
		scv_setting_style.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder=new Builder(SettingActivity.this);
				builder.setTitle("选择归属地对话框");
				builder.setSingleChoiceItems(items,sp.getInt("which", 0), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Editor editor = sp.edit();
						editor.putInt("which", which);
						editor.commit();
						dialog.dismiss();
						scv_setting_style.setDescText(items[which]);						
					}
				});
				builder.show();
			}
		});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(ServiceStatusUtils.isServiceRunning(this,"com.linqh.mobilesafe.service.CallSmsService" )){
			scv_setting_blacknumber.setChecked(true);
		}else {
			scv_setting_blacknumber.setChecked(false);
		}
		if(ServiceStatusUtils.isServiceRunning(this,"com.linqh.mobilesafe.service.NumberLocationService" )){
			scv_setting_numberlocation.setChecked(true);
		}else {
			scv_setting_numberlocation.setChecked(false);
		}
	}
}
