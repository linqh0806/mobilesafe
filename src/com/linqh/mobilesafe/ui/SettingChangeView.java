package com.linqh.mobilesafe.ui;

import com.linqh.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingChangeView extends RelativeLayout {
	private TextView tv_title;
	private TextView tv_desc;
	
	public SettingChangeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		String Title=attrs.getAttributeValue("http://schemas.android.com/apk/res/com.linqh.mobilesafe", "Title");
		String Desc=attrs.getAttributeValue("http://schemas.android.com/apk/res/com.linqh.mobilesafe", "Desc");
		tv_title.setText(Title);
		tv_desc.setText(Desc);
	}

	public SettingChangeView(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		View.inflate(context, R.layout.ui_setting_changview, this);
		tv_title=(TextView) findViewById(R.id.tv_title);
		tv_desc=(TextView) findViewById(R.id.tv_desc);
	}
	
	public void setDescText(String text){
		tv_desc.setText(text);
	}
	
}
