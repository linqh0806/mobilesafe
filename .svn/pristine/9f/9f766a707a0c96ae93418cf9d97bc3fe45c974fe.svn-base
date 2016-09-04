package com.linqh.mobilesafe.activities;

import com.linqh.mobilesafe.R;
import com.linqh.mobilesafe.receiver.MyAdmin;
import com.linqh.mobilesafe.ui.SettingCheckView;
import com.linqh.mobilesafe.utils.IntentUtils;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Setup4Activity extends SetupBasicActivity {
	private SettingCheckView scv_setup4_status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		scv_setup4_status = (SettingCheckView) findViewById(R.id.scv_setup4_status);
	    scv_setup4_status.setChecked(sp.getBoolean("protect_status", false));
		scv_setup4_status.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				if (scv_setup4_status.isChecked()) {
					// 防盗功能未开启
					scv_setup4_status.setChecked(false);
					editor.putBoolean("protect_status", false);
				} else {
					// 防盗功能开启
					scv_setup4_status.setChecked(true);
					editor.putBoolean("protect_status", true);
				}
				editor.commit();
			}
		});

	}

	@Override
	public void show_pre() {
		IntentUtils.StartActivityAndFinish(Setup4Activity.this, Setup3Activity.class);
	}

	@Override
	public void show_next() {
		System.out.println("设置完成了. 修改配置文件..");
		Editor editor = sp.edit();
		editor.putBoolean("finishsetup", true);
		editor.commit();
		IntentUtils.StartActivityAndFinish(Setup4Activity.this, LostFindActivity.class);
	}
	/**
	 * 激活设备的超级管理员
	 * @param view
	 */
	public void activeAdmin(View view){
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		ComponentName who = new ComponentName(this,MyAdmin.class);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, who);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "开启后可以实现远程锁屏和销毁数据");
		startActivity(intent);
	}
}
