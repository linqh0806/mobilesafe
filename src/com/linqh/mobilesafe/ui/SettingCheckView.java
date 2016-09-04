package com.linqh.mobilesafe.ui;

import com.linqh.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingCheckView extends LinearLayout {

	private TextView tv_uisetting;
	private CheckBox cb_uisetting;

	public SettingCheckView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initeView(context);
		String bigTitle = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.linqh.mobilesafe",
				"bigTitle");
		tv_uisetting.setText(bigTitle);
		TextSize(16.0f);
	}

	public SettingCheckView(Context context) {
		super(context);
		initeView(context);
	}

	private void initeView(Context context) {
		// TODO Auto-generated method stub
		this.setOrientation(VERTICAL);
		this.addView(View.inflate(context, R.layout.ui_setting_view, null));
		tv_uisetting = (TextView) findViewById(R.id.tv_uisetting);
		cb_uisetting = (CheckBox) findViewById(R.id.cb_uisetting);

	}

	public boolean isChecked() {
		return cb_uisetting.isChecked();
	}

	public void setChecked(boolean checked) {
		cb_uisetting.setChecked(checked);
	}

	public void TextSize(Float size) {
		tv_uisetting.setTextSize(size);
	}
}
