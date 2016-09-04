package com.linqh.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import com.linqh.mobilesafe.R;
import com.linqh.mobilesafe.domain.ProcessInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

public class TaskInfoProvider {
	public static List<ProcessInfo> getTaskInfoProvider(Context context){
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm=context.getPackageManager();
		List<RunningAppProcessInfo> runningAppProcessInfos=am.getRunningAppProcesses();
		List<ProcessInfo> processInfos=new ArrayList<ProcessInfo>();
		for(RunningAppProcessInfo runningAppProcessInfo:runningAppProcessInfos){
			ProcessInfo processInfo=new ProcessInfo();
			String packname=runningAppProcessInfo.processName;
			processInfo.setPackname(packname);
			long memsize=am.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid})[0].getTotalPrivateDirty()*1024;
			processInfo.setMemsize(memsize);
			try {
				PackageInfo packageInfo=pm.getPackageInfo(packname, 0);
				Drawable icon=packageInfo.applicationInfo.loadIcon(pm);
				processInfo.setIcon(icon);
				String appname=(String) packageInfo.applicationInfo.loadLabel(pm);
				processInfo.setAppname(appname);
				if((packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)!=0){
					//系统进程
					processInfo.setUsertask(false);
				}else {
					//用户进程
					processInfo.setUsertask(true);
				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();
				processInfo.setAppname(packname);
				processInfo.setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
			}
			processInfos.add(processInfo);
		}
		return processInfos;
		
	}

}
