package com.linqh.mobilesafe.engine;

import java.io.File;
import java.util.ArrayList;
import android.text.format.Formatter;
import java.util.List;

import com.linqh.mobilesafe.domain.AppInfo;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class AppInfosTools {
	
	public static List<AppInfo> getAppInfos(Context context){
		PackageManager pm=context.getPackageManager();
		List<PackageInfo> packageInfos=pm.getInstalledPackages(0);
		List<AppInfo> appInfos=new ArrayList<AppInfo>();
		for(PackageInfo packageInfo:packageInfos){
			AppInfo appInfo=new AppInfo();
			String packageName=packageInfo.packageName;
			appInfo.setPackname(packageName);
			String appname=packageInfo.applicationInfo.loadLabel(pm).toString();
			appInfo.setAppname(appname);
			Drawable icon=packageInfo.applicationInfo.loadIcon(pm);
			appInfo.setIcon(icon);
			String path=packageInfo.applicationInfo.sourceDir;
			File file=new File(path);
			long size=file.length();
			appInfo.setApksize(size);
//			System.out.println("PackName:"+packageName);
//			System.out.println("AppName:"+appname);
//			System.out.println("Icon:"+icon);
//			System.out.println("Size:"+size);
//			System.out.println("Path:"+path);
//			System.out.println("--------------------------------");
			int flags=packageInfo.applicationInfo.flags;
			if((flags&ApplicationInfo.FLAG_SYSTEM)==0){
				//用户程序
				appInfo.setUserApp(true);
			}else {
				//系统程序
				appInfo.setUserApp(false);
			}
			if((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)==0){
				//手机内存
				appInfo.setInRom(true);
			}else {
				//SD存储
				appInfo.setInRom(false);
			}
			appInfos.add(appInfo);
		}
		return appInfos;
		
	}
}
