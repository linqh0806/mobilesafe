package com.linqh.mobilesafe.utils;

import android.app.Activity;
import android.widget.Toast;

public class ToastUtils {

	public static void show(final Activity activity, final String text) {
		if ("main".equalsIgnoreCase(Thread.currentThread().getName())) {
			Toast.makeText(activity, text,Toast.LENGTH_SHORT).show();;
		} else {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(activity, text,Toast.LENGTH_SHORT).show();;
				}
			});
		}
	}
	
	public static void show(final Activity activity, final String text, final int lenth) {
		if ("main".equalsIgnoreCase(Thread.currentThread().getName())) {
			Toast.makeText(activity, text, lenth).show();;
		} else {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(activity, text, lenth).show();;
				}
			});
		}
	}
}
