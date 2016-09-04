package com.linqh.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;

public class GpsService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		final LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		// 指定精确度
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 设置电量
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);

		String provider = lm.getBestProvider(criteria, true);
		System.out.println("最好的提供者：" + provider);

		lm.requestLocationUpdates(provider, 0, 0, new LocationListener() {
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
			}

			@Override
			public void onProviderEnabled(String provider) {
			}

			@Override
			public void onProviderDisabled(String provider) {
			}

			@Override
			public void onLocationChanged(Location location) {
				double Longitude = location.getLongitude();
				double Latitude = location.getLatitude();
				SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
				String text="Longitude：" + Double.toString(Longitude) + "Latitude：" + Double.toString(Latitude);
				SmsManager.getDefault().sendTextMessage(sp.getString("safenumber", ""),null,text.toString(), null, null);
				System.out.println("经度：" + Longitude + "/n纬度：" + Latitude);
				lm.removeUpdates(this);
				stopSelf();
			}
		});
	}
}
