package com.linqh.mobilesafe.receiver;

import com.linqh.mobilesafe.R;
import com.linqh.mobilesafe.service.GpsService;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		ComponentName who = new ComponentName(context, MyAdmin.class);
		for (Object obj : objs) {
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
			String body = smsMessage.getMessageBody();
			if ("#*location*#".equals(body)) {
				Log.i("TAG", "返回手机的位置信息");
				Intent service=new Intent(context, GpsService.class);
				context.startService(service);
				abortBroadcast();
			} else if ("#*alarm*#".equals(body)) {
				Log.i("TAG", "播放报警音乐");
				MediaPlayer player = MediaPlayer.create(context, R.raw.mymusic);
				player.setVolume(1.0f, 1.0f);
				player.start();
				abortBroadcast();
			} else if ("#*wipedate*#".equals(body)) {
				Log.i("TAG", "远程销毁数据");
				if (dpm.isAdminActive(who)) {
					dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
				}
				abortBroadcast();
			} else if ("#*lockscreen*#".equals(body)) {
				Log.i("TAG", "远程锁屏");
				if (dpm.isAdminActive(who)) {
					dpm.resetPassword("123", 0);
					dpm.lockNow();
				}
				abortBroadcast();
			}
		}
	}

}
