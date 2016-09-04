package com.linqh.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * 服务运行状态工具类
 * @author dz1
 *
 */
public class ServiceStatusUtils {
	/**
	 * 检测服务是否运行
	 * @param context
	 * @return
	 */
	public static boolean isServiceRunning(Context context,String classname){
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> infos=am.getRunningServices(200);
		for(RunningServiceInfo info:infos){
			String runningclassname=info.service.getClassName();
			if(classname.equals(runningclassname)){
				return true;
			}
		}
		return false;	
	}
}
