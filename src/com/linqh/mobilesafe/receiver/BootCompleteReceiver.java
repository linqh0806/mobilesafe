package com.linqh.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("---------------------哈哈哈,手机启动完成了");
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		System.out.println("----------------------------------------------1");
		boolean protecting = sp.getBoolean("protect_status", false);
		if (protecting) {
			System.out.println("-------------手机防盗已开启");
			System.out.println("----------------------------------------------2");
			// 获取当前SIM卡串号
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			System.out.println("----------------------------------------------3");
			String realsim = tm.getSimSerialNumber();
			System.out.println("----------------------------------------------4");
			// 获取先前SIM卡串号
			String presim = sp.getString("sim", "") + "asd";
			System.out.println("----------------------------------------------5");

			if (realsim.equals(presim)) {
				
				System.out.println("------------------SIM未改变");
			}else {
				SmsManager.getDefault().sendTextMessage(sp.getString("safenumber", ""), null, "SIM chang", null, null);
			}
		} else {
			System.out.println("------------------------------手机防盗未开启");
		}
	}

}
