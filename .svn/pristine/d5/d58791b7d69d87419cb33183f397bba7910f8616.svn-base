package com.linqh.mobilesafe.activities;

import com.linqh.mobilesafe.R;
import com.linqh.mobilesafe.utils.IntentUtils;
import com.linqh.mobilesafe.utils.ToastUtils;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class Setup3Activity extends SetupBasicActivity {
	private EditText et_phone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		et_phone=(EditText) findViewById(R.id.et_phone);
		et_phone.setText(sp.getString("safenumber", null));
	}

	@Override
	public void show_pre() {
		IntentUtils.StartActivityAndFinish(Setup3Activity.this, Setup2Activity.class);
	}

	@Override
	public void show_next() {
		String phone=et_phone.getText().toString();
		if(TextUtils.isEmpty(phone)){
			ToastUtils.show(this,"安全号码不能为空");
			return;
		}
		Editor editor=sp.edit();
		editor.putString("safenumber", phone);
		editor.commit();
		IntentUtils.StartActivityAndFinish(Setup3Activity.this, Setup4Activity.class);
	}
	
	public void select(View view){
		Intent intent=new Intent(this,ContactActivity.class);
		startActivityForResult(intent, 0);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null){
			String phone=data.getStringExtra("phone").replace("-","").trim();
			et_phone.setText(phone);
		}
	}
}
