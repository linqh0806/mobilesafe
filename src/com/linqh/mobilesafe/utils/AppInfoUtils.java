package com.linqh.mobilesafe.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class AppInfoUtils {
	/**
	 * 获取应用版本名称
	 * @param context
	 * @return
	 */
	public static String getAppVersionName(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo packinfo = pm.getPackageInfo(context.getPackageName(), 0);
			String versionName = packinfo.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// 不可能达到
			return "";
		}
	}

	/**
	 * 获取应用版本号
	 * @param context
	 * @return
	 */
	public static int getAppVersionCode(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo packinfo = pm.getPackageInfo(context.getPackageName(), 0);
			int versionCode = packinfo.versionCode;
			return versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
}
