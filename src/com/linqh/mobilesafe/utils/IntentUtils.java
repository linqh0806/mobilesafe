package com.linqh.mobilesafe.utils;

import android.app.Activity;
import android.content.Intent;

public class IntentUtils {
	/**
	 * 开启一个activity
	 * @param context
	 * @param cls
	 */
	public static void StartActivity(Activity context,Class<?> cls){
		Intent intent =new Intent(context,cls);
		context.startActivity(intent);
	}
	/**
	 * 
	 * @param context
	 * @param cls
	 */
	public static void StartActivityAndFinish(Activity context,Class<?> cls){
		Intent intent =new Intent(context,cls);
		context.startActivity(intent);
		context.finish();
	}
	/**
	 * 
	 * @param context
	 * @param cls
	 * @param delaytime
	 */
	public static void StartActivityForDelay(final Activity context,final Class<?> cls,final long delaytime){
		new Thread(){
			public void run() {
				try {
					Thread.sleep(delaytime);		
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent intent =new Intent(context,cls);
				context.startActivity(intent);
			};
		}.start();
	}
	/**
	 * 
	 * @param context
	 * @param cls
	 * @param delaytime
	 */
	public static void StartActivityForDelayAndFinish(final Activity context,final Class<?> cls,final long delaytime){
		new Thread(){
			public void run() {
				try {
					Thread.sleep(delaytime);		
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent intent =new Intent(context,cls);
				context.startActivity(intent);
				context.finish();
			};
		}.start();
	}
	
	
}
