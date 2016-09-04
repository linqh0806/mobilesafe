package com.linqh.mobilesafe.activities;

import com.linqh.mobilesafe.R;
import com.linqh.mobilesafe.db.dao.NumberLocationDao;
import com.linqh.mobilesafe.utils.ToastUtils;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

public class NumberLocationActivity extends Activity {
	EditText et_number_findlocation;
	TextView tv_number_resultlocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_getnumberlocation);
		et_number_findlocation = (EditText) findViewById(R.id.et_number_findlocation);
		tv_number_resultlocation = (TextView) findViewById(R.id.tv_number_resultlocation);
	}

	public void getLocation(View view) {
		String number = et_number_findlocation.getText().toString();
		if (TextUtils.isEmpty(number)) {
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
	        et_number_findlocation.startAnimation(shake);
			ToastUtils.show(NumberLocationActivity.this, "号码不能为空");
		} else {
			String location = NumberLocationDao.getNumberAddress(number);
			tv_number_resultlocation.setText("查询结果："+location);
		}
	}
}
