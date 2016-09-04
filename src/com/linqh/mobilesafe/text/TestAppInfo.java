package com.linqh.mobilesafe.text;

import com.linqh.mobilesafe.engine.AppInfosTools;

import android.test.AndroidTestCase;

public class TestAppInfo extends AndroidTestCase {
	public void getAppInfo(){
		AppInfosTools.getAppInfos(getContext());
	}
}
