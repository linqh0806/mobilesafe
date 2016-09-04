package com.linqh.mobilesafe.activities;

import com.linqh.mobilesafe.R;
import com.linqh.mobilesafe.utils.IntentUtils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {
	private TextView tv_lostfind_number;
	private ImageView iv_lostfind_status;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lost_find);
		tv_lostfind_number = (TextView) findViewById(R.id.tv_lostfind_number);
		iv_lostfind_status = (ImageView) findViewById(R.id.iv_lostfind_status);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		if (sp.getBoolean("protect_status", false)) {
			tv_lostfind_number.setText(sp.getString("safenumber", ""));
			iv_lostfind_status.setImageResource(R.drawable.lock);
		} else {
			tv_lostfind_number.setText("");
			iv_lostfind_status.setImageResource(R.drawable.unlock);
		}
	}

	public void reEntrySetup(View view) {
		IntentUtils.StartActivityAndFinish(LostFindActivity.this, Setup1Activity.class);
	}
}
