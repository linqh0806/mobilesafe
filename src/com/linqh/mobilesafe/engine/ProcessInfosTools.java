package com.linqh.mobilesafe.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;

import com.lidroid.xutils.http.client.multipart.content.ContentBody;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

public class ProcessInfosTools {
/**
 * 获取手机当前进程总数
 * @param context
 * @return
 */
	public static int getProcessCount(Context context){
		ActivityManager am= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcessInfos=am.getRunningAppProcesses();
		return appProcessInfos.size();
	}
	
	/**
	 * 获取手机可用内存
	 * @param context
	 * @return
	 */
	public static long getAvailRom(Context context){
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo memoryInfo=new MemoryInfo();
		am.getMemoryInfo(memoryInfo);
		return memoryInfo.availMem;
	}
	public static long getTotalRom(Context context){
//		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		MemoryInfo memoryInfo=new MemoryInfo();
//		am.getMemoryInfo(memoryInfo);
//		return memoryInfo.totalMem;
		try {
			File file=new File("proc/meminfo");
			FileInputStream fis=new FileInputStream(file);
			BufferedReader br=new BufferedReader(new InputStreamReader(fis));
			String total=br.readLine();
			StringBuffer mem = new StringBuffer();
			for(char c:total.toCharArray()){
				if(c>=48&&c<=57){
					mem.append(c);
				}
			}
			return Long.parseLong(mem.toString())*1024;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		
	}
}
