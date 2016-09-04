package com.linqh.mobilesafe.activities;

import com.linqh.mobilesafe.R;
import com.linqh.mobilesafe.ui.SettingCheckView;
import com.linqh.mobilesafe.utils.IntentUtils;
import com.linqh.mobilesafe.utils.ToastUtils;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

public class Setup2Activity extends SetupBasicActivity {
	private SettingCheckView scv_setup2_bindsim;
	private TelephonyManager tm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		scv_setup2_bindsim = (SettingCheckView) findViewById(R.id.scv_setup2_bindsim);
		tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		String sim=sp.getString("sim",null);
		if (TextUtils.isEmpty(sim)) {
			scv_setup2_bindsim.setChecked(false);
		} else {
			scv_setup2_bindsim.setChecked(true);
		}
		scv_setup2_bindsim.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (scv_setup2_bindsim.isChecked()) {
					// SIM卡绑定获取SIM卡串号
					scv_setup2_bindsim.setChecked(false);
					Editor editor = sp.edit();
					editor.putString("sim", null);
					editor.commit();
				} else {
					// SIM卡未进行绑定
					scv_setup2_bindsim.setChecked(true);
					String sim_number = tm.getSimSerialNumber();
					Editor editor = sp.edit();
					editor.putString("sim", sim_number);
					editor.commit();
				}
			}
		});

	}

	@Override
	public void show_pre() {
		IntentUtils.StartActivityAndFinish(Setup2Activity.this, Setup1Activity.class);
	}

	@Override
	public void show_next() {
		if(!scv_setup2_bindsim.isChecked()){
			ToastUtils.show(this, "SIM未绑定，无法进行下一步操作");
			return;
		}
		IntentUtils.StartActivityAndFinish(Setup2Activity.this, Setup3Activity.class);
	}
}
