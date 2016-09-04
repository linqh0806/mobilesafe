package com.linqh.mobilesafe.activities;


import com.linqh.mobilesafe.R;
import com.linqh.mobilesafe.engine.SmsTools;
import com.linqh.mobilesafe.engine.SmsTools.BackupSmsCallBack;
import com.linqh.mobilesafe.utils.IntentUtils;
import com.linqh.mobilesafe.utils.ToastUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

public class SuperToolsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supertools);
	}

	public void getLocation(View view) {
		IntentUtils.StartActivity(SuperToolsActivity.this, NumberLocationActivity.class);
	}

	public void commonnum(View view) {
		IntentUtils.StartActivity(SuperToolsActivity.this, CommonNumActivity.class);
	}

	public void smsReduction(View view){
		final ProgressDialog pd=new ProgressDialog(this);
		pd.setMessage("正在还原");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.show();
		new Thread() {
			@Override
			public void run() {
				boolean result = SmsTools.smsReduction(SuperToolsActivity.this,new BackupSmsCallBack() {
					@Override
					public void beforeSmsBackup(int max) {
						pd.setMax(max);
					}
					@Override
					public void onSmsBackup(int process) {
						pd.setProgress(process);
					}
				},"smsBackup.xml");
				if(result){
					ToastUtils.show(SuperToolsActivity.this, "还原成功");
				}else{
					ToastUtils.show(SuperToolsActivity.this, "还原失败");
				}
				pd.dismiss();
			}
		}.start();
	}
	
	public void smsBackup(View view) {
		final ProgressDialog pd=new ProgressDialog(this);
		pd.setMessage("正在备份");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.show();
		new Thread() {
			@Override
			public void run() {
				boolean result = SmsTools.smsBackup(SuperToolsActivity.this,new BackupSmsCallBack() {
					@Override
					public void beforeSmsBackup(int max) {
						pd.setMax(max);
					}
					@Override
					public void onSmsBackup(int process) {
						pd.setProgress(process);
					}
				},"smsBackup.xml");
				if(result){
					ToastUtils.show(SuperToolsActivity.this, "备份成功");
				}else{
					ToastUtils.show(SuperToolsActivity.this, "备份失败");
				}
				pd.dismiss();
			}
		}.start();

	}
}
